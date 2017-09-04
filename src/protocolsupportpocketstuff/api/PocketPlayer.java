package protocolsupportpocketstuff.api;

import org.bukkit.entity.Player;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;

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
		//TODO: I don't know howWWwwww.. someone controlled you.. they bought and sold you..
		//connection.sendRawPacket(ModalRequest.create(id, Modals.get(id), connection.getVersion()).unwrap().array());
	}
}
