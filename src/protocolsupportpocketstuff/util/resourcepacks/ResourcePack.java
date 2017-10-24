package protocolsupportpocketstuff.util.resourcepacks;

public interface ResourcePack {
	String getName();

	String getPackId();

	int getPackSize();

	String getPackVersion();

	byte[] getSha256();

	byte[] getPackChunk(int chunkIdx);
}
