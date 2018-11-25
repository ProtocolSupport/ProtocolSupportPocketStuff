package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.api.resourcepacks.ResourcePack;
import protocolsupportpocketstuff.packet.PEPacket;

public class ResourcePackDataInfoPacket extends PEPacket {

	private ResourcePack pack;

	public ResourcePackDataInfoPacket() { }

	public ResourcePackDataInfoPacket(ResourcePack pack) {
		this.pack = pack;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.RESOURCE_INFO;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, pack.getPackId());
		serializer.writeIntLE(ResourcePackChunkDataPacket.CHUNK_SIZE); //max chunk size 1MB
		serializer.writeIntLE((int) Math.ceil(pack.getPackSize() / ResourcePackChunkDataPacket.CHUNK_SIZE)); //chunk count
		serializer.writeLongLE(pack.getPackSize()); //res pack size
		byte[] hash = pack.getSha256();
		ArraySerializer.writeVarIntByteArray(serializer, hash);
	}

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		throw new UnsupportedOperationException();
	}

}
