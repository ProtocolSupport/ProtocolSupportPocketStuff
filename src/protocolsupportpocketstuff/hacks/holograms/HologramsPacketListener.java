package protocolsupportpocketstuff.hacks.holograms;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.Position;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectByte;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectFloatLe;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectItemStack;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectPosition;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectShortLe;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectString;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectVarInt;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectVarLong;
import protocolsupport.protocol.utils.i18n.I18NData;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.packet.play.EntityDestroyPacket;
import protocolsupportpocketstuff.packet.play.PlayerMovePacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramsPacketListener extends Connection.PacketListener {
	private ProtocolSupportPocketStuff plugin;
	private Connection con;
	private HashMap<Long, CachedArmorStand> cachedArmorStands = new HashMap<Long, CachedArmorStand>();
	private static final HashMap<Integer, DataWatcherObject> DATA_WATCHERS = new HashMap<Integer, DataWatcherObject>();
	private static final int ARMOR_STAND_ID = 61;
	private static final double Y_OFFSET = 1.6200000047683716D;
	private boolean isSpawned = false;

	static {
		DATA_WATCHERS.put(0, new DataWatcherObjectByte());
		DATA_WATCHERS.put(1, new DataWatcherObjectShortLe());
		DATA_WATCHERS.put(2, new DataWatcherObjectVarInt());
		DATA_WATCHERS.put(3, new DataWatcherObjectFloatLe());
		DATA_WATCHERS.put(4, new DataWatcherObjectString());
		DATA_WATCHERS.put(5, new DataWatcherObjectItemStack());
		DATA_WATCHERS.put(6, new DataWatcherObjectPosition());
		DATA_WATCHERS.put(7, new DataWatcherObjectVarLong());
		DATA_WATCHERS.put(8, new DataWatcherObjectPosition());
	}

	public HologramsPacketListener(ProtocolSupportPocketStuff plugin, Connection con) {
		this.plugin = plugin;
		this.con = con;
	}

	@Override
	public void onRawPacketReceiving(RawPacketEvent event) {
		super.onRawPacketReceiving(event);

		ByteBuf data = event.getData();
		int packetId = VarNumberSerializer.readVarInt(data);

		if (packetId == PEPacketIDs.PLAYER_MOVE) {
			if (isSpawned)
				return;

			isSpawned = true;

			// Workaround for holograms on login, sending "spawn hologram" packets on login doesn't work
			// so we are going to spawn them when the player moves
			// This isn't required for when the player teleports between worlds.
			for (Map.Entry<Long, CachedArmorStand> armorStand : cachedArmorStands.entrySet()) {
				if (armorStand.getValue().isHologram) {
					armorStand.getValue().spawnHologram(armorStand.getKey(), this);
				}
			}
			return;
		}
	}

	@Override
	public void onRawPacketSending(RawPacketEvent event) {
		ByteBuf data = event.getData();

		int packetId = VarNumberSerializer.readVarInt(data);

		data.readByte();
		data.readByte();

		if (packetId == PEPacketIDs.CHANGE_DIMENSION) {
			// Clear cached holograms on dimension switch
			cachedArmorStands.clear();
			return;
		}
		if (packetId == PEPacketIDs.ENTITY_TELEPORT) {
			long entityId = VarNumberSerializer.readVarLong(data);

			if (!cachedArmorStands.containsKey(entityId))
				return;

			float x = MiscSerializer.readLFloat(data);
			float y = MiscSerializer.readLFloat(data) + (float) Y_OFFSET;
			float z = MiscSerializer.readLFloat(data);
			int pitch = data.readByte();
			int headYaw = data.readByte();
			int yaw = data.readByte();
			boolean onGround = data.readBoolean();
			event.setData(new PlayerMovePacket(entityId, x, y, z, pitch, headYaw, yaw, Position.ANIMATION_MODE_ALL, onGround).encode(con));
			return;
		}
		if (packetId == PEPacketIDs.SPAWN_ENTITY) {
			long entityId = VarNumberSerializer.readSVarLong(data);
			VarNumberSerializer.readVarLong(data); // runtime ID
			int typeId = VarNumberSerializer.readVarInt(data);

			if (cachedArmorStands.containsKey(entityId))
				return;

			if (typeId != ARMOR_STAND_ID)
				return;

			float x = MiscSerializer.readLFloat(data);
			float y = MiscSerializer.readLFloat(data);
			float z = MiscSerializer.readLFloat(data);

			MiscSerializer.readLFloat(data); // motx
			MiscSerializer.readLFloat(data); // moty
			MiscSerializer.readLFloat(data); // motz

			MiscSerializer.readLFloat(data); // pitch
			MiscSerializer.readLFloat(data); // yaw

			VarNumberSerializer.readVarInt(data); // attribute length, unused

			CachedArmorStand armorStand = new CachedArmorStand(x, y, z);
			cachedArmorStands.put(entityId, armorStand);

			String hologramName = retriveHologramName(data);

			if (hologramName == null)
				return;

			event.setCancelled(true);

			armorStand.nametag = hologramName;
			armorStand.isHologram = true;

			if (!isSpawned)
				return;

			// omg it is an hologram :O
			armorStand.spawnHologram(entityId, this);
			return;
		}
		if (packetId == PEPacketIDs.SET_ENTITY_DATA) {
			long entityId = VarNumberSerializer.readVarLong(data);

			if (!cachedArmorStands.containsKey(entityId))
				return;

			String hologramName = retriveHologramName(data);

			if (hologramName == null)
				return;

			// omg it is an hologram :O
			CachedArmorStand armorStand = cachedArmorStands.get(entityId);

			armorStand.isHologram = true;

			if (armorStand.isSpawned)
				return;

			// Kill current armor stand
			event.setData(new EntityDestroyPacket(entityId).encode(con));

			armorStand.nametag = hologramName;
			armorStand.isHologram = true;

			cachedArmorStands.put(entityId, armorStand);

			if (!isSpawned)
				return;

			armorStand.spawnHologram(entityId, this);
			return;
		}
		if (packetId == PEPacketIDs.ENTITY_DESTROY) {
			long entityId = VarNumberSerializer.readSVarLong(data);
			cachedArmorStands.remove(entityId);
			return;
		}
	}

	public String retriveHologramName(ByteBuf data) {
		boolean hasCustomName = false;
		boolean isInvisible = false;
		boolean showNametag = false;
		boolean alwaysShowNametag = false;
		boolean showBase = false;
		String nametag = null;

		int length = VarNumberSerializer.readVarInt(data);

		for (int idx = 0; length > idx; idx++) {
			int metaType = VarNumberSerializer.readVarInt(data);
			int metaKey = VarNumberSerializer.readVarInt(data);

			DataWatcherObject dw = DATA_WATCHERS.get(metaKey);

			dw.readFromStream(data, con.getVersion(), I18NData.DEFAULT_LOCALE);

			if (metaType == 4) {
				nametag = (String) dw.getValue();
				hasCustomName = !nametag.isEmpty();
			}

			if (metaType == 0) {
				long peBaseFlags = (long) dw.getValue();

				isInvisible = ((peBaseFlags & (1 << (7 - 1))) != 0);
				showNametag = ((peBaseFlags & (1 << (15 - 1))) != 0);
				alwaysShowNametag = ((peBaseFlags & (1 << (16 - 1))) != 0);
				// showBase = ((peBaseFlags & (1 << (16 - 1))) != 0);
			}
		}

		// TODO: Show base meta isn't set, bug?
		// TODO: alwaysShowNametag seems to be failing on first join, why?
		if (hasCustomName && isInvisible && showNametag /* && alwaysShowNametag *//* && !showBase */) {
			return nametag;
		} else {
			return null;
		}
	}

	static class CachedArmorStand {
		private float x;
		private float y;
		private float z;
		private boolean isSpawned = false;
		private String nametag;
		private boolean isHologram = false;

		public CachedArmorStand(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public void spawnHologram(long entityId, HologramsPacketListener listener) {
			isSpawned = true;
			ByteBuf serializer = Unpooled.buffer();
			VarNumberSerializer.writeVarInt(serializer, PEPacketIDs.SPAWN_PLAYER);
			serializer.writeByte(0);
			serializer.writeByte(0);
			MiscSerializer.writeUUID(serializer, UUID.randomUUID()); // random UUID boi
			StringSerializer.writeString(serializer, listener.con.getVersion(), nametag);
			VarNumberSerializer.writeSVarLong(serializer, entityId); // entity ID
			VarNumberSerializer.writeVarLong(serializer, entityId); // runtime ID
			MiscSerializer.writeLFloat(serializer, x);
			MiscSerializer.writeLFloat(serializer, y);
			MiscSerializer.writeLFloat(serializer, z);
			MiscSerializer.writeLFloat(serializer, 0); // motx
			MiscSerializer.writeLFloat(serializer, 0); // moty
			MiscSerializer.writeLFloat(serializer, 0); // motz
			MiscSerializer.writeLFloat(serializer, 0); // pitch
			MiscSerializer.writeLFloat(serializer, 0); // head yaw
			MiscSerializer.writeLFloat(serializer, 0); // yaw
			VarNumberSerializer.writeSVarInt(serializer, 0); // held item stack
			// Metadata:tm:
			// Write the length first...
			VarNumberSerializer.writeVarInt(serializer, 4);

			// NAMETAG
			VarNumberSerializer.writeVarInt(serializer, 4); // key
			VarNumberSerializer.writeVarInt(serializer, 4); // type
			StringSerializer.writeString(serializer, listener.con.getVersion(), nametag);

			// SCALE
			VarNumberSerializer.writeVarInt(serializer, 39); // key
			VarNumberSerializer.writeVarInt(serializer, 3); // type
			MiscSerializer.writeLFloat(serializer, 0.001F);

			// BOUNDING BOX WIDTH
			VarNumberSerializer.writeVarInt(serializer, 54); // key
			VarNumberSerializer.writeVarInt(serializer, 3); // type
			MiscSerializer.writeLFloat(serializer, 0.001F);

			// BOUNDING BOX HEIGHT
			VarNumberSerializer.writeVarInt(serializer, 55); // key
			VarNumberSerializer.writeVarInt(serializer, 3); // type
			MiscSerializer.writeLFloat(serializer, 0.001F);

			//adventure settings
			VarNumberSerializer.writeVarInt(serializer, 0);
			VarNumberSerializer.writeVarInt(serializer, 0);
			VarNumberSerializer.writeVarInt(serializer, 0);
			VarNumberSerializer.writeVarInt(serializer, 0);
			VarNumberSerializer.writeVarInt(serializer, 0);

			serializer.writeLongLE(0); //?

			//entity links
			//TODO: Implement entity links
			VarNumberSerializer.writeSVarInt(serializer, 0);
			listener.con.sendRawPacket(MiscSerializer.readAllBytes(serializer));
		}
	}
}
