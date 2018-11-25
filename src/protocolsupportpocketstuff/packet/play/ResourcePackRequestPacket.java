package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class ResourcePackRequestPacket extends PEPacket {

	protected String packId;
	protected int chunkIndex;

	@Override
	public int getPacketId() {
		return PEPacketIDs.RESOURCE_REQUEST;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		this.packId = StringSerializer.readString(clientdata, connection.getVersion());
		this.chunkIndex = (int) clientdata.readUnsignedIntLE();
	}

	public String getPackId() {
		return packId;
	}

	public int getChunkIndex() {
		return chunkIndex;
	}

}
