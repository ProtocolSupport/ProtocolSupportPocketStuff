package protocolsupportpocketstuff.skin;

import java.util.Base64;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import protocolsupport.api.Connection;
import protocolsupport.api.events.PlayerLoginFinishEvent;
import protocolsupport.api.utils.ProfileProperty;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.skins.SkinUtils;
import protocolsupportpocketstuff.api.skins.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.handshake.ClientLoginPacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.storage.Skins;

public class PeToPcProvider implements PocketPacketListener, Listener {

	private static final ProtocolSupportPocketStuff plugin = ProtocolSupportPocketStuff.getInstance();
	public static final String SKIN_PROPERTY_NAME = "textures";

	@PocketPacketHandler
	public void onConnect(Connection connection, ClientLoginPacket packet) {
		if (packet.getJsonPayload() == null) { return; }
		String skinData = packet.getJsonPayload().get("SkinData").getAsString();
		byte[] skinByteArray = Base64.getDecoder().decode(skinData);
		boolean slim = SkinUtils.slimFromModel(packet.getJsonPayload().get("SkinGeometryName").getAsString());
		new MineskinThread(skinByteArray, slim, (skindata) -> { /* Just cache it. */}).start();
	}

	@EventHandler
	public void onLoginFinish(PlayerLoginFinishEvent event) {
		if (Skins.getInstance().hasPeSkin(event.getUUID().toString())) {
			SkinDataWrapper skinDataWrapper = Skins.getInstance().getPeSkin(event.getUUID().toString());
			event.addProperty(new ProfileProperty(SKIN_PROPERTY_NAME, skinDataWrapper.getValue(), skinDataWrapper.getSignature()));
		}
	}

	@PocketPacketHandler
	public void onSkinChange(Connection connection, SkinPacket packet) {
		boolean slim = SkinUtils.slimFromModel(packet.getGeometryId());
		new MineskinThread(packet.getSkinData(), slim, (skindata) -> {
			plugin.debug("Dynamic skin update!");
			SkinUtils.updateSkin(connection.getPlayer(), packet.getSkinData(), skindata, slim);
		}).start();
	}

}
