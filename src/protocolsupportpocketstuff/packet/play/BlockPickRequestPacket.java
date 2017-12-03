package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.types.Position;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.hacks.middleclick.PickRequester;
import protocolsupportpocketstuff.packet.PEPacket;

public class BlockPickRequestPacket extends PEPacket {

	protected Position position = new Position(0, 0, 0);
	protected boolean huh;
	protected byte slot;
	
	@Override
	public int getPacketId() {
		return PEPacketIDs.BLOCK_PICK_REQUEST;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeSVarInt(serializer, position.getX());
		VarNumberSerializer.writeSVarInt(serializer, position.getY());
		VarNumberSerializer.writeSVarInt(serializer, position.getZ());
		serializer.writeBoolean(huh);
		serializer.writeByte(slot);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		position.setX(VarNumberSerializer.readSVarInt(clientdata));
		position.setY(VarNumberSerializer.readSVarInt(clientdata));
		position.setZ(VarNumberSerializer.readSVarInt(clientdata));
		huh = clientdata.readBoolean();
		slot = clientdata.readByte();
	}
	
	public class decodeHandler extends PEPacket.decodeHandler {

		public decodeHandler(ProtocolSupportPocketStuff plugin, Connection connection) {
			super(plugin, connection);
		}

		@Override
		public void handle() {
			BlockPickRequestPacket parent = BlockPickRequestPacket.this;
			System.out.println("BlockPICK!: " + parent.position + " slot: " + parent.slot);
			PickRequester.getInstance().handleBlockPick(connection.getPlayer(), position);
		}
		
	}

}
