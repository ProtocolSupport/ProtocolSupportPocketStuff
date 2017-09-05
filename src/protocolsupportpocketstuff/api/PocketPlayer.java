package protocolsupportpocketstuff.api;

import org.bukkit.entity.Player;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.ModalRequest;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.modals.Modals;

public class PocketPlayer {
	private Player player;
	private Connection connection;
	
	public PocketPlayer(Player player) {
		this.player = player;
		this.connection = ProtocolSupportAPI.getConnection(player);
	}
	
	/***
	 * Gets the bukkit Player.
	 * @return Player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/***
	 * Gets the player's PS connection.
	 * @return Connection
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/***
	 * Sends a PocketPlayer a modal using it's id.
	 * @param id
	 */
	public void sendModal(int id) {
		sendModal(id, Modals.get(id));
	}
	
	/***
	 * Sends a PocketPlayer a dynamic modal. 
	 * (Id is still required for callback!)
	 * @param id
	 * @param modal
	 */
	public void sendModal(int id, Modal modal) {
		sendPocketPacket(ModalRequest.create(id, modal.getModalString(), connection.getVersion()));
	}
	
	/***
	 * Sends a custom pocket PS packet to the player.
	 * @param data
	 */
	public void sendPocketPacket(ClientBoundPacketData data) {
		connection.sendRawPacket(MiscSerializer.readAllBytes(data));
	}
	
}
