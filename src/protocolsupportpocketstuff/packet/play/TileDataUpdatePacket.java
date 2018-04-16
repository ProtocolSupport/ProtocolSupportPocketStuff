package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.ItemStackSerializer;
import protocolsupport.protocol.serializer.PositionSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.types.Position;
import protocolsupport.zplatform.itemstack.NBTTagCompoundWrapper;
import protocolsupportpocketstuff.packet.PEPacket;

public class TileDataUpdatePacket extends PEPacket {

	private Position position = new Position(0, 0, 0);
	private NBTTagCompoundWrapper tag;

	public TileDataUpdatePacket() { }

	public TileDataUpdatePacket(Position position, NBTTagCompoundWrapper tag) {
		this.position = position;
		this.tag = tag;
	}

	public TileDataUpdatePacket(int x, int y, int z, NBTTagCompoundWrapper tag) {
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
		ItemStackSerializer.writeTag(serializer, true, connection.getVersion(), tag);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		PositionSerializer.readPEPositionTo(clientdata, position);
		this.tag = ItemStackSerializer.readTag(clientdata, true, connection.getVersion());
	}

	public Position getPosition() {
		return position;
	}

	public NBTTagCompoundWrapper getTag() {
		return tag;
	}

}