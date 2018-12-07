package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class ResourcePackChunkDataPacket extends PEPacket {

	private String packId;
	private int chunkIdx;
	private byte[] packChunk;

	public ResourcePackChunkDataPacket() { }

	public ResourcePackChunkDataPacket(String packId, int chunkIdx, byte[] packChunk) {
		this.packId = packId;
		this.chunkIdx = chunkIdx;
		this.packChunk = packChunk;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.RESOURCE_DATA;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, packId);
		serializer.writeIntLE(chunkIdx);
		serializer.writeLongLE(CHUNK_SIZE * chunkIdx);
		serializer.writeIntLE(packChunk.length);
		serializer.writeBytes(packChunk);
	}

	public static final int CHUNK_SIZE = 1048576;

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		throw new UnsupportedOperationException();
	}

}
