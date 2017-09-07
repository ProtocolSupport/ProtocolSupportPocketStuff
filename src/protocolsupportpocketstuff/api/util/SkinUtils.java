package protocolsupportpocketstuff.api.util;

import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.libs.com.google.gson.JsonParser;

import java.util.Base64;

public class SkinUtils {
	/***
	 * Gets the SkinDataWrapper from a base64 encoded skin property
	 * @param property
	 * @return an wrapper containing the skin URL and the skin metadata
	 */
	public static SkinDataWrapper readSkinDataFromProperty(String property) {
		// readSkinDataFromProperty("eyJ0aW1lc3RhbXAiOjE1MDQ4MDUzODE0NDUsInByb2ZpbGVJZCI6IjdjZjc2MTFkYmY2YjQxOWRiNjlkMmQzY2Q4NzUxZjRjIiwicHJvZmlsZU5hbWUiOiJrYXJldGg5OTkiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifSwidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NTEzOWNmM2EwMmY2MmUxYWY5ZjZiYWQxNDc5ZTZjNDdjNTQ0ZTFlZDNkNjlmMThmMTVkNjkyYmM4ZDI0ZTkifX19");
		// First we are going to base64 decode the property
		byte[] decodedByteArray = Base64.getDecoder().decode(property);
		String decoded = new String(decodedByteArray);

		JsonObject element = new JsonParser().parse(decoded).getAsJsonObject();

		JsonObject skinObject = element.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject();

		String skinUrl = skinObject.get("url").getAsString(); // Skin URL
		boolean isSlim = skinObject.has("metadata") && skinObject.get("metadata").getAsJsonObject().has("model") && skinObject.get("metadata").getAsJsonObject().get("model").getAsString().equals("slim");

		return new SkinDataWrapper(skinUrl, isSlim);
	}

	static class SkinDataWrapper {
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
