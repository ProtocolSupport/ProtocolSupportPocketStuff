package protocolsupportpocketstuff.api.util;

import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.libs.com.google.gson.JsonParser;

import java.util.Base64;

public class SkinUtils {
	
	public static final String skinPropertyName = "textures";
	
	/***
	 * Gets the SkinDataWrapper from a base64 encoded skin property
	 * @param property
	 * @return an wrapper containing the skin URL and the skin metadata
	 */
	public static SkinDataWrapper readSkinDataFromProperty(String property) {
		byte[] decodedByteArray = Base64.getDecoder().decode(property);
		String decoded = new String(decodedByteArray);
		JsonObject element = new JsonParser().parse(decoded).getAsJsonObject();
		JsonObject skinObject = element.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject();
		String skinUrl = skinObject.get("url").getAsString(); // Skin URL
		boolean isSlim = skinObject.has("metadata") && skinObject.get("metadata").getAsJsonObject().has("model") && skinObject.get("metadata").getAsJsonObject().get("model").getAsString().equals("slim");
		return new SkinDataWrapper(skinUrl, isSlim);
	}

	public static class SkinDataWrapper {
		
		private String skinUrl;
		private boolean isSlim;

		public SkinDataWrapper(String skinUrl, boolean isSlim) {
			this.skinUrl = skinUrl;
			this.isSlim = isSlim;
		}

		public String getSkinUrl() {
			return skinUrl;
		}

		public boolean isSlim() {
			return isSlim;
		}
		
	}
	
}
