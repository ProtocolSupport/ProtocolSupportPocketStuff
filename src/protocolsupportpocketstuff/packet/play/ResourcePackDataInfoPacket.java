package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.resourcepacks.ResourcePack;

public class ResourcePackDataInfoPacket extends PEPacket {
	private ResourcePack pack;

	public ResourcePackDataInfoPacket(ResourcePack pack) {
		this.pack = pack;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.RESOURCE_INFO;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, pack.getPackId());
		serializer.writeIntLE(1048576); // max chunk size 1MB
		serializer.writeIntLE(pack.getPackSize() / 1048576); // chunk count
		serializer.writeLongLE(pack.getPackSize()); // res pack size

		byte[] hash = pack.getSha256();

		VarNumberSerializer.writeVarInt(serializer, hash.length);
		for (byte b : hash) {
			serializer.writeByte(b);
		}
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) { }
}
