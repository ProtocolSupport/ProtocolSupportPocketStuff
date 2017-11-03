package protocolsupportpocketstuff.hacks.holograms;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocolsupport.api.Connection;
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

import java.util.HashMap;
import java.util.UUID;

public class HologramsPacketListener extends Connection.PacketListener {
	private ProtocolSupportPocketStuff plugin;
	private Connection con;
	private HashMap<Long, CachedArmorStand> cachedArmorStands = new HashMap<Long, CachedArmorStand>();

	public HologramsPacketListener(ProtocolSupportPocketStuff plugin, Connection con) {
		this.plugin = plugin;
		this.con = con;
	}

	@Override
	public void onRawPacketSending(RawPacketEvent event) {
		ByteBuf data = event.getData();
		int packetId = VarNumberSerializer.readVarInt(data);

		data.readByte();
		data.readByte();

		HashMap<Integer, DataWatcherObject> dataWatchers = new HashMap<Integer, DataWatcherObject>();

		dataWatchers.put(0, new DataWatcherObjectByte());
		dataWatchers.put(1, new DataWatcherObjectShortLe());
		dataWatchers.put(2, new DataWatcherObjectVarInt());
		dataWatchers.put(3, new DataWatcherObjectFloatLe());
		dataWatchers.put(4, new DataWatcherObjectString());
		dataWatchers.put(5, new DataWatcherObjectItemStack());
		dataWatchers.put(6, new DataWatcherObjectPosition());
		dataWatchers.put(7, new DataWatcherObjectVarLong());
		dataWatchers.put(8, new DataWatcherObjectPosition());

		if (packetId == PEPacketIDs.SPAWN_ENTITY) {
			long entityId = VarNumberSerializer.readSVarLong(data);
			VarNumberSerializer.readVarLong(data); // runtime ID
			int typeId = VarNumberSerializer.readVarInt(data);

			if (typeId != 61)
				return;

			System.out.println("Entity ID: " + entityId);
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
			// omg it is an hologram :O
			armorStand.spawnHologram(entityId, hologramName, this);
			return;
		}
		if (packetId == PEPacketIDs.SET_ENTITY_DATA) {
			long entityId = VarNumberSerializer.readVarLong(data);

			System.out.println("Updating entity ID " + entityId + "...");
			if (!cachedArmorStands.containsKey(entityId))
				return;

			String hologramName = retriveHologramName(data);

			if (hologramName == null)
				return;

			// omg it is an hologram :O
			CachedArmorStand armorStand = cachedArmorStands.get(entityId);

			if (armorStand.isSpawned) {
				System.out.println("Already spawned...");
				return;
			}

			// Kill current armor stand
			ByteBuf serializer = Unpooled.buffer();
			VarNumberSerializer.writeVarInt(serializer, PEPacketIDs.ENTITY_DESTROY);
			serializer.writeByte(0);
			serializer.writeByte(0);
			VarNumberSerializer.writeVarLong(serializer, entityId);
			event.setData(serializer);
			// event.setCancelled(true);
			armorStand.spawnHologram(entityId, hologramName,this);
			cachedArmorStands.put(entityId, armorStand);
			return;
		}
		if (packetId == PEPacketIDs.ENTITY_DESTROY) {
			long entityId = VarNumberSerializer.readSVarLong(data);
			System.out.println("Killing entity " + entityId + "...");
			if (cachedArmorStands.containsKey(entityId)) {
				System.out.println("Yes, it was an armor stand...");
			}
			cachedArmorStands.remove(entityId);
			return;
		}
	}

	public String retriveHologramName(ByteBuf data) {
		HashMap<Integer, DataWatcherObject> dataWatchers = new HashMap<Integer, DataWatcherObject>();

		dataWatchers.put(0, new DataWatcherObjectByte());
		dataWatchers.put(1, new DataWatcherObjectShortLe());
		dataWatchers.put(2, new DataWatcherObjectVarInt());
		dataWatchers.put(3, new DataWatcherObjectFloatLe());
		dataWatchers.put(4, new DataWatcherObjectString());
		dataWatchers.put(5, new DataWatcherObjectItemStack());
		dataWatchers.put(6, new DataWatcherObjectPosition());
		dataWatchers.put(7, new DataWatcherObjectVarLong());
		dataWatchers.put(8, new DataWatcherObjectPosition());

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

			DataWatcherObject dw = dataWatchers.get(metaKey);

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
				showBase = ((peBaseFlags & (1 << (16 - 1))) != 0);
			}
		}

		// System.out.println("hasCustomName: " + hasCustomName);
		// System.out.println("isInvisible: " + isInvisible);
		// System.out.println("showNametag: " + showNametag);
		// System.out.println("alwaysShowNametag: " + alwaysShowNametag);
		// System.out.println("showBase: " + showBase);

		// TODO: Show base meta isn't set, bug?
		if (hasCustomName && isInvisible && showNametag && alwaysShowNametag/* && !showBase */) {
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

		public CachedArmorStand(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public void spawnHologram(long entityId, String nametag, HologramsPacketListener listener) {
			System.out.println("Spawning entity with entity ID: " + entityId);
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
			VarNumberSerializer.writeVarInt(serializer, 0);

			// NAMETAG
			/* VarNumberSerializer.writeVarInt(serializer, 4); // key
			VarNumberSerializer.writeVarInt(serializer, 4); // type
			StringSerializer.writeString(serializer, listener.con.getVersion(), nametag);

			// SCALE
			VarNumberSerializer.writeVarInt(serializer, 39); // key
			VarNumberSerializer.writeVarInt(serializer, 3); // type
			MiscSerializer.writeLFloat(serializer, 0.001F);

			// Bounding Box Height
			VarNumberSerializer.writeVarInt(serializer, 54); // key
			VarNumberSerializer.writeVarInt(serializer, 3); // type
			MiscSerializer.writeLFloat(serializer, 0.001F);

			// Bounding Box Width
			VarNumberSerializer.writeVarInt(serializer, 55); // key
			VarNumberSerializer.writeVarInt(serializer, 3); // type
			MiscSerializer.writeLFloat(serializer, 0.001F); */

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
