package protocolsupportpocketstuff.packet;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;

public class ModalRequestPacket extends PePacket {

	private int modalId;
	private String modalJSON;
	
	public ModalRequestPacket() { }
	
	public ModalRequestPacket(int modalId, String modalJSON) {
		this.modalId = modalId;
		this.modalJSON = modalJSON;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.MODAL_REQUEST;
	}

	@Override
	public void encodePayload(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeVarInt(serializer, modalId);
		StringSerializer.writeString(serializer, connection.getVersion(), modalJSON);
	}

	@Override
	public void decodePayload(Connection connection, ByteBuf clientData) {
		modalId = VarNumberSerializer.readVarInt(clientData);
		modalJSON = StringSerializer.readString(clientData, connection.getVersion());
	}
	
}
