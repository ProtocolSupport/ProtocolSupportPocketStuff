package protocolsupportpocketstuff.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.ArrayList;

public class ResourcePackListener extends Connection.PacketListener {
	private ProtocolSupportPocketStuff plugin;
	private Connection connection;
	private int REFUSED = 1;
	private int SEND_PACKS = 2;
	private int HAVE_ALL_PACKS = 3;
	private int COMPLETED = 4;
	private boolean startThrottle = false;
	private boolean downloadedAllPacks = false;
	private ArrayList<ByteBuf> throttledPackets = new ArrayList<ByteBuf>(); // TODO: Is this really necessary? idk

	public ResourcePackListener(ProtocolSupportPocketStuff plugin, Connection connection) {
		this.plugin = plugin;
		this.connection = connection;
	}

	@Override
	public void onRawPacketSending(RawPacketEvent event) {
		super.onRawPacketSending(event);

		int packetId = VarNumberSerializer.readVarInt(event.getData());

		if (packetId == PEPacketIDs.RESOURCE_PACK) {
			startThrottle = true;
			// We don't care about the previous data
			ByteBuf serializer = Unpooled.buffer();
			// header
			VarNumberSerializer.writeVarInt(serializer, PEPacketIDs.RESOURCE_PACK);
			serializer.writeByte(0);
			serializer.writeByte(0);
			// body
			serializer.writeBoolean(plugin.getConfig().getBoolean("resource-behavior-packs.force-resources", false));
			serializer.writeShortLE(0); // beh pack count
			serializer.writeShortLE(1); // res pack count
			StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, "53644fac-a276-42e5-843f-a3c6f169a9ab"); // from the manifest.json file -> header.uuid
			StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, "3.2.0"); // TODO: the version is an array in the manifest.json
			File file = new File("D:\\Minecraft Servers\\PocketDreams v2\\PocketDreams Survival\\plugins\\ProtocolSupportPocketStuff\\resource_packs\\test.mcpack");
			long size = file.length();
			System.out.println("Resource Pack size: " + size);
			serializer.writeLongLE(size);
			StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, ""); // ???
			StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, ""); // ???

			event.setData(serializer);

			System.out.println("Replaced old data with new data...");
			return;
		}
		if (packetId == PEPacketIDs.RESOURCE_STACK && !downloadedAllPacks) {
			event.setCancelled(true);
			System.out.println("Cancelled stack data...");
			return;
		}

		if (!startThrottle) { return; }
		event.setCancelled(true);
		throttledPackets.add(event.getData().copy());
	}

	@Override
	public void onRawPacketReceiving(RawPacketEvent event) {
		super.onRawPacketReceiving(event);
		ByteBuf buf = event.getData().copy();
		int packetId = VarNumberSerializer.readVarInt(buf);

		buf.readByte();
		buf.readByte();

		if (packetId == 8) {
			int status = buf.readByte();

			System.out.println("Resource pack status: " + status);
			int entryCount = buf.readShortLE();
			int idx = 0;
			while (entryCount > idx) {
				String packId = StringSerializer.readString(buf, ProtocolVersion.MINECRAFT_PE);
				System.out.println(idx + ". " + packId);
				idx++;
			}

			if (status == HAVE_ALL_PACKS) {
				System.out.println("So you already have every resource pack? nice! here, take this packet too.");
				downloadedAllPacks = true;
				startThrottle = true;
				// We don't care about the previous data
				ByteBuf serializer = Unpooled.buffer();
				// header
				VarNumberSerializer.writeVarInt(serializer, PEPacketIDs.RESOURCE_STACK);
				serializer.writeByte(0);
				serializer.writeByte(0);
				// body
				serializer.writeBoolean(plugin.getConfig().getBoolean("resource-behavior-packs.force-resources", false));
				VarNumberSerializer.writeVarInt(serializer, 0); // beh pack count
				VarNumberSerializer.writeVarInt(serializer, 1); // res pack count
				StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, "53644fac-a276-42e5-843f-a3c6f169a9ab"); // from the manifest.json file -> header.uuid
				StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, "3.2.0"); // TODO: the version is an array in the manifest.json
				StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, ""); // ???
				connection.sendRawPacket(MiscSerializer.readAllBytes(serializer));

				for (ByteBuf throttled : throttledPackets) {
					connection.sendRawPacket(MiscSerializer.readAllBytes(throttled));
				}

				throttledPackets.clear();
				return;
			}
			if (status == SEND_PACKS) {
				System.out.println("so... uhh... do you want some resource packs?");
				// so... uhh... do you want some resource packs?
				// We don't care about the previous data
				ByteBuf serializer = Unpooled.buffer();
				// header
				VarNumberSerializer.writeVarInt(serializer, 82);
				serializer.writeByte(0);
				serializer.writeByte(0);
				StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, "53644fac-a276-42e5-843f-a3c6f169a9ab");
				serializer.writeIntLE(1048576); // max chunk size 1MB

				File file = new File("D:\\Minecraft Servers\\PocketDreams v2\\PocketDreams Survival\\plugins\\ProtocolSupportPocketStuff\\resource_packs\\test.mcpack");
				long size = file.length();

				System.out.println("Chunk Count: " + ((int) size / 1048576));
				serializer.writeIntLE((int) size / 1048576); // chunk count
				serializer.writeLongLE(size); // res pack size

				try {
					byte[] buffer= new byte[8192];
					int count;
					MessageDigest digest = MessageDigest.getInstance("SHA-256");
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					while ((count = bis.read(buffer)) > 0) {
						digest.update(buffer, 0, count);
					}
					byte[] hash = digest.digest();

					VarNumberSerializer.writeVarInt(serializer, hash.length);
					for (byte b : hash) {
						serializer.writeByte(b);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				connection.sendRawPacket(MiscSerializer.readAllBytes(serializer));
				return;
			}
		}

		if (packetId == 84) {
			String packId = StringSerializer.readString(buf, ProtocolVersion.MINECRAFT_PE);
			long chunkIdx = buf.readUnsignedIntLE();

			System.out.println("Hey server, can you give me the part " + chunkIdx + " of the " + packId + " res/beh pack? thx");

			ByteBuf payload = sendPackChunk(packId, chunkIdx);
			connection.sendRawPacket(MiscSerializer.readAllBytes(payload));
		}
	}

	public ByteBuf sendPackChunk(String id, long chunkIdx) {
		// We don't care about the previous data
		ByteBuf serializer = Unpooled.buffer();
		// header
		VarNumberSerializer.writeVarInt(serializer, 83);
		serializer.writeByte(0);
		serializer.writeByte(0);
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, id);
		serializer.writeIntLE((int) chunkIdx);
		serializer.writeLongLE(1048576 * chunkIdx);
		byte[] array = getPackChunk(chunkIdx);
		serializer.writeIntLE(array.length);
		serializer.writeBytes(array);
		return serializer;
	}

	public byte[] getPackChunk(long chunkIdx) {
		File file = new File("D:\\Minecraft Servers\\PocketDreams v2\\PocketDreams Survival\\plugins\\ProtocolSupportPocketStuff\\resource_packs\\test.mcpack");

		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			int arraySize = 1048576;
			int offset = 1048576 * (int) chunkIdx;

			int distanceToTheEnd = (int) raf.length() - offset;

			System.out.println("Remaining bytes: " + distanceToTheEnd);

			if (arraySize > distanceToTheEnd) {
				arraySize = distanceToTheEnd;
			}

			byte[] array = new byte[arraySize];

			raf.seek(offset);
			raf.read(array, 0, arraySize);

			return array;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("something went very wrong");
	}
}
