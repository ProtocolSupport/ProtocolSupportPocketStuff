package protocolsupportpocketstuff.skin;

import java.util.Base64;
import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import protocolsupport.api.Connection;
import protocolsupport.api.events.PlayerLoginFinishEvent;
import protocolsupport.api.utils.ProfileProperty;
import protocolsupport.protocol.utils.authlib.GameProfile;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.skins.SkinUtils;
import protocolsupportpocketstuff.api.skins.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.handshake.ClientLoginPacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.storage.Skins;
import protocolsupportpocketstuff.zplatform.PlatformThings;

public class PeToPcProvider implements PocketPacketListener, Listener {

	private static final ProtocolSupportPocketStuff plugin = ProtocolSupportPocketStuff.getInstance();
	public static final String SKIN_PROPERTY_NAME = "textures";

	//TODO: remove on player leave
	private Map<Connection, String> connectionToSkinMap = new WeakHashMap<>();

	@PocketPacketHandler
	public void onConnect(Connection connection, ClientLoginPacket packet) {
		if (packet.getJsonPayload() == null) { return; }
		String skinData = packet.getJsonPayload().get("SkinData").getAsString();
		byte[] skinByteArray = Base64.getDecoder().decode(skinData);
		boolean slim = SkinUtils.slimFromModel(packet.getJsonPayload().get("SkinGeometryName").getAsString());
		String uniqueSkinId = SkinUtils.uuidFromSkin(skinByteArray, slim).toString();
		String username = packet.getChainPayload().get("displayName").getAsString();
		connectionToSkinMap.put(connection, uniqueSkinId);
		new MineskinThread(skinByteArray, slim, (skindata) -> {
			Bukkit.getScheduler().runTask(ProtocolSupportPocketStuff.getInstance(), () -> {
				Player player = Bukkit.getPlayer(username);
				if (player != null) {
					PlatformThings.getStuff().setSkinProperties(player, skindata); // ?
				}
			});
		}).start();
	}

	@EventHandler
	public void onLoginFinish(PlayerLoginFinishEvent event) {
		String uniqueSkinId = connectionToSkinMap.get(event.getConnection());
		if (uniqueSkinId != null && Skins.getInstance().hasPeSkin(uniqueSkinId)) {
			SkinDataWrapper skinDataWrapper = Skins.getInstance().getPeSkin(uniqueSkinId);
			GameProfile gameProfile = (GameProfile)event.getConnection().getProfile();
			gameProfile.addProperty(new ProfileProperty(SKIN_PROPERTY_NAME, skinDataWrapper.getValue(), skinDataWrapper.getSignature()));
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
