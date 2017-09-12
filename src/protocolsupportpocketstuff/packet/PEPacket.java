package protocolsupportpocketstuff.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public abstract class PEPacket {

	public abstract int getPacketId();
	
	public abstract void encodePayload(Connection connection, ByteBuf serializer);
	
	public abstract void decodePayload(Connection connection, ByteBuf clientData);
	
	public ByteBuf encode(Connection connection) {
		ByteBuf serializer = Unpooled.buffer();
		VarNumberSerializer.writeVarInt(serializer, getPacketId());
		serializer.writeByte(0);
		serializer.writeByte(0);
		encodePayload(connection, serializer);
		return serializer;
	}
	
}
