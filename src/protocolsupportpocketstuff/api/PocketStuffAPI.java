package protocolsupportpocketstuff.api;

import java.math.BigInteger;

import protocolsupportpocketstuff.api.resourcepacks.ResourcePackManager;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.PEReceiver;

public class PocketStuffAPI {

	private static final BigInteger apiversion = BigInteger.valueOf(6);
	private static ResourcePackManager resourcePackManager;

	/***
	 * Get's PocketStuff's API version. This number is incremented on every dev release.
	 * @return API version.
	 */
	public static BigInteger getAPIVersion() {
		return apiversion;
	}

	public static ResourcePackManager getResourcePackManager() {
		return resourcePackManager;
	}

	public static void setResourcePackManager(ResourcePackManager resourcePackManager) {
		PocketStuffAPI.resourcePackManager = resourcePackManager;
	}

	/**
	 * Registers all pocket packet handlers in a pocket packet listener.
	 * @param listener
	 */
	@SuppressWarnings("deprecation")
	public static void registerPacketListeners(PocketPacketListener listener) {
		PEReceiver.registerPacketListeners(listener);
	}

}
