package protocolsupportpocketstuff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ProtocolSupportPocketStuff extends JavaPlugin {
	
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Hello World!" + ChatColor.RESET);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Bye World!" + ChatColor.RESET);
	}
	
}
