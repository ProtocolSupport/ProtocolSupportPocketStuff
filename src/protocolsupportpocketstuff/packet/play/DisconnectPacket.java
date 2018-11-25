package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class DisconnectPacket extends PEPacket {

	private boolean hide;
	private String reason;

	public DisconnectPacket() { }

	public DisconnectPacket(boolean hide, String reason) {
		this.hide = hide;
		this.reason = reason;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.DISCONNECT;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		serializer.writeBoolean(hide);
		StringSerializer.writeString(serializer, connection.getVersion(), reason);
	}

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		this.hide = clientdata.readBoolean();
		this.reason = StringSerializer.readString(clientdata, connection.getVersion());
	}

	public boolean getHide() {
		return hide;
	}

	public String getReason() {
		return reason;
	}
}
