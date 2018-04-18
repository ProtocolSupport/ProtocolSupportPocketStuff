package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class EntityPickRequestPacket extends PEPacket {

	protected long entityId;
	protected byte slot;

	@Override
	public int getPacketId() {
		return PEPacketIDs.ENTITY_PICK_REQUEST;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeVarLong(serializer, entityId);
		serializer.writeByte(slot);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		entityId = VarNumberSerializer.readVarLong(clientdata);
		slot = clientdata.readByte();
	}

}
