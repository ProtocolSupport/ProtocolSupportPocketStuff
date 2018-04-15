package protocolsupportpocketstuff.api.resourcepacks;

public interface ResourcePack {

	/**
	 * Initialise the resourcepack.
	 */
	void init();

	/**
	 * @return the name of the resourcepack.
	 */
	String getName();

	/**
	 * @return the identifier of the resourcepack.
	 */
	String getPackId();

	/**
	 * @return the size of the resourcepack.
	 */
	int getPackSize();

	/**
	 * @return the version of the resourcepack.
	 */
	String getPackVersion();

	/**
	 * @return the Sha256 hash of the resourcepack.
	 */
	byte[] getSha256();

	/**
	 * @param chunkIdx
	 * @return gets a data section of the resourcepack.
	 */
	byte[] getPackChunk(int chunkIdx);

}
