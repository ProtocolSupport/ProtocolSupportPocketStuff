package protocolsupportpocketstuff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import protocolsupport.api.unsafe.peskins.PESkinsProviderSPI;
import protocolsupportpocketstuff.listeners.event.SkinListener;
import protocolsupportpocketstuff.skin.PcToPeProvider;

public class ProtocolSupportPocketStuff extends JavaPlugin {
	
	public ProtocolSupportPocketStuff() { }
	
	@Override
	public void onEnable() {
		PESkinsProviderSPI.setProvider(new PcToPeProvider(this));
		getServer().getPluginManager().registerEvents(new SkinListener(this), this);
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Hello World!" + ChatColor.RESET);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Bye World!" + ChatColor.RESET);
	}
	
}
