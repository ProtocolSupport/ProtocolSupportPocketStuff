package protocolsupportpocketstuff.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.PocketPlayer;
import protocolsupportpocketstuff.api.Utils;

public class SkinListener implements Listener {
	
	private ProtocolSupportPocketStuff plugin;
	
	public SkinListener(ProtocolSupportPocketStuff plugin) {
		this.plugin = plugin;
	}
	
	//TODO: MAKE EVERYTHING :P
	
	
	//Test to send packet.
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getMessage().contains(".meep")) {
			Player p = e.getPlayer();
			p.sendMessage("Meep!");
			if(Utils.isPocketPlayer(p)) {
				PocketPlayer pp = new PocketPlayer(p);
				pp.sendModal(plugin.helloModal);
			}
		}
	}
	
}
