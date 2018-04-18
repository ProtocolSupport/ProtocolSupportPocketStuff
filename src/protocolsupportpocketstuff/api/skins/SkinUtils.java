package protocolsupportpocketstuff.api.skins;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import protocolsupport.libs.com.google.gson.JsonElement;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.libs.com.google.gson.JsonParser;
import protocolsupport.protocol.typeremapper.pe.PESkinModel;
import protocolsupport.utils.JsonUtils;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.util.PocketPlayer;
import protocolsupportpocketstuff.skin.DownloadskinThread;
import protocolsupportpocketstuff.storage.Skins;
import protocolsupportpocketstuff.zplatform.PlatformThings;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SkinUtils {

	/***
	 * Gets a bufferedimage from ARGB byte array.
	 * @param data
	 * @return
	 */
	public static BufferedImage imagefromPEData(byte[] data) {
		System.out.println(data.length);
		Validate.isTrue((data.length == 8192) || (data.length == 16384) || (data.length == 65536), "Skin data must be either 32*64 or 64*64 or 128*128 bytes long!");
		int width = (data.length == 65536) ? 128 : 64, height = (data.length == 65536) ? 128 : ((data.length == 16384) ? 64 : 32);
		ByteArrayInputStream stream = new ByteArrayInputStream(data);
		BufferedImage skin = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = new Color(stream.read(), stream.read(), stream.read(), stream.read());
				skin.setRGB(x, y, color.getRGB());
			}
		}
		return skin;
	}

	/***
	 * Gets a bytearray with ARGB data from a buffered skin image.
	 * @param skin
	 * @return
	 */
	public static byte[] imageToPEData(BufferedImage skin) {
		Validate.isTrue(skin.getWidth() == 64, "Must be 64 pixels wide");
		Validate.isTrue((skin.getHeight() == 128) || (skin.getHeight() == 64) || (skin.getHeight() == 32), "Must be 128, 64 or 32 pixels high");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		for (int y = 0; y < skin.getHeight(); y++) {
			for (int x = 0; x < skin.getWidth(); x++) {
                Color color = new Color(skin.getRGB(x, y), true);
                stream.write(color.getRed());
                stream.write(color.getGreen());
                stream.write(color.getBlue());
                stream.write(color.getAlpha());
			}
		}
		return stream.toByteArray();
	}

	/***
	 * Very cool method that updates a player's skin on the fly!
	 * @param player
	 * @param skin
	 * @param skindata
	 */
	public static void updateSkin(Player player, byte[] skin, SkinDataWrapper skindata, boolean isSlim) {
		Bukkit.getScheduler().runTask(ProtocolSupportPocketStuff.getInstance(), () -> {
			PlatformThings.getStuff().setSkinProperties(player, skindata);
			Bukkit.getOnlinePlayers().stream()
			.filter(onlinePlayer -> !onlinePlayer.equals(player))
			.filter(onlinePlayer -> onlinePlayer.canSee(player))
			.forEach(onlinePlayer -> {
				//removes the entity and display the new skin
				onlinePlayer.hidePlayer(ProtocolSupportPocketStuff.getInstance(), player);
				onlinePlayer.showPlayer(ProtocolSupportPocketStuff.getInstance(), player);
				//sends skin packet to dynamically update PE skins.
				if (PocketPlayer.isPocketPlayer(onlinePlayer)) {
					PocketPlayer.sendSkin(onlinePlayer, player.getUniqueId(), skin, isSlim);
				}
			});
		});
	}

	/***
	 * Very cool method that updates a player's skin on the fly!
	 * This method downloads the skin first, to display it to PE players (if it isn't in cache).
	 * @param player
	 * @param skindata
	 */
	public static void updateSkin(Player player, SkinDataWrapper skindata) {
		String url = urlFromProperty(skindata);
		boolean isSlim = slimFromProperty(skindata);
		new DownloadskinThread(url, (skin) -> {
			updateSkin(player, skin, skindata, isSlim);
		}).start();
	}
	
	public static void updateSkin(Player player, String url, ) {
		String url = urlFromProperty(skindata);
		boolean isSlim = slimFromProperty(skindata);
		new DownloadskinThread(url, (skin) -> {
			updateSkin(player, skin, skindata, isSlim);
		}).start();
	}

	/**
	 * Extracts the skinurl from skindata property.
	 * @param skindata
	 * @return the skinurl.
	 */
	public static String urlFromProperty(SkinDataWrapper skindata) {
		JsonObject skinobject = skinJsonFromProperty(skindata);
		if (skinobject == null)  { return null; };
		return JsonUtils.getString(skinobject, "url");
	}

	/**
	 * Extracts if the model contained in the skindata is slim or not.
	 * @param skindata
	 * @return
	 */
	public static boolean slimFromProperty(SkinDataWrapper skindata) {
		JsonObject skinobject = skinJsonFromProperty(skindata);
		if (skinobject == null)  { return false; };
		JsonObject skinMetadata; //Contains data about the skinModel. Currently only supports Slim and not Slim or Steve and Alex.
		return skinobject.has("metadata") && (skinMetadata = skinobject.get("metadata").getAsJsonObject()).has("model") && JsonUtils.getString(skinMetadata, "model").equals("slim");
	}

	/**
	 * Extracts the skinproperty object from skindata.
	 * @param skindata
	 * @return
	 */
	public static JsonObject skinJsonFromProperty(SkinDataWrapper skindata) {
		JsonElement propertyjson = new JsonParser().parse(new InputStreamReader(new ByteArrayInputStream(Base64.getDecoder().decode(skindata.getValue())), StandardCharsets.UTF_8));
		JsonObject texturesobject = JsonUtils.getJsonObject(JsonUtils.getAsJsonObject(propertyjson, "root element"), "textures");
		if (!texturesobject.has("SKIN")) {
			return null;
		}
		return JsonUtils.getJsonObject(texturesobject, "SKIN");
	}

	/**
	 * Wrapper for pc skindata.
	 */
	public static class SkinDataWrapper {

		private String signature;
		private String value;

		public SkinDataWrapper(String value, String signature) {
			this.value = value;
			this.signature = signature;
		}

		/**
		 * @return the skin texture value.
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @return the skin's mojang signature.
		 */
		public String getSignature() {
			return signature;
		}

	}

}
