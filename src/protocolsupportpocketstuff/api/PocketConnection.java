package protocolsupportpocketstuff.api;

import org.bukkit.entity.Player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.ModalRequest;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.modals.Modals;

public class PocketConnection {
	
	private Connection connection;
	
	public PocketConnection(Connection connection) {
		this.connection = connection;
	}

	public static PocketConnection get(Player player) {
		return new PocketConnection(ProtocolSupportAPI.getConnection(player));
	}
	
	/***
	 * Sends a modal and gets the corresponding id.
	 * @param modal
	 * @return
	 */
	public int sendModal(Modal modal) {
		int id = Modals.INSTANCE.takeId(); sendModal(id, modal.toJSON()); return id;
	}
	
	/**
	 * Sends a modal with a id.
	 * Nonono custom ids!
	 * @param id
	 * @param modal
	 */
	@Deprecated
	public void sendModal(int id, String modalJSON) {
		sendPocketPacket(ModalRequest.create(id, modalJSON, connection.getVersion()));
	}
	
	/***
	 * Sends a custom pocket PS packet to the player.
	 * @param data
	 */
	public void sendPocketPacket(ClientBoundPacketData data) {
		ByteBuf serializer = Unpooled.buffer();
		VarNumberSerializer.writeVarInt(serializer, data.getPacketId());
		serializer.writeByte(0);
		serializer.writeByte(0);
		serializer.writeBytes(data);
		connection.sendRawPacket(serializer.array());
	}
	
}
