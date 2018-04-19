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
import protocolsupportpocketstuff.api.resourcepacks.ResourcePackManager;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.commands.CommandHandler;
import protocolsupportpocketstuff.hacks.bossbars.BossBarPacketListener;
import protocolsupportpocketstuff.hacks.holograms.HologramsPacketListener;
import protocolsupportpocketstuff.hacks.itemframes.ItemFramesPacketListener;
import protocolsupportpocketstuff.hacks.skulls.SkullTilePacketListener;
import protocolsupportpocketstuff.metadata.EntityMetadataProvider;
import protocolsupportpocketstuff.modals.ModalReceiver;
import protocolsupportpocketstuff.packet.PEReceiver;
import protocolsupportpocketstuff.resourcepacks.ResourcePackListener;
import protocolsupportpocketstuff.skin.PcToPeProvider;
import protocolsupportpocketstuff.skin.PeToPcProvider;
import protocolsupportpocketstuff.storage.Skins;
import protocolsupportpocketstuff.util.PocketInfoReceiver;

public class ProtocolSupportPocketStuff extends JavaPlugin implements Listener {

	public static final String PREFIX = "[" + ChatColor.DARK_PURPLE + "PSPS" + ChatColor.RESET + "] ";

	private static ProtocolSupportPocketStuff INSTANCE;
	public static ProtocolSupportPocketStuff getInstance() {
		return INSTANCE;
	}

	@Override
	public void onLoad() {
		INSTANCE = this;
		// = ResourcePacks = \\
		ResourcePackManager resourcePackManager = new ResourcePackManager();
		resourcePackManager.reloadPacks();
		PocketStuffAPI.setResourcePackManager(resourcePackManager);
	}
	
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		// = Config = \\
		saveDefaultConfig();
		// = Storage = \\
		Skins.getInstance().buildCache(
			getConfig().getInt("skins.cache-size"), 
			getConfig().getInt("skins.cache-rate")
		);
		// = API = \\
		PocketStuffAPI.registerPacketListeners(new PocketInfoReceiver());
		PocketStuffAPI.registerPacketListeners(new ModalReceiver());
		// = Metadata = \\
		PEMetaProviderSPI.setProvider(new EntityMetadataProvider());
		// = ResourcePacks = \\
		if (!PocketStuffAPI.getResourcePackManager().isEmpty()) {
			ResourcePackListener provider = new ResourcePackListener();
			pm.registerEvents(provider, this);
			PocketStuffAPI.registerPacketListeners(provider);
		}
		// = Skins = \\
		if (getConfig().getBoolean("skins.PCtoPE")) {
			PESkinsProviderSPI.setProvider(new PcToPeProvider());
		}
		if (getConfig().getBoolean("skins.PEtoPC")) {
			PeToPcProvider provider = new PeToPcProvider();
			pm.registerEvents(provider, this);
			PocketStuffAPI.registerPacketListeners(provider);
		}
		// = Commands = \\
		getCommand("protocolsupportpocketstuff").setExecutor(new CommandHandler());
		// = Welcome = \\
		pm("Hello world! :D");
	}

	@EventHandler
	public void onConnectionOpen(ConnectionOpenEvent e) {
		Connection con = e.getConnection();
		//We can't check if it is a PE player yet because it is too early to figure out
		//The packet listener will detach automatically if the connection turns out to be non-pe.
		con.addPacketListener(new PEReceiver.PEReceiverListener(con));
	}

	@EventHandler
	public void onConnectionHandshake(ConnectionHandshakeEvent e) {
		Connection con = e.getConnection();
		if (PocketCon.isPocketConnection(con)) {
			// = Packet Listeners = \\
			//con.addPacketListener(new ModalResponsePacket().new decodeHandler(this, con));
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
		//if (!getConfig().getBoolean("logging.enable-debug", false)) { return; }
		msg = PREFIX + " [DEBUG] " + msg;
		if (getConfig().getBoolean("logging.disable-colors", false)) {
			msg = ChatColor.stripColor(msg);
		}
		getServer().getConsoleSender().sendMessage(msg);
	}

}
