package protocolsupportpocketstuff.skin;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.skins.SkinUtils;
import protocolsupportpocketstuff.storage.Skins;

public class DownloadskinThread extends Thread {

	private static final ProtocolSupportPocketStuff plugin = ProtocolSupportPocketStuff.getInstance();
	private static final Skins skins = Skins.getInstance();

	private String url;
	private Consumer<byte[]> skindataApplyCallback;

	public DownloadskinThread(String url, Consumer<byte[]> skindataApplyCallback) {
		this.url = url;
		this.skindataApplyCallback = skindataApplyCallback;
	}

	@Override
	public void run() {
		super.run();
		try {
			if (skins.hasPcSkin(url)) {
				plugin.debug("Skin of provided url is in cache, returning!");
				skindataApplyCallback.accept(skins.getPcSkin(url));
			} else {
				plugin.debug("Downloading skin...");
				byte[] skin = SkinUtils.imageToPEData(ImageIO.read(new URL(url)));
				plugin.debug("Caching skin...");
				skins.cachePcSkin(url, skin);
				skindataApplyCallback.accept(skin);
			}
		} catch (IOException e) {
			plugin.pm("Error downloading skin: " + url + "!");
		}
	}

}
