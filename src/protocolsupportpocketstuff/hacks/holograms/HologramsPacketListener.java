package protocolsupportpocketstuff.hacks.holograms;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.v1_12_R1.PacketPlayOutRespawn;
import protocolsupport.api.Connection;
import protocolsupport.api.Connection.PacketListener;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.SetPosition;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.protocol.utils.datawatcher.ReadableDataWatcherObject;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectByte;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectItemStack;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectPosition;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectString;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectVarInt;
import protocolsupport.protocol.utils.i18n.I18NData;
import protocolsupport.utils.CollectionsUtils;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.packet.play.EntityDestroyPacket;
import protocolsupportpocketstuff.packet.play.PlayerMovePacket;
import protocolsupportpocketstuff.packet.play.SpawnPlayerPacket;
import protocolsupportpocketstuff.util.datawatcher.objects.DataWatcherObjectFloatLe;
import protocolsupportpocketstuff.util.datawatcher.objects.DataWatcherObjectSVarLong;
import protocolsupportpocketstuff.util.datawatcher.objects.DataWatcherObjectShortLe;

import java.util.HashMap;
import java.util.UUID;

public class HologramsPacketListener extends PacketListener {
	private Connection con;
	private HashMap<Long, CachedArmorStand> cachedArmorStands = new HashMap<Long, CachedArmorStand>();
	private static final HashMap<Integer, ReadableDataWatcherObject<?>> DATA_WATCHERS = new HashMap<Integer, ReadableDataWatcherObject<?>>();
	private static final int ARMOR_STAND_ID = 61;
	private static final double Y_OFFSET = 1.6200000047683716D;

	static {
		DATA_WATCHERS.put(0, new DataWatcherObjectByte());
		DATA_WATCHERS.put(1, new DataWatcherObjectShortLe());
		DATA_WATCHERS.put(2, new DataWatcherObjectVarInt());
		DATA_WATCHERS.put(3, new DataWatcherObjectFloatLe());
		DATA_WATCHERS.put(4, new DataWatcherObjectString());
		DATA_WATCHERS.put(5, new DataWatcherObjectItemStack());
		DATA_WATCHERS.put(6, new DataWatcherObjectPosition());
		DATA_WATCHERS.put(7, new DataWatcherObjectSVarLong());
		DATA_WATCHERS.put(8, new DataWatcherObjectPosition());
	}

	public HologramsPacketListener(Connection con) {
		this.con = con;
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		// ===[ WORLD SWITCH ]===
		if (event.getPacket() instanceof PacketPlayOutRespawn) {
			cachedArmorStands.clear();
			return;
		}
	}

	@Override
	public void onRawPacketSending(RawPacketEvent event) {
		ByteBuf data = event.getData();

		int packetId = VarNumberSerializer.readVarInt(data);

		data.readByte();
		data.readByte();

		if (packetId == PEPacketIDs.ENTITY_TELEPORT) {
			long entityId = VarNumberSerializer.readVarLong(data);

			if (!cachedArmorStands.containsKey(entityId))
				return;

			float x = data.readFloatLE();
			float y = data.readFloatLE() + (float) Y_OFFSET;
			float z = data.readFloatLE();
			int pitch = data.readByte();
			int headYaw = data.readByte();
			int yaw = data.readByte();
			boolean onGround = data.readBoolean();
			event.setData(new PlayerMovePacket(entityId, x, y, z, pitch, headYaw, yaw, SetPosition.ANIMATION_MODE_ALL, onGround).encode(con));
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

			float x = data.readFloatLE();
			float y = data.readFloatLE();
			float z = data.readFloatLE();

			data.readFloatLE(); // motx
			data.readFloatLE(); // moty
			data.readFloatLE(); // motz

			data.readFloatLE(); // pitch
			data.readFloatLE(); // yaw

			VarNumberSerializer.readVarInt(data); // attribute length, unused

			CachedArmorStand armorStand = new CachedArmorStand(x, y, z);
			cachedArmorStands.put(entityId, armorStand);

			String hologramName = retriveHologramName(data);

			if (hologramName == null)
				return;

			event.setCancelled(true);

			armorStand.nametag = hologramName;
			armorStand.setHologram(true);

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

			armorStand.setHologram(true);

			if (armorStand.isSpawned)
				return;

			// Kill current armor stand
			event.setData(new EntityDestroyPacket(entityId).encode(con));

			armorStand.nametag = hologramName;
			armorStand.setHologram(true);

			cachedArmorStands.put(entityId, armorStand);

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
		//boolean alwaysShowNametag = false;
		//boolean showBase = false;
		String nametag = null;

		int length = VarNumberSerializer.readVarInt(data);

		for (int idx = 0; length > idx; idx++) {
			int metaType = VarNumberSerializer.readVarInt(data);
			int metaKey = VarNumberSerializer.readVarInt(data);

			ReadableDataWatcherObject<?> dw = DATA_WATCHERS.get(metaKey);

			dw.readFromStream(data, con.getVersion(), I18NData.DEFAULT_LOCALE);

			if (metaType == 4) {
				nametag = (String) dw.getValue();
				hasCustomName = !nametag.isEmpty();
			}

			if (metaType == 0) {
				long peBaseFlags = (Integer) dw.getValue();

				isInvisible = ((peBaseFlags & (1 << (7 - 1))) != 0);
				showNametag = ((peBaseFlags & (1 << (15 - 1))) != 0);
				//alwaysShowNametag = ((peBaseFlags & (1 << (16 - 1))) != 0);
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

			CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata = new CollectionsUtils.ArrayMap<>(76);
			metadata.put(4, new DataWatcherObjectString(nametag));
			metadata.put(39, new DataWatcherObjectFloatLe(0.001f)); // scale
			metadata.put(54, new DataWatcherObjectFloatLe(0.001f)); // bb width
			metadata.put(55, new DataWatcherObjectFloatLe(0.001f)); // bb height

			SpawnPlayerPacket packet = new SpawnPlayerPacket(
					UUID.randomUUID(),
					nametag,
					entityId,
					x, y, z, // coordinates
					0, 0, 0, // motion
					0, 0, 0, // pitch, head yaw & yaw
					metadata
			);

			PocketCon.sendPocketPacket(listener.con, packet);
		}

		public boolean isHologram() {
			return isHologram;
		}

		public void setHologram(boolean isHologram) {
			this.isHologram = isHologram;
		}
	}
}
