package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.PositionSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.types.Position;
import protocolsupportpocketstuff.packet.PEPacket;

public class UpdateBlockPacket extends PEPacket {
	private int x;
	private int y;
	private int z;
	private int blockId;
	private int meta;
	private static final int flag_update_neighbors = 0b0001;
	private static final int flag_network = 0b0010;
	private static final int flag_priority = 0b1000;

	private static final int flags = (flag_update_neighbors | flag_network | flag_priority);

	public UpdateBlockPacket(int x, int y, int z, int blockId, int meta) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockId = blockId;
		this.meta = meta;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.UPDATE_BLOCK;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		PositionSerializer.writePEPosition(serializer, new Position(x, y, z));
		VarNumberSerializer.writeVarInt(serializer, blockId);
		VarNumberSerializer.writeVarInt(serializer, (flags << 4) | meta);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) { }
}
