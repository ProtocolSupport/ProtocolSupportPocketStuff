package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import org.bukkit.World.Environment;
import org.bukkit.util.Vector;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class DimensionPacket extends PEPacket {

	private Environment enviroment;
	private Vector position;
	
	public DimensionPacket() { }
	
	public DimensionPacket(Environment environment, Vector position) {
		this.enviroment = environment;
		this.position = position;
	}
	
	@Override
	public int getPacketId() {
		return PEPacketIDs.CHANGE_DIMENSION;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeSVarInt(serializer, toPocketDimension(enviroment));
		serializer.writeFloat((float) position.getX());
		serializer.writeFloat((float) position.getY());
		serializer.writeFloat((float) position.getZ());
		serializer.writeBoolean(true); //unused value
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) {	}
	
	private static int toPocketDimension(Environment dimId) {
		switch (dimId) {
			case NETHER: {
				return 1;
			}
			case THE_END: {
				return 2;
			}
			case NORMAL: {
				return 0;
			}
			default: {
				throw new IllegalArgumentException(String.format("Unknown dim id %s", dimId));
			}
		}
	}

}
