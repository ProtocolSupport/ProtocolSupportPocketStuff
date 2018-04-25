package protocolsupportpocketstuff.api.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolType;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.modals.ModalCallback;
import protocolsupportpocketstuff.api.skins.PocketSkinModel;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.storage.Modals;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/***
 * Utility to send and retrieve pocket-specific things to a {@link Player}.
 */
public class PocketPlayer {

    //=====================================================\\
    //						Getting						   \\
    //=====================================================\\

	/***
	 * Checks if the player is a pocket player.
	 * @param player
	 * @return the truth.
	 */
	public static boolean isPocketPlayer(Player player) {
		return ProtocolSupportAPI.getProtocolVersion(player).getProtocolType().equals(ProtocolType.PE);
	}

	/***
	 * Gets all pocket players on the server.
	 * <br/><br/>
	 * <i>If your goal is sending packets, we advise to use
	 * {@link PocketCon.getPocketConnections} instead for better performance.</i>
	 * @return all pocket players.
	 */
	public static Collection<? extends Player> getPocketPlayers() {
		return Bukkit.getOnlinePlayers().stream().filter(pocketFilter()).collect(Collectors.toList());
	}

	/***
	 * Filter to filter PE players.
	 * @return the truth is a predicate.
	 */
	public static Predicate<Player> pocketFilter() {
		return p -> isPocketPlayer(p);
	}

	/***
	 * Sends a packet to pocket.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param player
	 * @param packet
	 */
	public static void sendPocketPacket(Player player, PEPacket packet) {
		PocketCon.sendPocketPacket(ProtocolSupportAPI.getConnection(player), packet);
	}

    //=====================================================\\
    //						Packets						   \\
    //=====================================================\\

	/***
	 * Sends a modal to a player and gets the callback id.
	 * The id can be used to track response of this modal in events.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param player
	 * @param modal
	 * @return the id of the modal.
	 */
	public static int sendModal(Player player, Modal modal) {
		return PocketCon.sendModal(ProtocolSupportAPI.getConnection(player), modal);
	}

	/***
	 * Sends a modal with an id specified.
	 * <em>Nonono, don't use custom ids!</em>
	 * If you like you can use this function in combination with
	 * {@link Modals.INSTANCE.takeId} to send custom JSON to the player.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param player
	 * @param modal
	 * @param modalCallback
	 * @return the id of the modal.
	 */
	public static int sendModal(Player player, Modal modal, ModalCallback modalCallback) {
		return PocketCon.sendModal(ProtocolSupportAPI.getConnection(player), modal, modalCallback);
	}

	/***
	 * Sends a modal with an id specified.
	 * Nonono, don't use custom ids!
	 * If you like you can use this function in combination with
	 * {@link Modals.INSTANCE.takeId} to send custom JSON to the player.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param player
	 * @param modalId
	 * @param modalJSON
	 * @return the id of the modal.
	 */
	public static int sendModal(Player player, int modalId, String modalJSON) {
		return PocketCon.sendModal(ProtocolSupportAPI.getConnection(player), modalId, modalJSON);
	}

	/***
	 * Sends a modal with an id specified.
	 * <em>Nonono, don't use custom ids!</em>
	 * If you like you can use this function in combination with
	 * {@link Modals.INSTANCE.takeId} to send custom JSON to the player.
	 * This method also registers specified callback (if not null)
	 * which is called after modal is completed and events are handled.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param player
	 * @param modalId
	 * @param modalJSON
	 * @param callback
	 * @return the id of the modal.
	 */
	public static int sendModal(Player player, int modalId, String modalJSON, ModalCallback callback) {
		return PocketCon.sendModal(ProtocolSupportAPI.getConnection(player), modalId, modalJSON, callback);
	}

	/***
	 * Sends a pc-like skin to a pocket connection.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param player
	 * @param uuid
	 * @param skin
	 * @param isSlim
	 */
	public static void sendSkin(Player player, UUID uuid, byte[] skin, boolean isSlim) {
		PocketCon.sendSkin(ProtocolSupportAPI.getConnection(player), uuid, skin, isSlim);
	}

	/***
	 * Sends a PocketSkin to a pocket connection.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param player
	 * @param uuid
	 * @param skin
	 * @param skinModel
	 */
	public static void sendSkin(Player player, UUID uuid, byte[] skin, PocketSkinModel skinModel) {
		PocketCon.sendSkin(ProtocolSupportAPI.getConnection(player), uuid, skin, skinModel);
	}

	/***
	 * Transfers a player to another server
	 * @param player
	 * @param address
	 * @param port
	 */
	public static void transfer(Player player, String address, short port) {
		PocketCon.transfer(ProtocolSupportAPI.getConnection(player), address, port);
	}

    //=====================================================\\
    //						Client-Info					   \\
    //=====================================================\\

	/***
	 * Gets the client's UUID. Used for updating skin of the self player or other UUID specific things.
	 * @param player
	 * @return
	 */
	public static UUID getClientUniqueId(Player player) {
		return PocketCon.getClientUniqueId(ProtocolSupportAPI.getConnection(player));
	}

	/***
	 * Gets the client random ID assigned upon install to the user. This can be edited by the client, so beware!
	 * @param player
	 * @return client's random id
	 */
	public static long getClientRandomId(Player player) {
		return PocketCon.getClientRandomId(ProtocolSupportAPI.getConnection(player));
	}

	/***
	 * Gets the client device model
	 * @param player
	 * @return client's device model
	 */
	public static String getDeviceModel(Player player) {
		return PocketCon.getDeviceModel(ProtocolSupportAPI.getConnection(player));
	}

	/***
	 * Gets the client operating system
	 * @param player
	 * @return client's operating system
	 */
	public static DeviceOperatingSystem getOperatingSystem(Player player) {
		return PocketCon.getOperatingSystem(ProtocolSupportAPI.getConnection(player));
	}

	/***
	 * Gets the client version
	 * @param player
	 * @return client version
	 */
	public static String getClientVersion(Player player) {
		return PocketCon.getClientVersion(ProtocolSupportAPI.getConnection(player));
	}

	/***
	 * Gets the client's information map.
	 * @param player
	 * @return
	 */
	public static HashMap<String, Object> getClientInformationMap(Player player) {
		return PocketCon.getClientInformationMap(ProtocolSupportAPI.getConnection(player));
	}

}
