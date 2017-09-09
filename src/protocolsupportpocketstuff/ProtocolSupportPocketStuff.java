package protocolsupportpocketstuff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import protocolsupportpocketstuff.event.listeners.SkinListener;

public class ProtocolSupportPocketStuff extends JavaPlugin {
	
	public ProtocolSupportPocketStuff() { }
	public static ProtocolSupportPocketStuff INSTANCE;
	private boolean offlineMode = true;
	
	public boolean serverInOfflinemode() {
		return offlineMode;
	}
	
	@Override
	public void onEnable() {
		ProtocolSupportPocketStuff.INSTANCE = this;
		getServer().getPluginManager().registerEvents(new SkinListener(this), this);
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Hello World!" + ChatColor.RESET);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Bye World!" + ChatColor.RESET);
	}
	
}
