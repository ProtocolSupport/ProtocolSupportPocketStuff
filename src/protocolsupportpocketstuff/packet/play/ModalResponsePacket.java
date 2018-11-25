package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class ModalResponsePacket extends PEPacket {

	private int modalId;
	private String modalJSON;

	public ModalResponsePacket() { }

	public ModalResponsePacket(int modalId, String modalJSON) {
		this.modalId = modalId;
		this.modalJSON = modalJSON;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.MODAL_RESPONSE;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		VarNumberSerializer.writeVarInt(serializer, modalId);
		StringSerializer.writeString(serializer, connection.getVersion(), modalJSON);
	}

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		modalId = VarNumberSerializer.readVarInt(clientdata);
		modalJSON = StringSerializer.readString(clientdata, connection.getVersion());
	}

	public int getModalId() {
		return modalId;
	}

	public String getModalJSON() {
		return modalJSON;
	}

}
