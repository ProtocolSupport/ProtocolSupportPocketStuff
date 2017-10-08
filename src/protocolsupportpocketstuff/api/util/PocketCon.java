package protocolsupportpocketstuff.api.util;

import org.bukkit.World.Environment;
import org.bukkit.util.Vector;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolType;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupportpocketstuff.api.event.ComplexFormResponseEvent;
import protocolsupportpocketstuff.api.event.ModalResponseEvent;
import protocolsupportpocketstuff.api.event.ModalWindowResponseEvent;
import protocolsupportpocketstuff.api.event.SimpleFormResponseEvent;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.modals.callback.ComplexFormCallback;
import protocolsupportpocketstuff.api.modals.callback.ModalCallback;
import protocolsupportpocketstuff.api.modals.callback.ModalWindowCallback;
import protocolsupportpocketstuff.api.modals.callback.SimpleFormCallback;
import protocolsupportpocketstuff.api.skins.PocketSkinModel;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.packet.play.DimensionPacket;
import protocolsupportpocketstuff.packet.play.ModalRequestPacket;
import protocolsupportpocketstuff.packet.play.SkinPacket;
import protocolsupportpocketstuff.storage.Modals;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
	 * @param modal
	 * @return
	 */
	public static int sendModal(Connection connection, Modal modal) {
		return sendModal(connection, Modals.INSTANCE.takeId(), modal.toJSON(), null);
	}

	public static int sendModal(Connection connection, Modal modal, ModalCallback callback) {
		return sendModal(connection, Modals.INSTANCE.takeId(), modal.toJSON(), callback);
	}

	/***
	 * Sends a modal with an id specified.
	 * Nonono, don't use custom ids!
	 * If you like you can use this function in combination with
	 * {@link Modals.INSTANCE.takeId} to send custom JSON to the player.
	 * @param id
	 * @param modal
	 * @return the modal's callback id.
	 */
	public static int sendModal(Connection connection, int id, String modalJSON) {
		sendModal(connection, id, modalJSON, null);
		return id;
	}

	public static int sendModal(Connection connection, int id, String modalJSON, ModalCallback callback) {
		if (callback != null)
			addCallback(connection, id, callback);

		sendPocketPacket(connection, new ModalRequestPacket(id, modalJSON));
		return id;
	}

	public static void addCallback(Connection connection, int id, ModalCallback callback) {
		connection.addMetadata("modalCallback", callback);
	}

	public static ModalCallback getCallback(Connection connection) {
		return (ModalCallback) connection.getMetadata("modalCallback");
	}

	public static void removeCallback(Connection connection) {
		connection.removeMetadata("modalCallback");
	}

	public static void handleModalResponse(Connection connection, ModalResponseEvent event) {
		ModalCallback modalCallback = PocketCon.getCallback(connection);
		if (modalCallback == null)
			return;

		modalCallback.onModalResponse(connection.getPlayer(), event.getModalJSON());
		if (event instanceof SimpleFormResponseEvent) {
			SimpleFormCallback simpleFormCallback = (SimpleFormCallback) modalCallback;
			simpleFormCallback.onSimpleFormResponse(connection.getPlayer(), event.getModalJSON(), ((SimpleFormResponseEvent) event).getClickedButton());
		} else if (event instanceof ComplexFormResponseEvent) {
			ComplexFormCallback complexFormCallback = (ComplexFormCallback) modalCallback;
			complexFormCallback.onComplexFormResponse(connection.getPlayer(), event.getModalJSON(), ((ComplexFormResponseEvent) event).getJsonArray());
		} else if (event instanceof ModalWindowResponseEvent) {
			ModalWindowCallback modalWindowResponseEvent = (ModalWindowCallback) modalCallback;
			modalWindowResponseEvent.onModalWindowResponse(connection.getPlayer(), event.getModalJSON(), ((ModalWindowResponseEvent) event).getResult());
		}

		PocketCon.removeCallback(connection);
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
	 * Sends a dimension change to a pocket connection.
	 * @param connection
	 * @param environment
	 * @param location
	 */
	public static void sendDimensionChange(Connection connection, Environment environment, Vector location) {
		sendPocketPacket(connection, new DimensionPacket(environment, location));
	}
	
	/***
	 * Sends a packet to pocket.
	 * @param connection
	 * @param packet
	 */
	public static void sendPocketPacket(Connection connection, PEPacket packet) {
		connection.sendRawPacket(MiscSerializer.readAllBytes(packet.encode(connection)));
	}
	
}
