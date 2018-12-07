package protocolsupportpocketstuff.skin;

import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.skins.SkinUtils;
import protocolsupportpocketstuff.api.skins.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.libs.kevinsawicki.http.HttpRequest;
import protocolsupportpocketstuff.storage.Skins;
import protocolsupportpocketstuff.util.GsonUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class MineskinThread extends Thread {

	private static final ProtocolSupportPocketStuff plugin = ProtocolSupportPocketStuff.getInstance();
	private static final Skins skins = Skins.getInstance();

	byte[] skin;
	BufferedImage skinImage;
	boolean isSlim;
	private Consumer<SkinDataWrapper> skindataUploadedCallback;

	public MineskinThread(byte[] skin, boolean isSlim, Consumer<SkinDataWrapper> skindataUploadedCallback) {
		this.skin = skin;
		this.skinImage = SkinUtils.imageFromPEData(skin);
		this.isSlim = isSlim;
		this.skindataUploadedCallback = skindataUploadedCallback;
	}

	public MineskinThread(BufferedImage skin, boolean isSlim, Consumer<SkinDataWrapper> skindataUploadedCallback) {
		this.skin = SkinUtils.imageToPEData(skin);
		this.skinImage = skin;
		this.isSlim = isSlim;
		this.skindataUploadedCallback = skindataUploadedCallback;
	}

	public static BufferedImage resize(BufferedImage inputImage) {
		BufferedImage outputImage = new BufferedImage(inputImage.getWidth() / 2, inputImage.getHeight() / 2, inputImage.getType());
		Graphics2D g2d = outputImage.createGraphics();
		g2d.drawImage(inputImage, 0, 0, outputImage.getWidth(), outputImage.getHeight(), null);
		g2d.dispose();

		return outputImage;
	}

	@Override
	public void run() {
		super.run();
		String uniqueSkinId = SkinUtils.uuidFromSkin(skin, isSlim).toString();
		if (skins.hasPeSkin(uniqueSkinId)) {
			plugin.debug("PE skin already in cache!");
			skindataUploadedCallback.accept(skins.getPeSkin(uniqueSkinId));
			return;
		}
		//MineSkin limits
		if(skinImage.getWidth() > 64 || skinImage.getHeight() > 64)
			skinImage = resize(skinImage);
		plugin.debug("Sending skin " + uniqueSkinId + " to MineSkin...");
		try {
			int tries = 0;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(skinImage, "png", os);
			JsonObject mineskinResponse = sendToMineSkin(os, isSlim);
			plugin.debug("[#" + (tries + 1) + "] " + mineskinResponse);
			while (mineskinResponse.has("error")) {
				String error = mineskinResponse.get("error").getAsString();
				if (tries > 4) {
					plugin.pm("Failed to send skin to MineSkin after 5 tries (" + error + "), cancelling upload thread...");
					return;
				}
				plugin.debug("[#" + (tries + 1) + "] Failed to send skin! Retrying again in 5s...");
				Thread.sleep(5000); // Throttle
				mineskinResponse = sendToMineSkin(os, isSlim);
				plugin.debug("[#" + (tries + 1) + "] " + mineskinResponse);
				tries++;
			}
			JsonObject skinData = mineskinResponse.get("data").getAsJsonObject();
			JsonObject skinTexture = skinData.get("texture").getAsJsonObject();
			String signature = skinTexture.get("signature").getAsString();
			String value = skinTexture.get("value").getAsString();
			plugin.debug("Storing skin on cache...");
			SkinDataWrapper skindata = new SkinDataWrapper(value, signature);
			skins.cachePeSkin(uniqueSkinId, skindata);
			String url = SkinUtils.urlFromProperty(skindata);
			if (!skins.hasPcSkin(url)) {
				plugin.debug("Caching skin for PE players too (saves us a download)!");
				skins.cachePcSkin(url, skin);
			}
			skindataUploadedCallback.accept(skindata);
		} catch (Exception e) {
			plugin.pm("Error when uploading " + uniqueSkinId + " to Mineskin!");
			e.printStackTrace();
		}
	}

	private static JsonObject sendToMineSkin(ByteArrayOutputStream byteArrayOutputStream, boolean isSlim) throws IOException {
		InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
		JsonObject mineskinResponse = sendToMineSkin(inputStream, isSlim);
		inputStream.close();
		return mineskinResponse;
	}

	private static JsonObject sendToMineSkin(InputStream inputStream, boolean isSlim) {
		HttpRequest httpRequest = HttpRequest.post("http://api.mineskin.org/generate/upload?name=&model=" + (isSlim ? "slim" : "steve") + "&visibility=1")
				.userAgent("ProtocolSupportPocketStuff");
		httpRequest.part("file", "mcpe_skin.png", null, inputStream);
		return GsonUtils.JSON_PARSER.parse(httpRequest.body()).getAsJsonObject();
	}

}
