package protocolsupportpocketstuff.api.util;

import protocolsupport.libs.com.google.gson.Gson;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.libs.com.google.gson.JsonParser;
import protocolsupport.libs.com.google.gson.stream.JsonReader;
import protocolsupport.protocol.typeremapper.pe.PESkin;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.skins.PocketSkinModel;
import protocolsupportpocketstuff.storage.Skins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.imageio.ImageIO;

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
	
	/***
	 * Gets from cache or downloads and caches a PocketSkin.
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static byte[] getOrDownloadAndCache(String url) throws MalformedURLException, IOException {
		Skins skins = Skins.INSTANCE;
		if (skins.hasPeSkin(url)) {
			System.out.println("from cache");
			return skins.getPeSkin(url);
		} else {
			byte[] skin = PESkin.toNetworkData(ImageIO.read(new URL(url)));
			skins.cachePeSkin(url, skin);
			System.out.println("from the WEB");
			return skin;
		}
	}
	
	/***
	 * Gets a skinModel from a local resource.
	 * @param name
	 * @return a PocketSkinModel object from file.
	 */
	public static PocketSkinModel getLocalSkinModel(String name) {
		return new Gson().fromJson(new JsonReader(readLocalModelFile(name)), PocketSkinModel.class);
	}
	
	/***
	 * Gets the default skinModel. 'CustomSlim' or 'Custom' aka 'Steve' or 'Alex'.
	 * @param slim
	 * @return the default skinModel
	 */
	public static PocketSkinModel getDefaultSkin(boolean slim) {
		return getLocalSkinModel(slim ? "CustomSlim" : "Custom");
	}
	
	private static BufferedReader readLocalModelFile(String name) {
		return new BufferedReader(new InputStreamReader(ProtocolSupportPocketStuff.class.getClassLoader().getResourceAsStream("resources/models/" + name + ".json"), StandardCharsets.UTF_8));
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
