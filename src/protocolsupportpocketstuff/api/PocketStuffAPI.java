package protocolsupportpocketstuff.api;

import java.math.BigInteger;

import protocolsupportpocketstuff.api.resourcepacks.ResourcePackManager;

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
}
