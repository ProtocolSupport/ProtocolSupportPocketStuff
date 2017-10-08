package protocolsupportpocketstuff;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import protocolsupport.api.Connection;
import protocolsupport.api.events.ConnectionHandshakeEvent;
import protocolsupport.api.unsafe.peskins.PESkinsProviderSPI;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.hacks.dimensions.DimensionListener;
import protocolsupportpocketstuff.packet.play.ModalResponsePacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.skin.PcToPeProvider;
import protocolsupportpocketstuff.skin.SkinListener;
import protocolsupportpocketstuff.storage.Skins;

public class ProtocolSupportPocketStuff extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		
		// = Config = \\
		saveDefaultConfig();
		
		// = Events = \\
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this, this);
		if(getConfig().getBoolean("skins.PCtoPE")) { pm.registerEvents(new SkinListener(this), this); }
		if(getConfig().getBoolean("hacks.dimensions")) { pm.registerEvents(new DimensionListener(), this); }
		
		// = SPI = \\
		if(getConfig().getBoolean("skins.PCtoPE")) { PESkinsProviderSPI.setProvider(new PcToPeProvider(this)); }
		
		// = Cache = \\
		Skins.INSTANCE.buildCache(getConfig().getInt("skins.cache-size"), getConfig().getInt("skins.cache-rate"));
		
		pm("Hello world! :D");
	}
	
	@EventHandler
	public void onConnectionHandshake(ConnectionHandshakeEvent e) {
		Connection con = e.getConnection();
		if(PocketCon.isPocketConnection(con)) {
			
			// = Packet Listeners = \\
			con.addPacketListener(new ModalResponsePacket().new decodeHandler(this, con));
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
		getServer().getConsoleSender().sendMessage("[" + ChatColor.DARK_PURPLE + "PSPS" + ChatColor.RESET + "] " + msg);
	}
	
}
