package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.PositionSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.types.Position;
import protocolsupportpocketstuff.packet.PEPacket;

public class UpdateBlockPacket extends PEPacket {

	private Position position = new Position(0, 0, 0);
	private int blockId;

	public UpdateBlockPacket() { }

	public UpdateBlockPacket(Position position, int blockId) {
		this.position = position;
		this.blockId = blockId;
	}

	public UpdateBlockPacket(int x, int y, int z, int blockId) {
		this.position.setX(x);
		this.position.setY(y);
		this.position.setZ(z);
		this.blockId = blockId;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.UPDATE_BLOCK;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		PositionSerializer.writePEPosition(serializer, position);
		VarNumberSerializer.writeVarInt(serializer, blockId);
		VarNumberSerializer.writeVarInt(serializer, flags);
	}

	private static final int flag_update_neighbors = 0b0001;
	private static final int flag_network = 0b0010;
	private static final int flag_priority = 0b1000;
	private static final int flags = (flag_update_neighbors | flag_network | flag_priority);

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		throw new UnsupportedOperationException();
	}

}
