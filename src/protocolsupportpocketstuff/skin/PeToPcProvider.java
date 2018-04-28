package protocolsupportpocketstuff.skin;

import java.util.Base64;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import protocolsupport.api.Connection;
import protocolsupport.api.events.PlayerPropertiesResolveEvent;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.skins.SkinUtils;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.handshake.ClientLoginPacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.util.PacketUtils;
import protocolsupportpocketstuff.util.StuffUtils;

public class PeToPcProvider implements PocketPacketListener, Listener {

	private static final ProtocolSupportPocketStuff plugin = ProtocolSupportPocketStuff.getInstance();
	public static final String SKIN_PROPERTY_NAME = "textures";
	public static final String APPLY_SKIN_ON_JOIN_KEY = "applySkinOnJoin";

	@PocketPacketHandler
	public void onConnect(Connection connection, ClientLoginPacket packet) {
		if (packet.getJsonPayload() == null) { return; }
		String skinData = packet.getJsonPayload().get("SkinData").getAsString();
		byte[] skinByteArray = Base64.getDecoder().decode(skinData);
		boolean slim = packet.getJsonPayload().get("SkinGeometryName").getAsString().equals("geometry.humanoid.customSlim");
		plugin.debug("SLIM: " + slim + " DATA LENGTH: " + skinByteArray.length);
		new MineskinThread(skinByteArray, slim, (skindata) -> {
			//TODO rewrite when PS properties event is more sane.
			if (connection.getPlayer() == null) {
				plugin.debug("MEta added!");
				connection.addMetadata(StuffUtils.APPLY_SKIN_ON_JOIN_KEY, skindata);
			}
			new PacketUtils.RunWhenOnline(connection, () -> {
				plugin.debug("Dynamic skin update!");
				SkinUtils.updateSkin(connection.getPlayer(), skinByteArray, skindata, slim);
			}, 2, true, 200l).start();
		}).start();
	}

	@PocketPacketHandler
	public void onSkinChange(Connection connection, SkinPacket packet) {
		System.out.println(packet.getGeometryId());
		boolean slim = packet.getGeometryId().equals("geometry.humanoid.customSlim");
		new MineskinThread(packet.getSkinData(), slim, (skindata) -> {
			plugin.debug("Dynamic skin update!");
			SkinUtils.updateSkin(connection.getPlayer(), packet.getSkinData(), skindata, slim);
		}).start();
	}

	@EventHandler
	public void onPlayerPropertiesResolve(PlayerPropertiesResolveEvent event) {
		Connection con = event.getConnection();
		plugin.debug("Props for " + event.getName() + "...");
		if (PocketCon.isPocketConnection(con)) {
			//In this event the server actually doesn't have the uuid registered yet.
			if (con.hasMetadata(StuffUtils.APPLY_SKIN_ON_JOIN_KEY)) {
				plugin.debug("Applying cached skin for " + event.getName() + "...");
				SkinUtils.SkinDataWrapper skinDataWrapper = (SkinUtils.SkinDataWrapper) con.getMetadata(APPLY_SKIN_ON_JOIN_KEY);
				event.addProperty(new PlayerPropertiesResolveEvent.ProfileProperty(SKIN_PROPERTY_NAME, skinDataWrapper.getValue(), skinDataWrapper.getSignature()));
				con.removeMetadata(APPLY_SKIN_ON_JOIN_KEY);
			}
		}
	}

}
