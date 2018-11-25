package protocolsupportpocketstuff.api.skins;

import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import protocolsupport.api.utils.ProfileProperty;
import protocolsupport.libs.com.google.gson.JsonElement;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.libs.com.google.gson.JsonParser;
import protocolsupport.utils.JsonUtils;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.util.PocketPlayer;
import protocolsupportpocketstuff.skin.DownloadskinThread;
import protocolsupportpocketstuff.skin.MineskinThread;
import protocolsupportpocketstuff.zplatform.PlatformThings;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class SkinUtils {

	/***
	 * Gets a bufferedimage from ARGB byte array.
	 * @param data
	 * @return
	 */
	public static BufferedImage imageFromPEData(byte[] data) {
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
				if (PocketPlayer.isPocketPlayer(onlinePlayer)) {
					//sends skin packet to dynamically update PE skins.
					PocketPlayer.sendSkin(onlinePlayer, player.getUniqueId(), skin, isSlim);
				} else {
					//removes the entity and display the new skin
					onlinePlayer.hidePlayer(ProtocolSupportPocketStuff.getInstance(), player);
					onlinePlayer.showPlayer(ProtocolSupportPocketStuff.getInstance(), player);
				}
			});
			if (PocketPlayer.isPocketPlayer(player)) { //Send with clientUUID to cause update.
				PocketPlayer.sendSkin(player, PocketPlayer.getClientUniqueId(player), skin, isSlim);
			} else { //Need tons of HACK packets to fake this :F
				PlatformThings.getStuff().sendPlayerSkin(player, skindata);
			}
		});
	}

	/***
	 * Very cool method that updates a player's skin on the fly!
	 * This method downloads the skin first, to display it to PE players (if it isn't in cache).
	 * <em>This method might runs async and may take some time to show results!</em>
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

	/***
	 * Very cool method that updates a player's skin on the fly!
	 * This method uploads the skin to mineskin and caches the texture, to display it so PC players can see it too!
	 * <em>This method might runs async and may take some time to show results!</em>
	 * @param player
	 * @param skin
	 * @param isSlim
	 */
	public static void updateSkin(Player player, byte[] skin, boolean isSlim) {
		new MineskinThread(skin, isSlim, (skindata) -> {
			updateSkin(player, skin, skindata, isSlim);
		}).start();
	}

	/***
	 * Very cool method that updates a player's skin on the fly!
	 * This method uploads the skin to mineskin and caches the texture, to display it so PC players can see it too!
	 * <em>This method might runs async and may take some time to show results!</em>
	 * @param player
	 * @param skin
	 * @param isSlim
	 */
	public static void updateSkin(Player player, BufferedImage skin, boolean isSlim) {
		updateSkin(player, imageToPEData(skin), isSlim);
	}

	/***
	 * Very cool method that updates a player's skin on the fly!
	 * This method downloads the skin, uploads it to mineskin and caches the texture and response
	 * and to display it to both PC and PE players sends the right packets for an update.
	 * <em>This method might runs async and may take some time to show results!</em>
	 * @param player
	 * @param skinUrl
	 * @param isSlim
	 */
	public static void updateSkin(Player player, String url, boolean isSlim) {
		new DownloadskinThread(url, (skin) -> {
			new MineskinThread(skin, isSlim, (skindata) -> {
				updateSkin(player, skin, skindata, isSlim);
			}).start();
		}).start();
	}

	/***
	 * Extracts the skinurl from skindata property.
	 * @param skindata
	 * @return the skinurl.
	 */
	public static String urlFromProperty(SkinDataWrapper skindata) {
		JsonObject skinobject = skinJsonFromProperty(skindata);
		if (skinobject == null)  { return null; };
		return JsonUtils.getString(skinobject, "url");
	}

	/***
	 * Extracts if the model contained in the skindata is slim or not.
	 * @param skindata
	 * @return true if the modal is slim.
	 */
	public static boolean slimFromProperty(SkinDataWrapper skindata) {
		JsonObject skinobject = skinJsonFromProperty(skindata);
		if (skinobject == null)  { return false; };
		JsonObject skinMetadata; //Contains data about the skinModel. Currently only supports Slim and not Slim or Steve and Alex.
		return skinobject.has("metadata") && (skinMetadata = skinobject.get("metadata").getAsJsonObject()).has("model") && JsonUtils.getString(skinMetadata, "model").equals("slim");
	}

	/***
	 * Extracts if the model contained in the geomtry is slim or not.
	 * @param geometryId
	 * @return
	 */
	public static boolean slimFromModel(String geometryId) {
		return geometryId.equals("geometry.humanoid.customSlim");
	}

	/***
	 * Extracts the skinproperty object from skindata.
	 * @param skindata
	 * @return the jsonProperty.
	 */
	public static JsonObject skinJsonFromProperty(SkinDataWrapper skindata) {
		JsonElement propertyjson = new JsonParser().parse(new InputStreamReader(new ByteArrayInputStream(Base64.getDecoder().decode(skindata.getValue())), StandardCharsets.UTF_8));
		JsonObject texturesobject = JsonUtils.getJsonObject(JsonUtils.getAsJsonObject(propertyjson, "root element"), "textures");
		if (!texturesobject.has("SKIN")) {
			return null;
		}
		return JsonUtils.getJsonObject(texturesobject, "SKIN");
	}

	/***
	 * Generates UUID from skin data for use in mineskin and cache reference.
	 * @return the uuid.
	 */
	public static UUID uuidFromSkin(byte[] skin, boolean isSlim) {
		skin[skin.length-1] = (byte) (isSlim ? 1 : 0);
		return UUID.nameUUIDFromBytes(skin);
	}

	/***
	 * Generates UUID from bufferedimage for use in mineskin and cache reference.
	 * @return the uuid.
	 */
	public static UUID uuidFromSkin(BufferedImage skin, boolean isSlim) {
		return uuidFromSkin(imageToPEData(skin), isSlim);
	}

	/***
	 * Wrapper for pc skindata.
	 */
	public static class SkinDataWrapper {

		private static final String PROPERTY_NAME = "textures";
		private String signature;
		private String value;

		/***
		 * Creates a new SkinDataWrapper using its base64 value and signature.
		 * @param value
		 * @param signature
		 */
		public SkinDataWrapper(String value, String signature) {
			this.value = value;
			this.signature = signature;
		}

		/***
		 * @return the skin texture value.
		 */
		public String getValue() {
			return value;
		}

		/***
		 * @return the skin's mojang signature.
		 */
		public String getSignature() {
			return signature;
		}

		/**
		 * @return a profile property containing the wrapped data.
		 */
		public ProfileProperty toProfileProperty() {
			return new ProfileProperty(PROPERTY_NAME, getValue(), getSignature());
		}

	}

}
