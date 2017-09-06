package protocolsupportpocketstuff.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.PocketConnection;
import protocolsupportpocketstuff.api.PocketUtils;
import protocolsupportpocketstuff.api.modals.SimpleForm;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton.ModalImageType;

public class SkinListener implements Listener {
	
	//Whatever. Will be used later probably.
	@SuppressWarnings("unused")
	private ProtocolSupportPocketStuff plugin;
	
	public SkinListener(ProtocolSupportPocketStuff plugin) {
		this.plugin = plugin;
	}
	
	//TODO: MAKE EVERYTHING :P
	
	
	//Test to send packet.
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getMessage().contains(".meep")) {
			e.getPlayer().sendMessage("Meep!");
			for(Player p : PocketUtils.getPocketPlayers()) {
				e.getPlayer().sendMessage("MEEEEEEP!");
				PocketConnection pcc = PocketConnection.get(p);
				pcc.sendModal(new SimpleForm().setTitle("Hoi").setContent("hallo").addButton(new ModalButton().setText("Magbot").setImage(ModalImageType.EXTERNAL_IMAGE, "http://magbot.nl/img/MagBot.png")));
				//pcc.sendModal(0, "{'type':'modal','title':'MEEP','content':'Meep, Meep.','button1':'Meep?','button2':'Meep!'}");
			}
		}
	}
	
}
