package protocolsupportpocketstuff;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import protocolsupport.api.Connection;
import protocolsupport.api.events.ConnectionHandshakeEvent;
import protocolsupport.api.events.ConnectionOpenEvent;
import protocolsupport.api.unsafe.pemetadata.PEMetaProviderSPI;
import protocolsupport.api.unsafe.peskins.PESkinsProviderSPI;
import protocolsupportpocketstuff.api.PocketStuffAPI;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.commands.CommandHandler;
import protocolsupportpocketstuff.hacks.dimensions.DimensionListener;
import protocolsupportpocketstuff.metadata.MetadataProvider;
import protocolsupportpocketstuff.packet.handshake.ClientLoginPacket;
import protocolsupportpocketstuff.packet.play.ModalResponsePacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.resourcepacks.ResourcePackManager;
import protocolsupportpocketstuff.skin.PcToPeProvider;
import protocolsupportpocketstuff.skin.SkinListener;
import protocolsupportpocketstuff.storage.Skins;
import protocolsupportpocketstuff.util.ResourcePackListener;

import java.io.File;

public class ProtocolSupportPocketStuff extends JavaPlugin implements Listener {
	public static final String PREFIX = "[" + ChatColor.DARK_PURPLE + "PSPS" + ChatColor.RESET + "] ";
	private static ProtocolSupportPocketStuff INSTANCE;

	public static ProtocolSupportPocketStuff getInstance() {
		return INSTANCE;
	}

	@Override
	public void onEnable() {
		INSTANCE = this;

		getCommand("protocolsupportpocketstuff").setExecutor(new CommandHandler());

		// = Config = \\
		saveDefaultConfig();

		new File(this.getDataFolder(), ResourcePackManager.FOLDER_NAME + "/").mkdirs();

		ResourcePackManager resourcePackManager = new ResourcePackManager(this);
		resourcePackManager.reloadPacks();
		PocketStuffAPI.setResourcePackManager(resourcePackManager);

		// = Events = \\
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		if(getConfig().getBoolean("skins.PCtoPE")) { pm.registerEvents(new SkinListener(this), this); }
		if(getConfig().getBoolean("hacks.dimensions")) { pm.registerEvents(new DimensionListener(), this); }
		
		// = SPI = \\
		if(getConfig().getBoolean("skins.PCtoPE")) { PESkinsProviderSPI.setProvider(new PcToPeProvider(this)); }
		PEMetaProviderSPI.setProvider(new MetadataProvider());

		// = Cache = \\
		Skins.INSTANCE.buildCache(getConfig().getInt("skins.cache-size"), getConfig().getInt("skins.cache-rate"));
		
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
			con.addPacketListener(new ResourcePackListener(this, con));
			if(getConfig().getBoolean("skins.PEtoPC")) { con.addPacketListener(new SkinPacket().new decodeHandler(this, con)); }
			
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
		msg = "[" + ChatColor.DARK_PURPLE + "PSPS" + ChatColor.RESET + "] " + msg;
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
		msg = "[" + ChatColor.RED + "PSPS" + ChatColor.RESET + "] " + msg;
		if (getConfig().getBoolean("logging.disable-colors", false)) {
			msg = ChatColor.stripColor(msg);
		}
		getServer().getConsoleSender().sendMessage(msg);
	}
}
