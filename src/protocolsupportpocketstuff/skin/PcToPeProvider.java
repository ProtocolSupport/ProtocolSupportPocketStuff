package protocolsupportpocketstuff.skin;

import protocolsupport.api.unsafe.peskins.PESkinsProvider;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.storage.Skins;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class PcToPeProvider extends PESkinsProvider {

	ProtocolSupportPocketStuff plugin;
	Skins skinCache = Skins.getInstance();
	
	public PcToPeProvider(ProtocolSupportPocketStuff plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public byte[] getSkinData(String url) {
		if (skinCache.hasPeSkin(url)) {
			return skinCache.getPcSkin(url);
		}
		return null;
	}

	@Override
	public void scheduleGetSkinData(String url, Consumer<byte[]> skindataApplyCallback) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			try {
				if (skinCache.hasPcSkin(url)) {
					skindataApplyCallback.accept(skinCache.getPcSkin(url));
				} else {
					byte[] skin = toData(ImageIO.read(new URL(url)));
					skinCache.cachePcSkin(url, skin);
					skindataApplyCallback.accept(skin);
				}
			} catch (IOException e) { }
		});
		
	}

}
