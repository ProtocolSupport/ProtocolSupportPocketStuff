package protocolsupportpocketstuff.hacks.itemframes;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.itemstack.ItemStackRemapper;
import protocolsupport.protocol.typeremapper.pe.PEDataValues;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.i18n.I18NData;
import protocolsupport.utils.IntTuple;
import protocolsupport.zplatform.ServerPlatform;
import protocolsupport.zplatform.impl.spigot.itemstack.SpigotItemStackWrapper;
import protocolsupport.zplatform.itemstack.ItemStackWrapper;
import protocolsupport.zplatform.itemstack.NBTTagCompoundWrapper;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.packet.TileDataUpdatePacket;
import protocolsupportpocketstuff.packet.play.UpdateBlockPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFramesPacketListener extends Connection.PacketListener {
	private ProtocolSupportPocketStuff plugin;
	private Connection con;
	private HashMap<Integer, CachedItemFrame> cachedItemFrames = new HashMap<>();
	private static Field SPAWN_ENTITY_ID = null;
	private static Field ENTITY_TYPE_ID = null;
	private static Field FACING_DIRECTION = null;
	private static Field X_POSITION = null;
	private static Field Y_POSITION = null;
	private static Field Z_POSITION = null;
	private static Field METADATA_ENTITY_ID = null;
	private static Field METADATA_DATA_WATCHERS = null;
	private static Field DESTROY_ENTITY_ARRAY = null;

	public ItemFramesPacketListener(ProtocolSupportPocketStuff plugin, Connection con) {
		this.plugin = plugin;
		this.con = con;
	}

	static {
		try {
			SPAWN_ENTITY_ID = PacketPlayOutSpawnEntity.class.getDeclaredField("a");
			ENTITY_TYPE_ID = PacketPlayOutSpawnEntity.class.getDeclaredField("k");
			FACING_DIRECTION = PacketPlayOutSpawnEntity.class.getDeclaredField("l");
			X_POSITION = PacketPlayOutSpawnEntity.class.getDeclaredField("c");
			Y_POSITION = PacketPlayOutSpawnEntity.class.getDeclaredField("d");
			Z_POSITION = PacketPlayOutSpawnEntity.class.getDeclaredField("e");

			SPAWN_ENTITY_ID.setAccessible(true);
			ENTITY_TYPE_ID.setAccessible(true);
			FACING_DIRECTION.setAccessible(true);
			X_POSITION.setAccessible(true);
			Y_POSITION.setAccessible(true);
			Z_POSITION.setAccessible(true);

			METADATA_ENTITY_ID = PacketPlayOutEntityMetadata.class.getDeclaredField("a");
			METADATA_DATA_WATCHERS = PacketPlayOutEntityMetadata.class.getDeclaredField("b");

			METADATA_ENTITY_ID.setAccessible(true);
			METADATA_DATA_WATCHERS.setAccessible(true);

			DESTROY_ENTITY_ARRAY = PacketPlayOutEntityDestroy.class.getDeclaredField("a");
			DESTROY_ENTITY_ARRAY.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		super.onPacketSending(event);

		// ===[ SPAWN ]===
		if (event.getPacket() instanceof PacketPlayOutSpawnEntity) {
			PacketPlayOutSpawnEntity packet = (PacketPlayOutSpawnEntity) event.getPacket();
			try {
				// Entity ID
				int entityId = getInt(SPAWN_ENTITY_ID, packet);
				// Entity Type ID
				int typeId = getInt(ENTITY_TYPE_ID, packet);
				// Item Frame Facing Direction
				int facing = getInt(FACING_DIRECTION, packet);

				if (typeId == 71) { // 71 = Item Frame's entity ID
					int x = (int) getDouble(X_POSITION, packet);
					int y = (int) getDouble(Y_POSITION, packet);
					int z = (int) getDouble(Z_POSITION, packet);

					CachedItemFrame itemFrame = new CachedItemFrame(x, y, z, facing);

					// Remove item frames in the same position as this one
					List<Integer> toRemove = new ArrayList<Integer>();

					for (Map.Entry<Integer, CachedItemFrame> entry : cachedItemFrames.entrySet()) {
						if (entry.getValue().x == x && entry.getValue().y == y && entry.getValue().z == z) {
							toRemove.add(entry.getKey());
						}
					}

					for (Integer eid : toRemove) {
						cachedItemFrames.remove(eid);
					}

					// Spawn the item frame!
					itemFrame.spawn(this);
					cachedItemFrames.put(entityId, itemFrame);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		// ===[ DESPAWN ]===
		if (event.getPacket() instanceof PacketPlayOutEntityDestroy) {
			PacketPlayOutEntityDestroy packet = (PacketPlayOutEntityDestroy) event.getPacket();
			try {
				int[] toRemove = (int[]) get(DESTROY_ENTITY_ARRAY, packet);

				for (int i : toRemove) {
					if (cachedItemFrames.containsKey(i)) {
						CachedItemFrame itemFrame = cachedItemFrames.get(i);
						// Despawn the item frame because the item frame was destroyed server side
						itemFrame.despawn(this);
						// If the item frame is removed, let's also remove it from our cache
						cachedItemFrames.remove(i);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if (event.getPacket() instanceof PacketPlayOutEntityMetadata) {
			PacketPlayOutEntityMetadata packet = (PacketPlayOutEntityMetadata) event.getPacket();
			try {
				int entityId = getInt(METADATA_ENTITY_ID, packet);

				if (cachedItemFrames.containsKey(entityId)) {
					List<DataWatcher.Item<?>> dataWatchers = (List<DataWatcher.Item<?>>) get(METADATA_DATA_WATCHERS, packet);

					CachedItemFrame itemFrame = cachedItemFrames.get(entityId);

					itemFrame.updateMetadata(this, dataWatchers);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

	@Override
	public void onRawPacketSending(RawPacketEvent event) {
		super.onRawPacketSending(event);
		ByteBuf data = event.getData();
		int packetId = VarNumberSerializer.readVarInt(data);

		if (packetId == PEPacketIDs.CHANGE_DIMENSION) {
			// Clear cached item frames on dimension switch
			cachedItemFrames.clear();
			return;
		}
		if (packetId == PEPacketIDs.CHUNK_DATA) {
			data.readByte();
			data.readByte();

			int chunkX = VarNumberSerializer.readSVarInt(event.getData());
			int chunkZ = VarNumberSerializer.readSVarInt(event.getData());

			for (Map.Entry<Integer, CachedItemFrame> entry : cachedItemFrames.entrySet()) {
				int itemChunkX = entry.getValue().x >> 4;
				int itemChunkZ = entry.getValue().z >> 4;

				if (chunkX == itemChunkX && chunkZ == itemChunkZ) {
					entry.getValue().spawn(this);
				}
			}
			return;
		}
	}

	public static Object get(Field field, Object source) {
		try {
			return field.get(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error while getting field \"" + field.getName() + "\" from " + source + "!");
	}

	public static int getInt(Field field, Object source) {
		try {
			return field.getInt(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error while getting field \"" + field.getName() + "\" from " + source + "!");
	}

	public static double getDouble(Field field, Object source) {
		try {
			return field.getDouble(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error while getting field \"" + field.getName() + "\" from " + source + "!");
	}

	static class CachedItemFrame {
		private int x;
		private int y;
		private int z;
		private int facing;
		private NBTTagCompoundWrapper spawnTag;

		public CachedItemFrame(int x, int y, int z, int facing) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.facing = facing;
		}

		public NBTTagCompoundWrapper getSpawnTag() {
			if (spawnTag == null) {
				spawnTag = ServerPlatform.get().getWrapperFactory().createEmptyNBTCompound();
				spawnTag.setInt("x", x);
				spawnTag.setInt("y", y);
				spawnTag.setInt("z", z);
				spawnTag.setByte("ItemRotation", 0);
				spawnTag.setFloat("ItemDropChance", 1);
				spawnTag.setString("id", "ItemFrame");
			}
			return spawnTag;
		}

		public void spawn(ItemFramesPacketListener listener) {
			int peFacing = 0;

			switch (facing) {
				case 3:
					peFacing = 0;
					break;
				case 0:
					peFacing = 2;
					break;
				case 2:
					peFacing = 3;
					break;
				case 1:
					peFacing = 1;
					break;
				default:
					break;
			}

			// First we change the block type...
			// Item Frame block ID is 199
			UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket(x, y, z, 199, peFacing);

			PocketCon.sendPocketPacket(listener.con, updateBlockPacket);

			// Now we are going to set the item frame NBT tag, this is required so the item frame is displayed
			NBTTagCompoundWrapper tag = getSpawnTag();
			TileDataUpdatePacket tileDataUpdate = new TileDataUpdatePacket(x, y, z, tag);
			PocketCon.sendPocketPacket(listener.con, tileDataUpdate);
		}

		public void despawn(ItemFramesPacketListener listener) {
			// We are going to set it to air because... well, there isn't too many other choices I guess *shrugs*
			UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket((int) x, (int) y, (int) z, 0, 0);
			PocketCon.sendPocketPacket(listener.con, updateBlockPacket);
		}

		public void updateMetadata(ItemFramesPacketListener listener, List<DataWatcher.Item<?>> dataWatchers) {
			NBTTagCompoundWrapper tag = getSpawnTag();
			System.out.println("Updating metadata...");

			for (DataWatcher.Item dw : dataWatchers) {
				// Obfuscation Helper: dw.a().a() = DataWatcher ID
				switch (dw.a().a()) {
					case 6:
						try {
							net.minecraft.server.v1_12_R1.ItemStack item = (net.minecraft.server.v1_12_R1.ItemStack) dw.b();
							// Yes, Reflection to initialize an ItemStackWrapper
							// This is only required so we don't end up with moar reflection just to get all the required stuff from the item (like NBT tags)
							Constructor constructor = SpigotItemStackWrapper.class.getDeclaredConstructor(net.minecraft.server.v1_12_R1.ItemStack.class);
							constructor.setAccessible(true);
							ItemStackWrapper wrapper = (ItemStackWrapper) constructor.newInstance(item);
							// Remap the item to PE
							wrapper = ItemStackRemapper.remapToClient(listener.con.getVersion(), I18NData.DEFAULT_LOCALE, wrapper.getTypeId(), wrapper);
							NBTTagCompoundWrapper itemFrameTag = ServerPlatform.get().getWrapperFactory().createEmptyNBTCompound();

							// Remap the item type ID/data
							IntTuple itemAndData = PEDataValues.ITEM_ID.getRemap(wrapper.getTypeId(), wrapper.getData());
							int id = wrapper.getTypeId();
							int data = wrapper.getData();

							if (itemAndData != null) {
								id = itemAndData.getI1();

								if (itemAndData.getI2() != -1) {
									data = itemAndData.getI2();
								}
							}

							// Create our own:tm: NBT tag for the Item Frame with the item stuff
							itemFrameTag.setShort("id", id);
							itemFrameTag.setByte("Count", wrapper.getAmount());
							itemFrameTag.setShort("Damage", data);
							NBTTagCompoundWrapper itemTag = wrapper.getTag();
							if (itemTag != null && !itemTag.isNull()) {
								itemFrameTag.setCompound("tag", itemTag);
							}
							tag.setCompound("Item", itemFrameTag);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case 7:
						int itemRotation = (int) dw.b();
						tag.setByte("ItemRotation", itemRotation);
						break;
					default:
						break;
				}
			}

			TileDataUpdatePacket tileDataUpdate = new TileDataUpdatePacket(x, y, z, tag);

			PocketCon.sendPocketPacket(listener.con, tileDataUpdate);
		}
	}
}
