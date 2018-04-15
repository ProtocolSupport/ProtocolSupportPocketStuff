package protocolsupportpocketstuff;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import protocolsupport.api.Connection;
import protocolsupport.api.ServerPlatformIdentifier;
import protocolsupport.api.events.ConnectionHandshakeEvent;
import protocolsupport.api.events.ConnectionOpenEvent;
import protocolsupport.api.unsafe.pemetadata.PEMetaProviderSPI;
import protocolsupport.api.unsafe.peskins.PESkinsProviderSPI;
import protocolsupportpocketstuff.api.PocketStuffAPI;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.commands.CommandHandler;
import protocolsupportpocketstuff.hacks.bossbars.BossBarPacketListener;
import protocolsupportpocketstuff.hacks.holograms.HologramsPacketListener;
import protocolsupportpocketstuff.hacks.itemframes.ItemFramesPacketListener;
import protocolsupportpocketstuff.hacks.skulls.SkullTilePacketListener;
import protocolsupportpocketstuff.metadata.MetadataProvider;
import protocolsupportpocketstuff.packet.handshake.ClientLoginPacket;
import protocolsupportpocketstuff.packet.play.BlockPickRequestPacket;
import protocolsupportpocketstuff.packet.play.ModalResponsePacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.resourcepacks.ResourcePackManager;
import protocolsupportpocketstuff.skin.PcToPeProvider;
import protocolsupportpocketstuff.skin.SkinListener;
import protocolsupportpocketstuff.storage.Skins;
import protocolsupportpocketstuff.util.ResourcePackListener;

public class ProtocolSupportPocketStuff extends JavaPlugin implements Listener {
	public static final String PREFIX = "[" + ChatColor.DARK_PURPLE + "PSPS" + ChatColor.RESET + "] ";
	private static ProtocolSupportPocketStuff INSTANCE;
	public static ProtocolSupportPocketStuff getInstance() {
		return INSTANCE;
	}

	@Override
	public void onEnable() {
		INSTANCE = this;
		// = Config = \\
		saveDefaultConfig();
		// = SPI = \\
		if(getConfig().getBoolean("skins.PCtoPE")) { PESkinsProviderSPI.setProvider(new PcToPeProvider(this)); }
		PEMetaProviderSPI.setProvider(new MetadataProvider());
		// = Cache = \\
		Skins.getInstance().buildCache(getConfig().getInt("skins.cache-size"), getConfig().getInt("skins.cache-rate"));
		// = ResourcePacks = \\
		ResourcePackManager resourcePackManager = new ResourcePackManager();
		resourcePackManager.reloadPacks();
		PocketStuffAPI.setResourcePackManager(resourcePackManager);
		// = Events = \\
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		if(getConfig().getBoolean("skins.PCtoPE")) { pm.registerEvents(new SkinListener(this), this); }
		// = Commands = \\
		getCommand("protocolsupportpocketstuff").setExecutor(new CommandHandler());
		// = Welcome = \\
		pm("Hello world! :D");
	}

	@EventHandler
	public void onConnectionOpen(ConnectionOpenEvent e) {
		Connection con = e.getConnection();
		// We can't check if it is a PE player yet because it is too early to figure out
		con.addPacketListener(new ClientLoginPacket().new decodeHandler(this, con));
	}

	@EventHandler
	public void onConnectionHandshake(ConnectionHandshakeEvent e) {
		Connection con = e.getConnection();
		if(PocketCon.isPocketConnection(con)) {
			// = Packet Listeners = \\
			con.addPacketListener(new ModalResponsePacket().new decodeHandler(this, con));
			if (!PocketStuffAPI.getResourcePackManager().isEmpty()) { con.addPacketListener(new ResourcePackListener(this, con)); }
			if (getConfig().getBoolean("skins.PEtoPC")) { con.addPacketListener(new SkinPacket().new decodeHandler(this, con)); }
			if (getConfig().getBoolean("hacks.middleclick")) { con.addPacketListener(new BlockPickRequestPacket().new decodeHandler(this, con)); }
			if (getConfig().getBoolean("hacks.holograms")) { con.addPacketListener(new HologramsPacketListener(con)); }
			if (getConfig().getBoolean("hacks.player-heads-skins.skull-blocks")) { con.addPacketListener(new SkullTilePacketListener(con)); }
			if (ServerPlatformIdentifier.get() == ServerPlatformIdentifier.SPIGOT) {
				if (getConfig().getBoolean("hacks.itemframes")) {
					con.addPacketListener(new ItemFramesPacketListener(this, con));
				}
				if (getConfig().getBoolean("hacks.bossbars")) {
					con.addPacketListener(new BossBarPacketListener(con));
				}
			}
		}
	}

	@Override
	public void onDisable() {
		pm("Bye world :O");
	}

	/**
	 * Sends a plugin message.
	 * @param msg
	 */
	public void pm(String msg) {
		msg = PREFIX + msg;
		if (getConfig().getBoolean("logging.disable-colors", false)) {
			msg = ChatColor.stripColor(msg);
		}
		getServer().getConsoleSender().sendMessage(msg);
	}

	/**
	 * Sends a debug plugin message.
	 * @param msg
	 */
	public void debug(String msg) {
		if (!getConfig().getBoolean("logging.enable-debug", false)) { return; }
		msg = PREFIX + " [DEBUG] " + msg;
		if (getConfig().getBoolean("logging.disable-colors", false)) {
			msg = ChatColor.stripColor(msg);
		}
		getServer().getConsoleSender().sendMessage(msg);
	}
}
