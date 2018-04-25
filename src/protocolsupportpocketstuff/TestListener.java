package protocolsupportpocketstuff;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.modals.SimpleForm;
import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.ModalImage.ModalImageType;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;
import protocolsupportpocketstuff.api.modals.events.ComplexFormResponseEvent;
import protocolsupportpocketstuff.api.modals.events.ModalResponseEvent;
import protocolsupportpocketstuff.api.modals.events.ModalWindowResponseEvent;
import protocolsupportpocketstuff.api.modals.events.SimpleFormResponseEvent;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.api.util.PocketPacketListener;

public class TestListener implements Listener, PocketPacketListener {
	
	private ProtocolSupportPocketStuff plugin = ProtocolSupportPocketStuff.getInstance();

    //=====================================================\\
    //					From Pocket						   \\
    //=====================================================\\
	
	//Test to send packet.
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getMessage().contains(".meep")) {
			e.getPlayer().sendMessage("Meep!");
			for(Connection con : PocketCon.getPocketConnections()) {
				e.getPlayer().sendMessage("MEEEEEP!!");
				SimpleForm f = new SimpleForm("hoi", "hallo");
				f.clone();
				PocketCon.sendModal(con, 
					new SimpleForm("hoi", "hallo")
						.addButton(new ModalButton("Magbot").setImage(new ModalImage(ModalImageType.EXTERNAL_IMAGE, "http://magbot.nl/img/MagBot.png")))
						.addButton(new ModalButton("Awesome").setImage(new ModalImage(ModalImageType.EXTERNAL_IMAGE, "http://yumamom.com/wp-content/uploads/2015/05/LEGO.jpg"))), response -> {
							if (response.asSimpleFormResponse().getClickedButton() == 1) {
								response.getPlayer().sendMessage("You are awesome!");
							} else {
								response.getPlayer().sendMessage("You are not awesome! :(");
							}
						});
			}
		}
	}

	//:F
	@EventHandler
	public void onClientResponse(ModalResponseEvent e) {
		plugin.debug("ClientResponseEvent received ~ " + e.getClass().getSimpleName() + " ~ JSON: " + e.getResponseString());
	}

	@EventHandler
	public void onModalWindowResponse(ModalWindowResponseEvent e) {
		plugin.debug("ModalWindowResponseEvent received ~ " + e.getResult());
	}

	@EventHandler
	public void onSimpleFormResponse(SimpleFormResponseEvent e) {
		plugin.debug("SimpleFormResponseEvent received ~ " + e.getClickedButton());
	}

	@EventHandler
	public void onComplexFormResponse(ComplexFormResponseEvent e) {
		plugin.debug("ComplexFormResponseEvent received ~ " + e.getJsonArray());
	}
}
