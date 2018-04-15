package protocolsupportpocketstuff.skin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import protocolsupport.api.Connection;
import protocolsupport.api.events.PlayerPropertiesResolveEvent;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.event.ComplexFormResponseEvent;
import protocolsupportpocketstuff.api.event.ModalResponseEvent;
import protocolsupportpocketstuff.api.event.ModalWindowResponseEvent;
import protocolsupportpocketstuff.api.event.PocketChangeSkinEvent;
import protocolsupportpocketstuff.api.event.SimpleFormResponseEvent;
import protocolsupportpocketstuff.api.modals.SimpleForm;
import protocolsupportpocketstuff.api.modals.callback.SimpleFormCallback;
import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.ModalImage.ModalImageType;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;
import protocolsupportpocketstuff.api.skins.SkinUtils;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.util.StuffUtils;

public class SkinListener implements Listener {
	
	private ProtocolSupportPocketStuff plugin;
	
	public SkinListener(ProtocolSupportPocketStuff plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerPropertiesResolve(PlayerPropertiesResolveEvent e) {
		Connection con = e.getConnection();
		if (PocketCon.isPocketConnection(con)) {
			if (con.hasMetadata(StuffUtils.APPLY_SKIN_ON_JOIN_KEY)) {
				plugin.debug("Applying cached skin for " + e.getConnection() + "...");
				SkinUtils.SkinDataWrapper skinDataWrapper = (SkinUtils.SkinDataWrapper) con.getMetadata(StuffUtils.APPLY_SKIN_ON_JOIN_KEY);
				e.addProperty(new PlayerPropertiesResolveEvent.ProfileProperty(StuffUtils.SKIN_PROPERTY_NAME, skinDataWrapper.getValue(), skinDataWrapper.getSignature()));
				con.removeMetadata(StuffUtils.APPLY_SKIN_ON_JOIN_KEY);
			}
		}
	}

	//Test to send packet.
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getMessage().contains(".meep")) {
			e.getPlayer().sendMessage("Meep!");
			for(Connection con : PocketCon.getPocketConnections()) {
				e.getPlayer().sendMessage("MEEEEEP!!");
				PocketCon.sendModal(con, 
						new SimpleForm("hoi", "hallo")
							.addButton(new ModalButton("Magbot").setImage(new ModalImage(ModalImageType.EXTERNAL_IMAGE, "http://magbot.nl/img/MagBot.png")))
							.addButton(new ModalButton("Awesome").setImage(new ModalImage(ModalImageType.EXTERNAL_IMAGE, "http://yumamom.com/wp-content/uploads/2015/05/LEGO.jpg"))),
						new SimpleFormCallback() {

							@Override
							public void onSimpleFormResponse(Player player, String modalJSON, boolean isClosedByClient, int clickedButton) {
								player.sendMessage("Thanks for clicking! :wave:");
							}
						});
			}
		}
	}

	@EventHandler
	public void onSkinChange(PocketChangeSkinEvent e) {
		plugin.debug(e.getPlayer().getName() + " changed skin in-game: Slim Skin? " + e.isSlim() + " Width: " + e.getSkin().getWidth());
		new MineskinThread(plugin, e.getConnection(), e.getUuid().toString(), e.getSkin(), e.isSlim()).start();
	}
	
	//:F
	@EventHandler
	public void onClientResponse(ModalResponseEvent e) {
		plugin.debug("ClientResponseEvent received ~ " + e.getClass().getSimpleName() + " ~ JSON: " + e.getModalJSON());

		PocketCon.handleModalResponse(e.getConnection(), e);
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
