package protocolsupportpocketstuff.hacks.itemframes;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.ItemStackSerializer;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.PositionSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.itemstack.ItemStackRemapper;
import protocolsupport.protocol.typeremapper.pe.PEDataValues;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.i18n.I18NData;
import protocolsupport.protocol.utils.types.Position;
import protocolsupport.utils.IntTuple;
import protocolsupport.zplatform.ServerPlatform;
import protocolsupport.zplatform.impl.spigot.itemstack.SpigotItemStackWrapper;
import protocolsupport.zplatform.itemstack.ItemStackWrapper;
import protocolsupport.zplatform.itemstack.NBTTagCompoundWrapper;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.packet.play.TileDataUpdatePacket;
import protocolsupportpocketstuff.packet.play.UpdateBlockPacket;
import protocolsupportpocketstuff.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFramesPacketListener extends Connection.PacketListener {
	private Connection con;
	private HashMap<Integer, CachedItemFrame> cachedItemFrames = new HashMap<>();
	private boolean isSpawned = false;

	// Reflection stuff
	private static Field SPAWN_ENTITY_ID = null;
	private static Field ENTITY_TYPE_ID = null;
	private static Field FACING_DIRECTION = null;
	private static Field X_POSITION = null;
	private static Field Y_POSITION = null;
	private static Field Z_POSITION = null;
	private static Field METADATA_ENTITY_ID = null;
	private static Field METADATA_DATA_WATCHERS = null;
	private static Field DESTROY_ENTITY_ARRAY = null;
	private static Constructor<?> INIT_SPIGOT_ITEMSTACKWRAPPER = null;
	private static Field USE_ENTITY_ID = null;
	private static Field USE_ENTITY_ACTION = null;
	private static Field USE_ENTITY_HAND = null;

	// Constants
	private static final int ACTION_USE_ITEM = 2;
	private static final int ITEM_FRAME_ENTITY_ID = 71;
	private static final int ITEM_FRAME_BLOCK_ID = 199;

	public ItemFramesPacketListener(ProtocolSupportPocketStuff plugin, Connection con) {
		this.con = con;
	}

	static {
		try {
			// ===[ PacketPlayOutSpawnEntity ]===
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

			// ===[ PacketPlayOutEntityMetadata ]===
			METADATA_ENTITY_ID = PacketPlayOutEntityMetadata.class.getDeclaredField("a");
			METADATA_DATA_WATCHERS = PacketPlayOutEntityMetadata.class.getDeclaredField("b");

			METADATA_ENTITY_ID.setAccessible(true);
			METADATA_DATA_WATCHERS.setAccessible(true);

			// ===[ PacketPlayOutEntityDestroy ]===
			DESTROY_ENTITY_ARRAY = PacketPlayOutEntityDestroy.class.getDeclaredField("a");
			DESTROY_ENTITY_ARRAY.setAccessible(true);

			// ===[ SpigotItemStackWrapper ]===
			INIT_SPIGOT_ITEMSTACKWRAPPER = SpigotItemStackWrapper.class.getDeclaredConstructor(net.minecraft.server.v1_12_R1.ItemStack.class);
			INIT_SPIGOT_ITEMSTACKWRAPPER.setAccessible(true);

			// ===[ PacketPlayInUseEntity ]===
			USE_ENTITY_ID = PacketPlayInUseEntity.class.getDeclaredField("a");
			USE_ENTITY_ACTION = PacketPlayInUseEntity.class.getDeclaredField("action");
			USE_ENTITY_HAND = PacketPlayInUseEntity.class.getDeclaredField("d");

			USE_ENTITY_ID.setAccessible(true);
			USE_ENTITY_ACTION.setAccessible(true);
			USE_ENTITY_HAND.setAccessible(true);
		} catch (NoSuchFieldException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		super.onPacketSending(event);

		// ===[ SPAWN ]===
		if (event.getPacket() instanceof PacketPlayOutSpawnEntity) {
			PacketPlayOutSpawnEntity packet = (PacketPlayOutSpawnEntity) event.getPacket();
			// Entity ID
			int entityId = ReflectionUtils.getInt(SPAWN_ENTITY_ID, packet);
			// Entity Type ID
			int typeId = ReflectionUtils.getInt(ENTITY_TYPE_ID, packet);
			// Item Frame Facing Direction
			int facing = ReflectionUtils.getInt(FACING_DIRECTION, packet);

			if (typeId == ITEM_FRAME_ENTITY_ID) {
				int x = (int) ReflectionUtils.getDouble(X_POSITION, packet);
				int y = (int) ReflectionUtils.getDouble(Y_POSITION, packet);
				int z = (int) ReflectionUtils.getDouble(Z_POSITION, packet);

				CachedItemFrame itemFrame = new CachedItemFrame(x, y, z, facing);

				// Remove item frames in the same position as this one
				List<Integer> toRemove = new ArrayList<Integer>();

				for (Map.Entry<Integer, CachedItemFrame> entry : cachedItemFrames.entrySet()) {
					if (entry.getValue().getX() == x && entry.getValue().getY() == y && entry.getValue().getZ() == z) {
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
			return;
		}

		// ===[ DESPAWN ]===
		if (event.getPacket() instanceof PacketPlayOutEntityDestroy) {
			PacketPlayOutEntityDestroy packet = (PacketPlayOutEntityDestroy) event.getPacket();
			int[] toRemove = (int[]) ReflectionUtils.get(DESTROY_ENTITY_ARRAY, packet);

			for (int i : toRemove) {
				if (cachedItemFrames.containsKey(i)) {
					CachedItemFrame itemFrame = cachedItemFrames.get(i);
					// Despawn the item frame because the item frame was destroyed server side
					itemFrame.despawn(this);
					// If the item frame is removed, let's also remove it from our cache
					cachedItemFrames.remove(i);
				}
			}
			return;
		}

		if (event.getPacket() instanceof PacketPlayOutEntityMetadata) {
			PacketPlayOutEntityMetadata packet = (PacketPlayOutEntityMetadata) event.getPacket();
			int entityId = ReflectionUtils.getInt(METADATA_ENTITY_ID, packet);

			if (cachedItemFrames.containsKey(entityId)) {
				@SuppressWarnings("unchecked")
				List<DataWatcher.Item<?>> dataWatchers = (List<DataWatcher.Item<?>>) ReflectionUtils.get(METADATA_DATA_WATCHERS, packet);

				CachedItemFrame itemFrame = cachedItemFrames.get(entityId);

				itemFrame.updateMetadata(this, dataWatchers);
			}
			return;
		}
	}

	@Override
	public void onRawPacketReceiving(RawPacketEvent event) {
		ByteBuf data = event.getData();
		
		int packetId = VarNumberSerializer.readVarInt(data);
		data.readByte();
		data.readByte();
		
		Position position = new Position(0, 0, 0);

		if (packetId == PEPacketIDs.PLAYER_MOVE) {
			if (isSpawned)
				return;

			isSpawned = true;

			// Workaround for item frames on login, sending "spawn item frame" packets on login doesn't work
			// so we are going to spawn them when the player moves
			// This isn't required for when the player teleports between worlds.
			for (CachedItemFrame itemFrame : cachedItemFrames.values()) {
				itemFrame.spawn(this);
			}
			return;
		}
		if (packetId == PEPacketIDs.PLAYER_ACTION) { // Used for when the player tries to hit a item frame when it doesn't have any item inside of it
			VarNumberSerializer.readSVarLong(data); // entity ID
			int action = VarNumberSerializer.readSVarInt(data);

			if (action != 0) // We only care about "start break" packets
				return;

			PositionSerializer.readPEPosition(data);

			Map.Entry<Integer, CachedItemFrame> entry = getItemFrameAt(position.getX(), position.getY(), position.getZ());

			if (entry == null)
				return;

			sendInteractEntityPacket(entry.getKey(), PacketPlayInUseEntity.EnumEntityUseAction.ATTACK);
			return;
		}
		if (packetId == 71) { // Item Frame Drop Item
			PositionSerializer.readPEPositionTo(data, position);

			Map.Entry<Integer, CachedItemFrame> entry = getItemFrameAt(position.getX(), position.getY(), position.getZ());

			if (entry == null)
				return;

			sendInteractEntityPacket(entry.getKey(), PacketPlayInUseEntity.EnumEntityUseAction.ATTACK);
		}
		if (packetId == PEPacketIDs.INVENTORY_TRANSACTION) { // GodPacket, now on ProtocolSupportPocketStuff!
			int actionId = VarNumberSerializer.readVarInt(data);

			if (actionId != ACTION_USE_ITEM)
				return;

			int count = VarNumberSerializer.readVarInt(data);
			for (int i = 0; i < count; i++) {
				int sourceId = VarNumberSerializer.readVarInt(data); // probably is 0
				if (sourceId != 0)
					return; // We only handle transactions with ID 0
				VarNumberSerializer.readSVarInt(data); // inventory ID
				VarNumberSerializer.readVarInt(data); // slot
				ItemStackSerializer.readItemStack(data, con.getVersion(), I18NData.DEFAULT_LOCALE, true); // old item
				ItemStackSerializer.readItemStack(data, con.getVersion(), I18NData.DEFAULT_LOCALE, true); // new item
			}

			int subTypeId = VarNumberSerializer.readVarInt(data);

			if (subTypeId != 0)
				return;

			PositionSerializer.readPEPositionTo(data, position);
			VarNumberSerializer.readSVarInt(data); // face
			VarNumberSerializer.readSVarInt(data); // slot
			ItemStackSerializer.readItemStack(data, con.getVersion(), I18NData.DEFAULT_LOCALE, true); // itemstack
			data.readFloatLE(); // fromX
			data.readFloatLE(); // fromY
			data.readFloatLE(); // fromZ
			data.readFloatLE(); // cX
			data.readFloatLE(); // cY
			data.readFloatLE(); // cZ

			int itemFrameX = position.getX();
			int itemFrameY = position.getY();
			int itemFrameZ = position.getZ();

			Map.Entry<Integer, CachedItemFrame> entry = getItemFrameAt(itemFrameX, itemFrameY, itemFrameZ);

			if (entry == null)
				return;

			CachedItemFrame itemFrame = entry.getValue();
			itemFrame.spawn(this); // Respawn the item frame

			sendInteractEntityPacket(entry.getKey(), PacketPlayInUseEntity.EnumEntityUseAction.INTERACT);
			return;
		}
	}

	@Override
	public void onRawPacketSending(RawPacketEvent event) {
		super.onRawPacketSending(event);
		ByteBuf data = event.getData();
		int packetId = VarNumberSerializer.readVarInt(data);

		data.readByte();
		data.readByte();

		if (packetId == PEPacketIDs.CHANGE_DIMENSION) {
			// Clear cached item frames on dimension switch
			cachedItemFrames.clear();
			return;
		}
		if (packetId == PEPacketIDs.UPDATE_BLOCK) {
			Position position = new Position(0, 0, 0); 
			PositionSerializer.readPEPositionTo(data, position);
			int id = VarNumberSerializer.readVarInt(data);

			Map.Entry<Integer, CachedItemFrame> entry = getItemFrameAt(position.getX(), position.getY(), position.getZ());

			if (entry == null)
				return;

			if (id != ITEM_FRAME_BLOCK_ID)
				event.setCancelled(true);
			return;
		}
		if (packetId == PEPacketIDs.CHUNK_DATA) {
			int chunkX = VarNumberSerializer.readSVarInt(event.getData());
			int chunkZ = VarNumberSerializer.readSVarInt(event.getData());

			for (Map.Entry<Integer, CachedItemFrame> entry : cachedItemFrames.entrySet()) {
				int itemChunkX = entry.getValue().getX() >> 4;
				int itemChunkZ = entry.getValue().getZ() >> 4;

				// When sending an chunk data to the client, let's listen if there is an item frame inside the chunk
				// If there is, spawn the Item Frame. This does NOT check the Y chunk coordinate because that would
				// require too much code, but it seems there isn't any side effects with this "hacky" workaround
				if (chunkX == itemChunkX && chunkZ == itemChunkZ) {
					entry.getValue().spawn(this);
				}
			}
			return;
		}
	}

	public Map.Entry<Integer, CachedItemFrame> getItemFrameAt(int x, int y, int z) {
		for (Map.Entry<Integer, CachedItemFrame> entry : cachedItemFrames.entrySet()) {
			CachedItemFrame itemFrame = entry.getValue();
			if (itemFrame.x == x && itemFrame.y == y && itemFrame.z == z) {
				return entry;
			}
		}
		return null;
	}

	public void sendInteractEntityPacket(int entityId, PacketPlayInUseEntity.EnumEntityUseAction useAction) {
		PacketPlayInUseEntity packet = new PacketPlayInUseEntity();
		ReflectionUtils.set(USE_ENTITY_ID, packet, entityId);
		ReflectionUtils.set(USE_ENTITY_ACTION, packet, useAction);
		ReflectionUtils.set(USE_ENTITY_HAND, packet, EnumHand.MAIN_HAND);
		con.receivePacket(packet);
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
				spawnTag.setInt("x", getX());
				spawnTag.setInt("y", getY());
				spawnTag.setInt("z", getZ());
				spawnTag.setByte("ItemRotation", 0);
				spawnTag.setFloat("ItemDropChance", 1);
				spawnTag.setString("id", "ItemFrame");
			}
			return spawnTag;
		}

		public int getPEFacing() {
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

			return peFacing;
		}

		public void spawn(ItemFramesPacketListener listener) {
			// First we change the block type...
			// Item Frame block ID is 199
			UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket(getX(), getY(), getZ(), ItemFramesPacketListener.ITEM_FRAME_BLOCK_ID, getPEFacing());

			PocketCon.sendPocketPacket(listener.con, updateBlockPacket);

			// Now we are going to set the item frame NBT tag, this is required so the item frame is displayed
			NBTTagCompoundWrapper tag = getSpawnTag();
			TileDataUpdatePacket tileDataUpdate = new TileDataUpdatePacket(getX(), getY(), getZ(), tag);
			PocketCon.sendPocketPacket(listener.con, tileDataUpdate);
		}

		public void despawn(ItemFramesPacketListener listener) {
			// We are going to set it to air because... well, there isn't too many other choices I guess *shrugs*
			UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket(getX(), getY(), getZ(), 0, 0);
			PocketCon.sendPocketPacket(listener.con, updateBlockPacket);
		}

		public void updateMetadata(ItemFramesPacketListener listener, List<DataWatcher.Item<?>> dataWatchers) {
			NBTTagCompoundWrapper tag = getSpawnTag();

			for (DataWatcher.Item<?> dw : dataWatchers) {
				// Obfuscation Helper: dw.a().a() = DataWatcher ID
				switch (dw.a().a()) {
					case 6: // Set item inside Item Frame
						try {
							net.minecraft.server.v1_12_R1.ItemStack item = (net.minecraft.server.v1_12_R1.ItemStack) dw.b();
							// Yes, Reflection to initialize an ItemStackWrapper
							// This is only required so we don't end up with moar reflection just to get all the required stuff from the item (like NBT tags)
							ItemStackWrapper wrapper = (ItemStackWrapper) ItemFramesPacketListener.INIT_SPIGOT_ITEMSTACKWRAPPER.newInstance(item);

							// Remap the item to PE
							// First we are going to remap it using remapToClient, to fix stuff like maps, enchantments, spawn eggs, etc
							wrapper = ItemStackRemapper.remapToClient(listener.con.getVersion(), I18NData.DEFAULT_LOCALE, wrapper.getTypeId(), wrapper);
							NBTTagCompoundWrapper itemFrameTag = ServerPlatform.get().getWrapperFactory().createEmptyNBTCompound();

							// Then we are going to remap the item ID/value
							IntTuple itemAndData = PEDataValues.ITEM_ID.getRemap(wrapper.getTypeId(), wrapper.getData());
							int id = wrapper.getTypeId();
							int data = wrapper.getData();

							if (itemAndData != null) {
								id = itemAndData.getI1();

								if (itemAndData.getI2() != -1) {
									data = itemAndData.getI2();
								}
							}

							if (id != 0) { // If it isn't air...
								// Create our own:tm: NBT tag for the Item Frame with the item stuff
								itemFrameTag.setShort("id", id);
								itemFrameTag.setByte("Count", wrapper.getAmount());
								itemFrameTag.setShort("Damage", data);
								NBTTagCompoundWrapper itemTag = wrapper.getTag();
								if (itemTag != null && !itemTag.isNull()) {
									itemFrameTag.setCompound("tag", itemTag);
								}
								tag.setCompound("Item", itemFrameTag);
							} else { // If it is, remove the old tag
								tag.remove("Item");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					case 7: // Set item rotation inside Item Frame
						int itemRotation = (Integer) dw.b();
						tag.setByte("ItemRotation", itemRotation);
						break;
					default:
						break;
				}
			}
			TileDataUpdatePacket tileDataUpdate = new TileDataUpdatePacket(getX(), getY(), getZ(), tag);
			PocketCon.sendPocketPacket(listener.con, tileDataUpdate);
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getZ() {
			return z;
		}
	}
}
