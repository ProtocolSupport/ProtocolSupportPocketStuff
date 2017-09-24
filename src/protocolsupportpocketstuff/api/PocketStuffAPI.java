package protocolsupportpocketstuff.api;

import java.math.BigInteger;

public class PocketStuffAPI {

	private static final BigInteger apiversion = BigInteger.valueOf(3);
	
	/***
	 * Get's PocketStuff's API version. This number is incremented on every dev release.
	 * @return API version.
	 */
	public static BigInteger getAPIVersion() {
		return apiversion;
	}
	
}
