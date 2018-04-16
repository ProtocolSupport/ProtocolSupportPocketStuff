package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupportpocketstuff.packet.PEPacket;

public class BossEventPacket extends PEPacket {

	private long entityId;
	private int eventId;

	public static final int SHOW = 0;
	public static final int UPDATE = 1;
	public static final int REMOVE = 2;

	public BossEventPacket() { }

	public BossEventPacket(long entityId, int eventId) {
		this.entityId = entityId;
		this.eventId = eventId;
	}

	@Override
	public int getPacketId() {
		return 74;
	} // Boss Event Packet

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeSVarLong(serializer, entityId);
		VarNumberSerializer.writeVarInt(serializer, eventId);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) { }

}