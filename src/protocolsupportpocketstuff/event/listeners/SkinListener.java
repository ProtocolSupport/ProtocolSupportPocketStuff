package protocolsupportpocketstuff.event.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.modals.SimpleForm;
import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.api.util.PocketUtils;

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
				PocketCon.sendModal(con, new SimpleForm().setTitle("Hoi").setContent("hallo").addButton(new ModalButton("Magbot").setImage(new ModalImage(ModalImage.ModalImageType.EXTERNAL_IMAGE, "http://magbot.nl/img/MagBot.png"))));
			}
		}
	}
	
}
