package protocolsupportpocketstuff.event.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.ConnectionUtils;
import protocolsupportpocketstuff.api.PocketUtils;
import protocolsupportpocketstuff.api.modals.SimpleForm;
import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;

public class SkinListener implements Listener {
	
	//Whatever. Will be used later probably.
	@SuppressWarnings("unused")
	private ProtocolSupportPocketStuff plugin;
	
	public SkinListener(ProtocolSupportPocketStuff plugin) {
		this.plugin = plugin;
	}
	
	//Test to send packet.
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getMessage().contains(".meep")) {
			e.getPlayer().sendMessage("Meep!");
			for(Player p : PocketUtils.getPocketPlayers()) {
				Connection con = ProtocolSupportAPI.getConnection(p);
				e.getPlayer().sendMessage("MEEEEEEP!");
				ConnectionUtils.sendModal(con, new SimpleForm().setTitle("Hoi").setContent("hallo").addButton(new ModalButton().setText("Magbot").setImage(ModalImage.ModalImageType.EXTERNAL_IMAGE, "http://magbot.nl/img/MagBot.png")));
			}
		}
	}
	
}
