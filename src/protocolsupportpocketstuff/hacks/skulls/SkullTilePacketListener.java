package protocolsupportpocketstuff.hacks.skulls;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_13_R2.PacketPlayOutRespawn;
import org.apache.commons.lang3.Validate;
import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.EntityMetadata.PeMetaBase;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.ItemStackSerializer;
import protocolsupport.protocol.serializer.PositionSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectFloatLe;
import protocolsupport.protocol.utils.types.Position;
import protocolsupport.protocol.utils.types.nbt.NBTCompound;
import protocolsupport.protocol.utils.types.nbt.NBTList;
import protocolsupport.protocol.utils.types.nbt.NBTType;
import protocolsupport.protocol.utils.types.nbt.serializer.PENBTSerializer;
import protocolsupport.utils.CollectionsUtils;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.packet.play.EntityDestroyPacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.packet.play.SpawnPlayerPacket;
import protocolsupportpocketstuff.packet.play.TileDataUpdatePacket;
import protocolsupportpocketstuff.storage.Skins;
import protocolsupportpocketstuff.util.GsonUtils;
import protocolsupportpocketstuff.util.StuffUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.SplittableRandom;
import java.util.UUID;

public class SkullTilePacketListener extends Connection.PacketListener {

	private final ConnectionImpl con;
	private final ProtocolSupportPocketStuff plugin;
	private final HashMap<Long, CachedSkullBlock> cachedSkullBlocks = new HashMap<>();
	private static final String SKULL_MODEL = StuffUtils.getResourceAsString("models/fake_skull_block.json");

	// Constants
	private static final int SKULL_RUNTIME_ID_0 = 1096;
	private static final int SKULL_RUNTIME_ID_1 = SKULL_RUNTIME_ID_0 + 1;
	private static final int SKULL_RUNTIME_ID_2 = SKULL_RUNTIME_ID_1 + 1;
	private static final int SKULL_RUNTIME_ID_3 = SKULL_RUNTIME_ID_2 + 1;
	private static final int SKULL_RUNTIME_ID_4 = SKULL_RUNTIME_ID_3 + 1;
	private static final int SKULL_RUNTIME_ID_5 = SKULL_RUNTIME_ID_4 + 1;
	private static final int SKULL_RUNTIME_ID_6 = SKULL_RUNTIME_ID_5 + 1;
	private static final int SKULL_RUNTIME_ID_7 = SKULL_RUNTIME_ID_6 + 1;
	private static final int SKULL_RUNTIME_ID_8 = SKULL_RUNTIME_ID_7 + 1;
	private static final int SKULL_RUNTIME_ID_9 = SKULL_RUNTIME_ID_8 + 1;
	private static final int SKULL_RUNTIME_ID_10 = SKULL_RUNTIME_ID_9 + 1;
	private static final int SKULL_RUNTIME_ID_11 = SKULL_RUNTIME_ID_10 + 1;
	private static final int SKULL_RUNTIME_ID_12 = SKULL_RUNTIME_ID_11 + 1;
	private static final int SKULL_RUNTIME_ID_13 = SKULL_RUNTIME_ID_12 + 1;
	private static final int SKULL_RUNTIME_ID_14 = SKULL_RUNTIME_ID_13 + 1;
	private static final int SKULL_RUNTIME_ID_15 = SKULL_RUNTIME_ID_14 + 1;

	public SkullTilePacketListener(ProtocolSupportPocketStuff plugin, ConnectionImpl con) {
		this.plugin = plugin;
		this.con = con;
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		// ===[ WORLD SWITCH ]===
		if (event.getPacket() instanceof PacketPlayOutRespawn) {
			cachedSkullBlocks.clear();
			return;
		}
	}

	@Override
	public void onRawPacketSending(RawPacketEvent event) {
		ByteBuf data = event.getData();
		int packetId = VarNumberSerializer.readVarInt(data);

		if (packetId == PEPacketIDs.TILE_DATA_UPDATE) {
			Position position = new Position(0, 0, 0);
			PositionSerializer.readPEPositionTo(data, position);
			try {
				NBTCompound tag = PENBTSerializer.VI_INSTANCE.deserializeTag(data);

				if (!isSkull(tag))
					return;

				handleSkull(position, tag, -1);

				tag.removeTag("Owner"); // remove the owner tag, if we don't remove it, the skull stays with the default parameters (skeleton with rot 0)
				event.setData(new TileDataUpdatePacket(position.getX(), position.getY(), position.getZ(), tag).encode(con));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		if (packetId == PEPacketIDs.UPDATE_BLOCK) {
			Position position = new Position(0, 0, 0);
			PositionSerializer.readPEPositionTo(data, position);
			int id = VarNumberSerializer.readVarInt(data);
			/*int flagsAndDataValue = */VarNumberSerializer.readVarInt(data);

			long asLong = StuffUtils.convertPositionToLong(position);

			/*int flags = 0;

			if ((flagsAndDataValue & 0x01) != 0) {
				flags += 0x01;
			}
			if ((flagsAndDataValue & 0x02) != 0) {
				flags += 0x02;
			}
			if ((flagsAndDataValue & 0x04) != 0) {
				flags += 0x04;
			}
			if ((flagsAndDataValue & 0x08) != 0) {
				flags += 0x08;
			}*/

			int dataValue = id - SKULL_RUNTIME_ID_0;

			if (id == SKULL_RUNTIME_ID_0 ||
				id == SKULL_RUNTIME_ID_1 ||
				id == SKULL_RUNTIME_ID_2 ||
				id == SKULL_RUNTIME_ID_3 ||
				id == SKULL_RUNTIME_ID_4 ||
				id == SKULL_RUNTIME_ID_5 ||
				id == SKULL_RUNTIME_ID_6 ||
				id == SKULL_RUNTIME_ID_7 ||
				id == SKULL_RUNTIME_ID_8 ||
				id == SKULL_RUNTIME_ID_9 ||
				id == SKULL_RUNTIME_ID_10 ||
				id == SKULL_RUNTIME_ID_11 ||
				id == SKULL_RUNTIME_ID_12 ||
				id == SKULL_RUNTIME_ID_13 ||
				id == SKULL_RUNTIME_ID_14 ||
				id == SKULL_RUNTIME_ID_15) {
				if (!cachedSkullBlocks.containsKey(asLong)) {
					CachedSkullBlock cachedSkullBlock = new CachedSkullBlock(position);
					cachedSkullBlock.dataValue = dataValue; // Store the new dataValue from this block
					cachedSkullBlocks.put(asLong, cachedSkullBlock);
					return;
				}

				CachedSkullBlock cachedSkullBlock = cachedSkullBlocks.get(asLong);
				if (cachedSkullBlock.dataValue != dataValue) { // We only need to respawn the block if the data value has changed
					cachedSkullBlock.dataValue = dataValue;
					cachedSkullBlock.destroy(this);
					cachedSkullBlock.spawn(this);
				}
				return;
			}

			if (cachedSkullBlocks.containsKey(asLong)) {
				cachedSkullBlocks.get(asLong).destroy(this);
				cachedSkullBlocks.remove(asLong);
			}
			return;
		}
		if (packetId == PEPacketIDs.CHUNK_DATA) {
			int chunkX = VarNumberSerializer.readSVarInt(data); // chunk X
			int chunkZ = VarNumberSerializer.readSVarInt(data); // chunk Z
			byte[] byteArray = ArraySerializer.readVarIntByteArray(data);

			ByteBuf chunkdata = Unpooled.wrappedBuffer(byteArray);
			int sections = chunkdata.readByte(); // how many sections we have in this chunk

			int chunkXStart = chunkX << 4;
			int chunkZStart = chunkZ << 4;

			HashMap<Long, Integer> skulls = new HashMap<>();

			for (int i = 0; i < sections; i++) {
				chunkdata.readByte(); // subchunk version

				// Borrowed from https://gist.github.com/Tomcc/a96af509e275b1af483b25c543cfbf37 ~ thanks 7kasper!
				int paletteAndFlag = chunkdata.readByte();
				boolean isRuntime = (paletteAndFlag & 1) != 0;
				int bitsPerBlock = paletteAndFlag >> 1;
				int blocksPerWord = (int) Math.floor(32 / bitsPerBlock);
				int wordCount = (int) Math.ceil(4096.0 / blocksPerWord);
				int blockIndex = chunkdata.readerIndex();
				chunkdata.skipBytes(wordCount * 4); //4 bytes per word.

				HashMap<Integer, Integer> palette = new HashMap<Integer, Integer>();

				if (isRuntime) {
					int palleteSize = VarNumberSerializer.readSVarInt(chunkdata);
					for (int palletId = 0; palletId < palleteSize; palletId++) {
						int runtimeId = VarNumberSerializer.readSVarInt(chunkdata);
						palette.put(palletId, runtimeId);
					}
				} else {
					throw new RuntimeException("Trying to read non-runtime chunk data on runtime!");
				}

				int afterPaletteIndex = chunkdata.readerIndex();
				chunkdata.readerIndex(blockIndex);
				int position = 0;
				for (int wordi = 0; wordi < wordCount; wordi++) {
					int word = chunkdata.readIntLE();
					for (int block = 0; block < blocksPerWord; block++) {
						int state = (word >> ((position % blocksPerWord) * bitsPerBlock)) & ((1 << bitsPerBlock) - 1);
						int x = (position >> 8) & 0xF;
						int y = position & 0xF;
						int z = (position >> 4) & 0xF;

						int blockId = palette.get(state);

						if (blockId == SKULL_RUNTIME_ID_0 ||
							blockId == SKULL_RUNTIME_ID_1 ||
							blockId == SKULL_RUNTIME_ID_2 ||
							blockId == SKULL_RUNTIME_ID_3 ||
							blockId == SKULL_RUNTIME_ID_4 ||
							blockId == SKULL_RUNTIME_ID_5 ||
							blockId == SKULL_RUNTIME_ID_6 ||
							blockId == SKULL_RUNTIME_ID_7 ||
							blockId == SKULL_RUNTIME_ID_8 ||
							blockId == SKULL_RUNTIME_ID_9 ||
							blockId == SKULL_RUNTIME_ID_10 ||
							blockId == SKULL_RUNTIME_ID_11 ||
							blockId == SKULL_RUNTIME_ID_12 ||
							blockId == SKULL_RUNTIME_ID_13 ||
							blockId == SKULL_RUNTIME_ID_14 ||
							blockId == SKULL_RUNTIME_ID_15) {
							skulls.put(StuffUtils.convertCoordinatesToLong(chunkXStart + x, (i * 16) + y, chunkZStart + z), blockId);
						}
						position++;
					}
				}
				chunkdata.readerIndex(afterPaletteIndex);
			}

			chunkdata.skipBytes(512); // height map
			chunkdata.skipBytes(256); // biome data

			chunkdata.readByte(); // borders
			VarNumberSerializer.readSVarInt(chunkdata); // extra data

			while (chunkdata.readableBytes() != 0) {
				NBTCompound tag = ItemStackSerializer.readTag(chunkdata, true, con.getVersion());

				if (!isSkull(tag))
					continue;

				int x = tag.getNumberTag("x").getAsInt();
				int y = tag.getNumberTag("y").getAsInt();
				int z = tag.getNumberTag("z").getAsInt();

				Position position = new Position(x, y, z);

				Integer id = skulls.getOrDefault(StuffUtils.convertPositionToLong(position), -1);

				if (id == -1) {
					plugin.debug("Skull at " + x + ", " + y + ", " + z + " has NBT tag, but doesn't have any ID! Bug?");
					return;
				}

				int dataValue = id - SKULL_RUNTIME_ID_0; // If we do the current ID - first skull related ID, we will get the good old data value

				// Is there's any possibility of an skull being on the chunk nbt tags but not really in the world? idk
				// So that's why getOrDefault is used
				handleSkull(position, tag, dataValue);
			}
		}
	}

	public boolean isSkull(NBTCompound tag) {
		System.out.println("isSkull " + tag.getTagOfType("id", NBTType.STRING).getValue());
		return tag.getTagOfType("id", NBTType.STRING).getValue().equals("Skull");
	}

	public String getUrlFromSkull(NBTCompound tag, boolean isItem) {
		NBTCompound owner = tag.getTagOfType(isItem ? "SkullOwner" : "Owner", NBTType.COMPOUND);
		if (owner == null) return null;

		NBTCompound properties = owner.getTagOfType("Properties", NBTType.COMPOUND);
		if (properties == null) return null;

		NBTList<NBTCompound> textures = properties.getTagListOfType("textures", NBTType.COMPOUND);
		if (textures == null) return null;

		String value = textures.getTag(0).getTagOfType("Value", NBTType.STRING).getValue();

		String _json = new String(Base64.getDecoder().decode(value));

		JsonObject json = GsonUtils.JSON_PARSER.parse(_json).getAsJsonObject();

		return json.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
	}

	public void handleSkull(Position position, NBTCompound tag, int dataValue) {
		if (position == null && tag == null) {
			throw new RuntimeException("Both Position and NBTCompound are null!");
		}
		if (position == null) {
			int x = tag.getNumberTag("x").getAsInt();
			int y = tag.getNumberTag("y").getAsInt();
			int z = tag.getNumberTag("z").getAsInt();

			position = new Position(x, y, z);
		}

		CachedSkullBlock cachedSkullBlock = cachedSkullBlocks.getOrDefault(StuffUtils.convertPositionToLong(position), new CachedSkullBlock(position));
		if (dataValue != -1)
			cachedSkullBlock.dataValue = dataValue;

		if (tag != null) {
			String url = getUrlFromSkull(tag, false);

			if (url != null) {
				cachedSkullBlock.url = url;
				cachedSkullBlock.tag = tag;
			}
		}

		cachedSkullBlocks.put(StuffUtils.convertPositionToLong(position), cachedSkullBlock);

		if (!cachedSkullBlock.isCustomSkull())
			return;

		if (cachedSkullBlock.isSpawned) {
			cachedSkullBlock.destroy(this);
		}

		cachedSkullBlock.spawn(this);
	}

	protected static byte[] toData(BufferedImage skin) {
		Validate.isTrue(skin.getWidth() == 64, "Must be 64 pixels wide");
		Validate.isTrue((skin.getHeight() == 64) || (skin.getHeight() == 32), "Must be 64 or 32 pixels high");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		for (int y = 0; y < skin.getHeight(); y++) {
			for (int x = 0; x < skin.getWidth(); x++) {
				Color color = new Color(skin.getRGB(x, y), true);
				stream.write(color.getRed());
				stream.write(color.getGreen());
				stream.write(color.getBlue());
				stream.write(color.getAlpha());
			}
		}
		return stream.toByteArray();
	}

	static class CachedSkullBlock {
		private long entityId = new SplittableRandom().nextLong(Integer.MAX_VALUE, Long.MAX_VALUE);
		private Position position;
		private NBTCompound tag;
		private String url;
		private int dataValue = 1;
		private boolean isSpawned = false;

		public CachedSkullBlock(Position position) {
			this.position = position;
		}

		public Position getPosition() {
			return position;
		}

		public boolean isCustomSkull() {
			return tag != null && url != null;
		}

		public long getEntityId() {
			return entityId;
		}

		public void spawn(SkullTilePacketListener listener) {
			isSpawned = true;

			if (Skins.getInstance().hasPcSkin(url)) {
				sendFakePlayer(listener, url, Skins.getInstance().getPcSkin(url));
			} else {
				new Thread() {
					public void run() {
						try {
							BufferedImage image = ImageIO.read(new URL(url));
							byte[] data = toData(image);
							sendFakePlayer(listener, url, data);
							Skins.getInstance().cachePcSkin(url, data);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		}

		private void sendFakePlayer(SkullTilePacketListener listener, String url, byte[] data) {
			int x = tag.getNumberTag("x").getAsInt();
			int y = tag.getNumberTag("y").getAsInt();
			int z = tag.getNumberTag("z").getAsInt();

			CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata = new CollectionsUtils.ArrayMap<>(76);

			metadata.put(PeMetaBase.SCALE, new DataWatcherObjectFloatLe(1.05f)); //Needs to be a *bit* bigger than the original skull
			metadata.put(PeMetaBase.BOUNDINGBOX_WIDTH, new DataWatcherObjectFloatLe(0.001f));
			metadata.put(PeMetaBase.BOUNDINGBOX_HEIGTH, new DataWatcherObjectFloatLe(0.001f));

			UUID uuid = UUID.randomUUID();

			float yaw = 0;
			float xOffset = 0.5F;
			float yOffset = 0.0F;
			float zOffset = 0.5F;

			if (dataValue == 1) { // on ground
				int rot = tag.getNumberTag("Rot").getAsByte();
				switch (rot) {
					case 0:
						yaw = 180F;
						break;
					case 1:
						yaw = 202.5F;
						break;
					case 2:
						yaw = 225F;
						break;
					case 3:
						yaw = 247.5F;
						break;
					case 4:
						yaw = 270F;
						break;
					case 5:
						yaw = 292.5F;
						break;
					case 6:
						yaw = 315F;
						break;
					case 7:
						yaw = 337.5F;
						break;
					case 8:
						yaw = 0F;
						break;
					case 9:
						yaw = 22.5F;
						break;
					case 10:
						yaw = 45F;
						break;
					case 11:
						yaw = 67.5F;
						break;
					case 12:
						yaw = 90F;
						break;
					case 13:
						yaw = 112.5F;
						break;
					case 14:
						yaw = 135F;
						break;
					case 15:
						yaw = 157.5F;
						break;
				}
			} else { // on walls
				yOffset = 0.25F;

				if (dataValue == 2) {
					zOffset = 0.75F;
					yaw = 180;
				}
				if (dataValue == 3) {
					zOffset = 0.25F;
					yaw = 0;
				}
				if (dataValue == 4) {
					xOffset = 0.75F;
					yaw = 90;
				}
				if (dataValue == 5) {
					xOffset = 0.25F;
					yaw = 270;
				}
			}
			SpawnPlayerPacket packet = new SpawnPlayerPacket(
					uuid,
					"",
					entityId,
					x + xOffset, y + yOffset, (float) z + zOffset, // coordinates
					0, 0, 0, // motion
					0, 0, yaw, // pitch, head yaw & yaw
					metadata
			);

			PocketCon.sendPocketPacket(listener.con, packet);
			PocketCon.sendPocketPacket(listener.con, new SkinPacket(
					uuid,
					"d5c91b67-3d30-4aee-b99f-859542f02ea9_Skull",
					"geometry.Tiles.PSPEFakeSkull",
					"Steve",
					data,
					new byte[0],
					"geometry.Tiles.PSPEFakeSkull",
					SKULL_MODEL
			));
			TileDataUpdatePacket tileDataUpdatePacket = new TileDataUpdatePacket(x, y, z, tag);
			PocketCon.sendPocketPacket(listener.con, tileDataUpdatePacket);
		}

		public void destroy(SkullTilePacketListener listener) {
			isSpawned = false;
			PocketCon.sendPocketPacket(listener.con, new EntityDestroyPacket(entityId));
		}
	}
}