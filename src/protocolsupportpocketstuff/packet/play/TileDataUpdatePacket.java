package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.ItemStackSerializer;
import protocolsupport.protocol.serializer.PositionSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.types.Position;
import protocolsupport.protocol.utils.types.nbt.NBTCompound;
import protocolsupportpocketstuff.packet.PEPacket;

public class TileDataUpdatePacket extends PEPacket {

	private Position position = new Position(0, 0, 0);
	private NBTCompound tag;

	public TileDataUpdatePacket() { }

	public TileDataUpdatePacket(Position position, NBTCompound tag) {
		this.position = position;
		this.tag = tag;
	}

	public TileDataUpdatePacket(int x, int y, int z, NBTCompound tag) {
		this.position.setX(x);
		this.position.setY(y);
		this.position.setZ(z);
		this.tag = tag;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.TILE_DATA_UPDATE;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		PositionSerializer.writePEPosition(serializer, position);
		//TODO: fix
		//ItemStackSerializer.writeTag(serializer, true, connection.getVersion(), tag);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		PositionSerializer.readPEPositionTo(clientdata, position);
		//TODO: fix
//		/this.tag = ItemStackSerializer.readTag(clientdata, true, connection.getVersion());
	}

	public Position getPosition() {
		return position;
	}

	public NBTCompound getTag() {
		return tag;
	}

}