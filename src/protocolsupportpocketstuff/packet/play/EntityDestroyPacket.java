package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class EntityDestroyPacket extends PEPacket {

	private long entityId;

	public EntityDestroyPacket() { }

	public EntityDestroyPacket(long entityId) {
		this.entityId = entityId;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.ENTITY_DESTROY;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		VarNumberSerializer.writeSVarLong(serializer, entityId);
	}

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		this.entityId = VarNumberSerializer.readSVarLong(clientdata);
	}

	public long getEntityId() {
		return entityId;
	}

}