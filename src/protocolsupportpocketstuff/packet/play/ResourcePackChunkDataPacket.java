package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.util.StuffUtils;

public class ResourcePackChunkDataPacket extends PEPacket {
	private String packId;
	private int chunkIdx;
	private byte[] packChunk;

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
	public void toData(Connection connection, ByteBuf serializer) {
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, packId);
		serializer.writeIntLE(chunkIdx);
		serializer.writeLongLE(StuffUtils.CHUNK_SIZE * chunkIdx);
		serializer.writeIntLE(packChunk.length);
		serializer.writeBytes(packChunk);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) { }
}
