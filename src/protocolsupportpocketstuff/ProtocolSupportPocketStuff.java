package protocolsupportpocketstuff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import protocolsupportpocketstuff.api.modals.ModalBuilder;
import protocolsupportpocketstuff.api.modals.Modals;
import protocolsupportpocketstuff.listeners.SkinListener;

public class ProtocolSupportPocketStuff extends JavaPlugin {
	
	public int helloModal = 0;
	
	@Override
	public void onEnable() {
		//Test
		ModalBuilder mb = new ModalBuilder();
		mb.addString("Title", "Hello!");
		helloModal = Modals.register(mb.toModal());
		
		getServer().getPluginManager().registerEvents(new SkinListener(this), this);
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Hello World!" + ChatColor.RESET);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Bye World!" + ChatColor.RESET);
	}
	
}
