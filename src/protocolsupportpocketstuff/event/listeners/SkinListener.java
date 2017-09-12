package protocolsupportpocketstuff.event.listeners;

import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import protocolsupport.api.Connection;
import protocolsupport.api.events.PlayerListEvent;
//import protocolsupport.api.events.PlayerPropertiesResolveEvent;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.modals.SimpleForm;
import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.ModalImage.ModalImageType;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;
import protocolsupportpocketstuff.api.util.PocketCon;
//import protocolsupportpocketstuff.api.util.SkinUtils;
import protocolsupportpocketstuff.skin.SkinRunner;

public class SkinListener implements Listener {
	
	private ProtocolSupportPocketStuff plugin;
	
	public SkinListener(ProtocolSupportPocketStuff plugin) {
		this.plugin = plugin;
	}
	
	//Test to send packet.
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getMessage().contains(".meep")) {
			e.getPlayer().sendMessage("Meep!");
			for(Connection con : PocketCon.getPocketConnections()) {
				e.getPlayer().sendMessage("MEEEEEEP!");
				PocketCon.sendModal(con, 
						new SimpleForm("hoi", "hallo")
							.addButton(new ModalButton("Magbot").setImage(new ModalImage(ModalImageType.EXTERNAL_IMAGE, "http://magbot.nl/img/MagBot.png")))
							.addButton(new ModalButton("Awesome").setImage(new ModalImage(ModalImageType.EXTERNAL_IMAGE, "http://yumamom.com/wp-content/uploads/2015/05/LEGO.jpg"))));
			}
		}
	}
	
	@EventHandler
	public void PlayerList(PlayerListEvent e) {
		System.out.println("blubububbu");
		e.getInfos().forEach(i -> plugin.getServer().getScheduler().runTaskAsynchronously(plugin,
					new SkinRunner(e.getConnection(), i.getUuid(), i.getUsername(), i.getProperties())));
	}
	
	//Somehow this seems to mess with a PE client that also has a PC skin.
	//Since we will do PE -> PC skins in the future I do not really care(tm).
//	@EventHandler(priority = EventPriority.MONITOR)
//	public void propertyResolve(PlayerPropertiesResolveEvent e) {
//		if(e.hasProperty(SkinUtils.skinPropertyName) && PocketCon.isPocketConnection(e.getConnection())) {
//			e.removeProperty(SkinUtils.skinPropertyName);
//		}
//	}
	
}
