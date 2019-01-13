package protocolsupportpocketstuff.skin;

import java.util.Base64;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import protocolsupport.api.Connection;
import protocolsupport.api.events.PlayerProfileCompleteEvent;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.skins.SkinUtils;
import protocolsupportpocketstuff.api.skins.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.handshake.ClientLoginPacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.util.PacketUtils;


public class PeToPcProvider implements PocketPacketListener, Listener {

	private static final ProtocolSupportPocketStuff plugin = ProtocolSupportPocketStuff.getInstance();
	public static final String TRANSFER_SKIN = "PEApplySkinOnJoin";

	@PocketPacketHandler
	public void onConnect(Connection connection, ClientLoginPacket packet) {
		if (packet.getJsonPayload() == null) { return; }
		byte[] skin = Base64.getDecoder().decode(packet.getJsonPayload().get("SkinData").getAsString());
		boolean isSlim = SkinUtils.slimFromModel(packet.getJsonPayload().get("SkinGeometryName").getAsString());
		new MineskinThread(skin, isSlim, (skindata) -> {
			connection.addMetadata(TRANSFER_SKIN, skindata);
			//Dynamically update when we can, propagate the skin to everyone.
			new PacketUtils.RunWhenOnline(connection, () -> {
				SkinUtils.updateSkin(connection.getPlayer(), skin, skindata, isSlim);
			}, 2, true, 200L);
		}).start();
	}

	@EventHandler
	public void onProfileFinish(PlayerProfileCompleteEvent event) {
		if (event.getConnection().hasMetadata(TRANSFER_SKIN)) {
			SkinDataWrapper skinData = (SkinDataWrapper) event.getConnection().getMetadata(TRANSFER_SKIN);
			event.addProperty(skinData.toProfileProperty());
		}
	}

	@PocketPacketHandler
	public void onSkinChange(Connection connection, SkinPacket packet) {
		boolean slim = SkinUtils.slimFromModel(packet.getGeometryId());
		new MineskinThread(packet.getSkinData(), slim, (skindata) -> {
			plugin.debug(connection.getPlayer().getName() + " did a dynamic skin update!");
			SkinUtils.updateSkin(connection.getPlayer(), packet.getSkinData(), skindata, slim);
		}).start();
	}

}
