package protocolsupportpocketstuff.api.util;

import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.skins.PocketSkin;
import protocolsupportpocketstuff.packet.ModalRequestPacket;
import protocolsupportpocketstuff.packet.PePacket;
import protocolsupportpocketstuff.storage.Modals;
import protocolsupportpocketstuff.storage.Skins;

public class PocketCon {
	
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
	
	/***
	 * Sends a modal with a id.
	 * Nonono custom ids!
	 * @param id
	 * @param modal
	 */
	@Deprecated
	public static void sendModal(Connection connection, int id, String modalJSON) {
		PocketCon.sendPocketPacket(connection, new ModalRequestPacket(id, modalJSON));
	}
	
	/***
	 * Sends all cached skins to a pocket connection.
	 * @param connection
	 */
	public static void sendSkins(Connection connection) {
		for(PocketSkin peSkin : Skins.INSTANCE.getPeSkins().values()) {
			sendSkin(connection, peSkin);
		}
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
