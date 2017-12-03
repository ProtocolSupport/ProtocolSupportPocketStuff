package protocolsupportpocketstuff.hacks.bossbars;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutBoss;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectFloatLe;
import protocolsupport.protocol.utils.datawatcher.objects.DataWatcherObjectString;
import protocolsupport.utils.CollectionsUtils;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.packet.play.BossEventPacket;
import protocolsupportpocketstuff.packet.play.EntityDataPacket;
import protocolsupportpocketstuff.packet.play.EntityDestroyPacket;
import protocolsupportpocketstuff.packet.play.SetAttributesPacket;
import protocolsupportpocketstuff.packet.play.SpawnEntityPacket;
import protocolsupportpocketstuff.util.ReflectionUtils;
import protocolsupportpocketstuff.util.StuffUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class BossBarPacketListener extends Connection.PacketListener {
	
	private Connection con;
	private HashMap<Long, CachedBossBar> cachedBossBars = new HashMap<>();
	private boolean isSpawned = false;

	// Reflection stuff
	private static Field BOSS_UUID;
	private static Field BOSS_ACTION;
	private static Field BOSS_TITLE;
	private static Field BOSS_HEALTH;

	// Constants
	private static final int BOSS_BAR_ENTITY_ID = 12; // Pig

	static {
		try {
			BOSS_UUID = PacketPlayOutBoss.class.getDeclaredField("a");
			BOSS_ACTION = PacketPlayOutBoss.class.getDeclaredField("b");
			BOSS_TITLE = PacketPlayOutBoss.class.getDeclaredField("c");
			BOSS_HEALTH = PacketPlayOutBoss.class.getDeclaredField("d");

			BOSS_UUID.setAccessible(true);
			BOSS_ACTION.setAccessible(true);
			BOSS_TITLE.setAccessible(true);
			BOSS_HEALTH.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public BossBarPacketListener(Connection con) {
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
			for (CachedBossBar cachedBossBar : cachedBossBars.values()) {
				cachedBossBar.spawn(this);
			}
			return;
		}
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		super.onPacketSending(event);

		if (!(event.getPacket() instanceof PacketPlayOutBoss))
			return;

		PacketPlayOutBoss packet = (PacketPlayOutBoss) event.getPacket();

		UUID uuid = (UUID) ReflectionUtils.get(BOSS_UUID, packet);
		long unique = uuid.getMostSignificantBits() & Long.MAX_VALUE;

		PacketPlayOutBoss.Action action = (PacketPlayOutBoss.Action) ReflectionUtils.get(BOSS_ACTION, packet);

		if (action == PacketPlayOutBoss.Action.ADD) {
			IChatBaseComponent title = (IChatBaseComponent) ReflectionUtils.get(BOSS_TITLE, packet);
			float percentage = ReflectionUtils.getFloat(BOSS_HEALTH, packet);

			CachedBossBar bossBar = new CachedBossBar(unique, StuffUtils.toLegacy(title), percentage);

			cachedBossBars.put(unique, bossBar);

			if (!isSpawned)
				return;

			bossBar.spawn(this);
			return;
		}
		if (action == PacketPlayOutBoss.Action.REMOVE) {
			if (!cachedBossBars.containsKey(unique))
				return;

			CachedBossBar bossBar = cachedBossBars.get(unique);
			cachedBossBars.remove(unique);
			bossBar.despawn(this);
			return;
		}
		if (action == PacketPlayOutBoss.Action.UPDATE_NAME) {
			if (!cachedBossBars.containsKey(unique))
				return;

			IChatBaseComponent title = (IChatBaseComponent) ReflectionUtils.get(BOSS_TITLE, packet);

			CachedBossBar bossBar = cachedBossBars.get(unique);
			bossBar.title = StuffUtils.toLegacy(title);

			if (!isSpawned)
				return;

			bossBar.updateMetadata(this);
			return;
		}
		if (action == PacketPlayOutBoss.Action.UPDATE_PCT) {
			if (!cachedBossBars.containsKey(unique))
				return;

			CachedBossBar bossBar = cachedBossBars.get(unique);
			float percentage = ReflectionUtils.getFloat(BOSS_HEALTH, packet);
			bossBar.updateHealth(percentage, this);
			return;
		}
	}

	static class CachedBossBar {
		private long unique;
		private String title;
		private float percentage;

		public CachedBossBar(long unique, String title, float health) {
			this.unique = unique;
			this.title = title;
			this.percentage = health;
		}

		public void spawn(BossBarPacketListener listener) {
			CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata = new CollectionsUtils.ArrayMap<>(76);
			metadata.put(4, new DataWatcherObjectString(title));
			metadata.put(39, new DataWatcherObjectFloatLe(0.001f)); // scale
			metadata.put(54, new DataWatcherObjectFloatLe(0.001f)); // bb width
			metadata.put(55, new DataWatcherObjectFloatLe(0.001f)); // bb height

			SpawnEntityPacket packet = new SpawnEntityPacket(
					unique,
					BOSS_BAR_ENTITY_ID,
					0, -10, 0, // coordinates
					0, 0, 0, // motion
					0, 0, // pitch & yaw
					Arrays.asList(new SetAttributesPacket.Attribute("minecraft:health", 0.0f, 100f, percentage * 100, percentage * 100)),
					metadata
			);

			PocketCon.sendPocketPacket(listener.con, packet);
			PocketCon.sendPocketPacket(listener.con, new BossEventPacket(unique, BossEventPacket.SHOW));
		}

		public void despawn(BossBarPacketListener listener) {
			PocketCon.sendPocketPacket(listener.con, new EntityDestroyPacket(unique));
			PocketCon.sendPocketPacket(listener.con, new BossEventPacket(unique, BossEventPacket.REMOVE));
		}

		public void updateHealth(float percentage, BossBarPacketListener listener) {
			this.percentage = percentage;
			updateAttributes(listener);
		}

		public void updateMetadata(BossBarPacketListener listener) {
			CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata = new CollectionsUtils.ArrayMap<>(76);
			metadata.put(4, new DataWatcherObjectString(title));
			PocketCon.sendPocketPacket(listener.con, new EntityDataPacket(unique, metadata));
		}

		public void updateAttributes(BossBarPacketListener listener) {
			PocketCon.sendPocketPacket(listener.con, new SetAttributesPacket(unique, new SetAttributesPacket.Attribute("minecraft:health", 0.0f, 100f, percentage * 100, percentage * 100)));
			PocketCon.sendPocketPacket(listener.con, new BossEventPacket(unique, BossEventPacket.UPDATE));
		}
	}
}
