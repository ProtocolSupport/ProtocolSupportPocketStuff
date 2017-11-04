package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class EntityDestroyPacket extends PEPacket {
	private long entityId;

	public EntityDestroyPacket(long entityId) {
		this.entityId = entityId;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.ENTITY_DESTROY;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeSVarLong(serializer, entityId);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) { }
}