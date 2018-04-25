package protocolsupportpocketstuff.api.util;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolType;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.typeremapper.pe.PESkinModel;
import protocolsupportpocketstuff.api.event.ModalResponseEvent;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.modals.ModalType;
import protocolsupportpocketstuff.api.modals.ModalUtils;
import protocolsupportpocketstuff.api.skins.PocketSkinModel;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.packet.play.ModalRequestPacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.packet.play.TransferPacket;
import protocolsupportpocketstuff.storage.Modals;
import protocolsupportpocketstuff.util.StuffUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/***
 * Utility to send and retrieve pocket-specific things to a {@link Connection}.
 */
public class PocketCon {
	
    //=====================================================\\
    //						Getting						   \\
    //=====================================================\\

	/***
	 * Checks if a connection is a pocket connection.
	 * @param player
	 * @return the truth.
	 */
	public static boolean isPocketConnection(Connection connection) {
		return connection.getVersion().getProtocolType().equals(ProtocolType.PE);
	}
	
	/**
	 * Gets all pocket connections on the server.
	 * @return all pocket connections.
	 */
	public static Collection<? extends Connection> getPocketConnections() {
		return ProtocolSupportAPI.getConnections().stream().filter(pocketFilter()).collect(Collectors.toList());
	}
	
	/***
	 * Filter to filter PE connections.
	 * @return the truth is a predicate.
	 */
	public static Predicate<Connection> pocketFilter() {
		return c -> isPocketConnection(c);
	}
	
    //=====================================================\\
    //						Packets						   \\
    //=====================================================\\
	
	/***
	 * Sends a modal and gets the corresponding id.
	 * The id can be used to track response of this modal in events.
	 * @param modal
	 * @return the id of the modal.
	 */
	public static int sendModal(Connection connection, Modal modal) {
		return sendModal(connection, Modals.getInstance().takeId(), modal.toJSON(), null);
	}

	/***
	 * Sends a modal and gets the corresponding id.
	 * The id can be used to track response of this modal in events.
	 * The callback is called when the player completes a modal
	 * and all modaleventhandlers are done. 
	 * @param connection
	 * @param modal
	 * @param callback
	 * @return the id of the modal.
	 */
	public static int sendModal(Connection connection, Modal modal, Consumer<ModalResponseEvent> callback) {
		return sendModal(connection, Modals.getInstance().takeId(), modal.toJSON(), callback);
	}

	/***
	 * Sends a modal with an id specified.
	 * <em>Nonono, don't use custom ids!</em>
	 * If you like you can use this function in combination with
	 * {@link Modals.INSTANCE.takeId} to send custom JSON to the player.
	 * @param id
	 * @param modalJSON
	 * @return the id of the modal.
	 */
	public static int sendModal(Connection connection, int id, String modalJSON) {
		sendModal(connection, id, modalJSON, null);
		return id;
	}

	/***
	 * Sends a modal with an id specified.
	 * <em>Nonono, don't use custom ids!</em>
	 * If you like you can use this function in combination with
	 * {@link Modals.INSTANCE.takeId} to send custom JSON to the player.
	 * This method also registers specified callback (if not null)
	 * which is called after modal is completed and events are handled.
	 * @param id
	 * @param modalJSON
	 * @param callback
	 * @return the id of the modal.
	 */
	public static int sendModal(Connection connection, int id, String modalJSON, Consumer<ModalResponseEvent> callback) {
		if (callback != null) { ModalUtils.setCallback(connection, callback); }
		ModalUtils.setSendType(connection, ModalType.fromModal(modalJSON));
		sendPocketPacket(connection, new ModalRequestPacket(id, modalJSON));
		return id;
	}

	/**
	 * Sends a pc-like skin to a pocket connection.
	 * @param connection
	 * @param uuid
	 * @param skin
	 * @param isSlim
	 */
	public static void sendSkin(Connection connection, UUID uuid, byte[] skin, boolean isSlim) {
		sendSkin(connection, uuid, skin, PocketSkinModel.fromPEModel(PESkinModel.getSkinModel(isSlim)));
	}

	/***
	 * Sends a PocketSkin to a pocket connection.
	 * @param connection
	 * @param uuid
	 * @param skin
	 * @param skinModel
	 */
	public static void sendSkin(Connection connection, UUID uuid, byte[] skin, PocketSkinModel skinModel) {
		//TODO: "Steve" is actually a hack. The name send should be the previous skin name. Not sure if this matters though. Works for now :S"
		sendPocketPacket(connection, new SkinPacket(uuid, skinModel.getSkinId(), skinModel.getSkinName(), "Steve", skin, new byte[0], skinModel.getGeometryId(), skinModel.getGeometryData()));
	}

	/***
	 * Transfers a player to another server
	 * @param connection
	 * @param address
	 * @param port
	 */
	public static void transfer(Connection connection, String address, short port) {
		sendPocketPacket(connection, new TransferPacket(address, port));
	}

	/***
	 * Sends a packet to pocket.
	 * @param connection
	 * @param packet
	 */
	public static void sendPocketPacket(Connection connection, PEPacket packet) {
		connection.sendRawPacket(MiscSerializer.readAllBytes(packet.encode(connection)));
	}

    //=====================================================\\
    //						Client-Info					   \\
    //=====================================================\\

	/***
	 * Gets the client's UUID. Used for updating skin of the self player or other UUID specific things.
	 * @param connection
	 * @return
	 */
	public static UUID getClientUniqueId(Connection connection) {
		return (UUID) getClientInformationMap(connection).get("UUID");
	}

	/***
	 * Gets the client random ID assigned upon install to the user. This can be edited by the client, so beware!
	 * @param connection
	 * @return client's random id
	 */
	public static long getClientRandomId(Connection connection) {
		return (Long) getClientInformationMap(connection).get("ClientRandomId");
	}

	/***
	 * Gets the client device model
	 * @param connection
	 * @return client's device model
	 */
	public static String getDeviceModel(Connection connection) {
		return (String) getClientInformationMap(connection).get("DeviceModel");
	}

	/***
	 * Gets the client operating system
	 * @param connection
	 * @return client's operating system
	 */
	public static DeviceOperatingSystem getOperatingSystem(Connection connection) {
		return DeviceOperatingSystem.getOperatingSystemById((int) getClientInformationMap(connection).get("DeviceOS"));
	}

	/***
	 * Gets the client version
	 * @param connection
	 * @return client version
	 */
	public static String getClientVersion(Connection connection) {
		return (String) getClientInformationMap(connection).get("GameVersion");
	}

	/***
	 * Gets the client's information map.
	 * @param connection
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Object> getClientInformationMap(Connection connection) {
		return (HashMap<String, Object>) connection.getMetadata(StuffUtils.CLIENT_INFO_KEY);
	}

}
