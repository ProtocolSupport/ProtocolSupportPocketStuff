package protocolsupportpocketstuff.util;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.PocketStuffAPI;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.packet.play.DisconnectPacket;
import protocolsupportpocketstuff.packet.play.ResourcePackChunkDataPacket;
import protocolsupportpocketstuff.packet.play.ResourcePackDataInfoPacket;
import protocolsupportpocketstuff.packet.play.ResourcePackStackPacket;
import protocolsupportpocketstuff.packet.play.ResourcesPackInfoPacket;
import protocolsupportpocketstuff.resourcepacks.ResourcePack;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResourcePackListener extends Connection.PacketListener {
	private ProtocolSupportPocketStuff plugin;
	private Connection connection;
	private int REFUSED = 1;
	private int SEND_PACKS = 2;
	private int HAVE_ALL_PACKS = 3;
	//private int COMPLETED = 4;
	private boolean startThrottle = false;
	private boolean downloadedAllPacks = false;
	private ArrayList<ByteBuf> throttledPackets = new ArrayList<ByteBuf>();

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

			boolean forceResources = PocketStuffAPI.getResourcePackManager().resourcePacksRequired();
			ResourcesPackInfoPacket infoPacket = new ResourcesPackInfoPacket(forceResources, PocketStuffAPI.getResourcePackManager().getBehaviorPacks(), PocketStuffAPI.getResourcePackManager().getResourcePacks());
			event.setData(infoPacket.encode(connection));

			plugin.debug("Replaced old data with new data...");
			return;
		}
		if (packetId == PEPacketIDs.RESOURCE_STACK && !downloadedAllPacks) {
			event.setCancelled(true);
			plugin.debug("Cancelled stack data...");
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

		if (packetId == PEPacketIDs.RESOURCE_RESPONSE) {
			int status = buf.readByte();

			plugin.debug("Resource pack status: " + status);

			if (status == REFUSED) {
				PocketCon.sendPocketPacket(connection, new DisconnectPacket(false, "You must accept resource packs to join this server."));
				return;
			}

			ArrayList<String> missingPacks = new ArrayList<String>();
			int entryCount = buf.readShortLE();
			for (int idx = 0; entryCount > idx; idx++) {
				missingPacks.add(StringSerializer.readString(buf, ProtocolVersion.MINECRAFT_PE));
			}

			if (status == HAVE_ALL_PACKS) {
				plugin.debug("So you already have every resource pack? nice! here, take this packet too.");
				downloadedAllPacks = true;
				startThrottle = false;

				boolean forceResources = PocketStuffAPI.getResourcePackManager().resourcePacksRequired();
				ResourcePackStackPacket stackPacket = new ResourcePackStackPacket(forceResources, PocketStuffAPI.getResourcePackManager().getBehaviorPacks(), PocketStuffAPI.getResourcePackManager().getResourcePacks());
				PocketCon.sendPocketPacket(connection, stackPacket);

				for (ByteBuf throttled : throttledPackets) {
					connection.sendRawPacket(MiscSerializer.readAllBytes(throttled));
				}

				throttledPackets.clear();
				return;
			}
			if (status == SEND_PACKS) {
				plugin.debug("so... uhh... do you want some resource packs?");

				// so... uhh... do you want some resource packs?
				// We don't care about the previous data
				ArrayList<ResourcePack> packs = new ArrayList<ResourcePack>();
				packs.addAll(PocketStuffAPI.getResourcePackManager().getBehaviorPacks().stream().filter( it -> missingPacks.contains(it.getPackId())).collect(Collectors.toList()));
				packs.addAll(PocketStuffAPI.getResourcePackManager().getResourcePacks().stream().filter( it -> missingPacks.contains(it.getPackId())).collect(Collectors.toList()));

				for (ResourcePack pack : packs) {
					PocketCon.sendPocketPacket(connection, new ResourcePackDataInfoPacket(pack));
				}
				return;
			}
		}

		if (packetId == PEPacketIDs.RESOURCE_REQUEST) {
			String packId = StringSerializer.readString(buf, ProtocolVersion.MINECRAFT_PE);
			int chunkIdx = (int) buf.readUnsignedIntLE();

			plugin.debug("Sending part " + chunkIdx + " of the " + packId + " res/beh pack");

			ArrayList<ResourcePack> packs = new ArrayList<ResourcePack>();
			packs.addAll(PocketStuffAPI.getResourcePackManager().getBehaviorPacks());
			packs.addAll(PocketStuffAPI.getResourcePackManager().getResourcePacks());

			Optional<ResourcePack> pack = packs.stream().filter(it -> it.getPackId().equals(packId)).findFirst();

			if (!pack.isPresent()) {
				plugin.debug("Client requested pack " + packId + ", however I don't have it!");
				PocketCon.sendPocketPacket(connection, new DisconnectPacket(false, "Got a resource pack chunk request for unknown pack with UUID " + packId));
				return;
			}

			PocketCon.sendPocketPacket(connection, new ResourcePackChunkDataPacket(packId, chunkIdx, pack.get().getPackChunk(chunkIdx)));
		}
	}
}