package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class TransferPacket extends PEPacket {
	private String address;
	private short port;

	public TransferPacket(String address, short port) {
		this.address = address;
		this.port = port;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.TRANSFER;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		StringSerializer.writeString(serializer, connection.getVersion(), address);
		serializer.writeShortLE(port);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) { }
}