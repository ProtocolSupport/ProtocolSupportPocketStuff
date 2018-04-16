package protocolsupportpocketstuff.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocolsupport.api.Connection;
import protocolsupport.protocol.pipeline.version.v_pe.PEPacketEncoder;
import protocolsupportbuildprocessor.annotations.NeedsNoArgConstructor;

@NeedsNoArgConstructor
public abstract class PEPacket {

	public abstract int getPacketId();

	public abstract void toData(Connection connection, ByteBuf serializer);

	public abstract void readFromClientData(Connection connection, ByteBuf clientdata);
	
	public ByteBuf encode(Connection connection) {
		ByteBuf serializer = Unpooled.buffer();
		PEPacketEncoder.sWritePacketId(serializer, getPacketId());
		toData(connection, serializer);
		return serializer;
	}

	public void decode(Connection connection, ByteBuf clientdata) {
		readFromClientData(connection, clientdata);
	}

}
