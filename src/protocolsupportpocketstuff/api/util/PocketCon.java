package protocolsupportpocketstuff.api.util;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolType;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.skins.PocketSkin;
import protocolsupportpocketstuff.packet.ModalRequestPacket;
import protocolsupportpocketstuff.packet.PePacket;
import protocolsupportpocketstuff.storage.Modals;

public class PocketCon {
	
    //=====================================================\\
    //						Getting						   \\
    //=====================================================\\

	/***
	 * Checks if a connection is a pocket connection.
	 * @param player
	 * @return the truth
	 */
	public static boolean isPocketConnection(Connection connection) {
		return connection.getVersion().getProtocolType().equals(ProtocolType.PE);
	}
	
	/**
	 * Gets all pocket connections on the server.
	 * @return
	 */
	public static Collection<? extends Connection> getPocketConnections() {
		return ProtocolSupportAPI.getConnections().stream().filter(pocketFilter()).collect(Collectors.toList());
	}
	
	/***
	 * Filter to filter PE connections.
	 * @return
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
		return sendModal(connection, Modals.INSTANCE.takeId(), modal.toJSON()); 
	}
	
	/***
	 * Sends a modal with an id specified.
	 * Nonono, don't use custom ids!
	 * If you like you can use this function in combination with
	 * {@link Modals.INSTANCE.takeId} to send custom JSON to the player.
	 * @param id
	 * @param modal
	 * @return
	 */
	public static int sendModal(Connection connection, int id, String modalJSON) {
		PocketCon.sendPocketPacket(connection, new ModalRequestPacket(id, modalJSON)); return id;
	}
	
	/***
	 * Sends a PocketSkin to a pocket connection.
	 * @param connection
	 * @param skin
	 */
	public static void sendSkin(Connection connection, PocketSkin skin) {
		sendPocketPacket(connection, skin.getPacket());
	}
	
	/***
	 * Sends a packet to pocket.
	 * @param connection
	 * @param packet
	 */
	public static void sendPocketPacket(Connection connection, PePacket packet) {
		connection.sendRawPacket(MiscSerializer.readAllBytes(packet.encode(connection)));
	}
	
}
