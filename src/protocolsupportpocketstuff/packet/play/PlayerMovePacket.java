package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class PlayerMovePacket extends PEPacket {
	private long entityId;
	private float x;
	private float y;
	private float z;
	private float pitch;
	private float headYaw;
	private float yaw;
	private int mode;
	private boolean onGround;

	public PlayerMovePacket(long entityId, float x, float y, float z, float pitch, float headYaw, float yaw, int mode, boolean onGround) {
		this.entityId = entityId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.headYaw = headYaw;
		this.yaw = yaw;
		this.mode = mode;
		this.onGround = onGround;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.PLAYER_MOVE;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeSVarLong(serializer, entityId);
		MiscSerializer.writeLFloat(serializer, x);
		MiscSerializer.writeLFloat(serializer, y);
		MiscSerializer.writeLFloat(serializer, z);
		MiscSerializer.writeLFloat(serializer, pitch);
		MiscSerializer.writeLFloat(serializer, headYaw);
		MiscSerializer.writeLFloat(serializer, yaw); //head yaw actually
		serializer.writeByte(mode);
		serializer.writeBoolean(onGround); //on ground
		VarNumberSerializer.writeVarLong(serializer, 0);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) { }
}