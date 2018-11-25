package protocolsupportpocketstuff.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.pipeline.version.v_pe.PEPacketEncoder;

public abstract class PEPacket {

	public abstract int getPacketId();

	public abstract void toData(ConnectionImpl connection, ByteBuf serializer);

	public abstract void readFromClientData(ConnectionImpl connection, ByteBuf clientdata);
	
	public ByteBuf encode(ConnectionImpl connection) {
		ByteBuf serializer = Unpooled.buffer();
		PEPacketEncoder.sWritePacketId(serializer, getPacketId());
		toData(connection, serializer);
		return serializer;
	}

	public void decode(ConnectionImpl connection, ByteBuf clientdata) {
		readFromClientData(connection, clientdata);
	}

}
