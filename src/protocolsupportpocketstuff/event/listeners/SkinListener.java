package protocolsupportpocketstuff.event.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.events.PlayerPropertiesResolveEvent;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.modals.SimpleForm;
import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.api.util.PocketPlayer;
import protocolsupportpocketstuff.api.util.SkinUtils;
import protocolsupportpocketstuff.skin.SkinDownloader;
import protocolsupportpocketstuff.storage.Skins;

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
			for(Player p : PocketPlayer.getPocketPlayers()) {
				Connection con = ProtocolSupportAPI.getConnection(p);
				e.getPlayer().sendMessage("MEEEEEEP!");
				PocketCon.sendModal(con, new SimpleForm().setTitle("Hoi").setContent("hallo").addButton(new ModalButton("Magbot").setImage(new ModalImage(ModalImage.ModalImageType.EXTERNAL_IMAGE, "http://magbot.nl/img/MagBot.png"))));
			}
		} else if (e.getMessage().contains(".moop")) {
			PocketPlayer.getPocketPlayers().forEach(p -> PocketPlayer.sendSkins(p));
		}
	}
	
	
	//REAL:
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSkinResolve(PlayerPropertiesResolveEvent e) {
		System.out.println("Resolved PlayerProperties: ");
		System.out.println(e.toString());
		System.out.println(e.getProperties());
		if(e.getProperties().containsKey(SkinUtils.skinPropertyName)) {
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin,
					new SkinDownloader(e.getName(), e.getProperties().get(SkinUtils.skinPropertyName).getValue()));
		}
	}
	
	@EventHandler()
	public void onLogOut(PlayerQuitEvent e) {
		Skins.INSTANCE.clearSkin(e.getPlayer().getName().trim());
		Skins.INSTANCE.getPeSkins().forEach((name, skin) -> plugin.getServer().getConsoleSender().sendMessage(name));
	}
	
}
