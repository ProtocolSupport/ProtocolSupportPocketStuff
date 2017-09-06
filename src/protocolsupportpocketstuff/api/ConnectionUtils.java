package protocolsupportpocketstuff.api;

import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.modals.Modals;
import protocolsupportpocketstuff.packet.ModalRequestPacket;
import protocolsupportpocketstuff.packet.PePacket;

public class ConnectionUtils {
	
	/***
	 * Sends a modal and gets the corresponding id.
	 * @param modal
	 * @return
	 */
	public static int sendModal(Connection connection, Modal modal) {
		int id = Modals.INSTANCE.takeId(); 
		sendModal(connection, id, modal.toJSON()); 
		return id;
	}
	
	/**
	 * Sends a modal with a id.
	 * Nonono custom ids!
	 * @param id
	 * @param modal
	 */
	@Deprecated
	public static void sendModal(Connection connection, int id, String modalJSON) {
		ConnectionUtils.sendPocketPacket(connection, new ModalRequestPacket(id, modalJSON));
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
