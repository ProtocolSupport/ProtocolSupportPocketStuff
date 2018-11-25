package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class TransferPacket extends PEPacket {

	private String address;
	private short port;

	public TransferPacket() { }

	public TransferPacket(String address, short port) {
		this.address = address;
		this.port = port;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.TRANSFER;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		StringSerializer.writeString(serializer, connection.getVersion(), address);
		serializer.writeShortLE(port);
	}

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		throw new UnsupportedOperationException();
	}

}