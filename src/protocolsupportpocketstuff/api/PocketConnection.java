package protocolsupportpocketstuff.api;

import org.bukkit.entity.Player;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.ModalRequest;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.modals.Modals;

public abstract class PocketConnection extends Connection {
	
	public static PocketConnection get(Player player) {
		return (PocketConnection) ProtocolSupportAPI.getConnection(player);
	}
	
	/***
	 * Sends a modal and gets the corresponding id.
	 * @param modal
	 * @return
	 */
	public int sendModal(Modal modal) {
		int id = Modals.INSTANCE.takeId(); sendModal(id, modal); return id;
	}
	
	/**
	 * Sends a modal with a id.
	 * Nonono custom ids!
	 * @param id
	 * @param modal
	 */
	@Deprecated
	public void sendModal(int id, Modal modal) {
		sendPocketPacket(ModalRequest.create(id, modal.getModalString(), version));
	}
	
	/***
	 * Sends a custom pocket PS packet to the player.
	 * @param data
	 */
	public void sendPocketPacket(ClientBoundPacketData data) {
		sendRawPacket(MiscSerializer.readAllBytes(data));
	}
	
}
