package protocolsupportpocketstuff.skin;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import protocolsupport.api.unsafe.peskins.PESkinsProvider;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.storage.Skins;

public class PcToPeProvider extends PESkinsProvider {

	ProtocolSupportPocketStuff plugin;
	Skins skinCache = Skins.INSTANCE;
	
	public PcToPeProvider(ProtocolSupportPocketStuff plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public byte[] getSkinData(String url) {
		if (skinCache.hasPeSkin(url)) {
			return skinCache.getPeSkin(url);
		}
		return null;
	}

	@Override
	public void scheduleGetSkinData(String url, Consumer<byte[]> skindataApplyCallback) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				try {
					if (skinCache.hasPeSkin(url)) {
						skindataApplyCallback.accept(skinCache.getPeSkin(url));
					} else {
						byte[] skin = toData(ImageIO.read(new URL(url)));
						skinCache.cachePeSkin(url, skin);
						skindataApplyCallback.accept(skin);
					}
				} catch (IOException e) { }
			}
			
		});
		
	}

}
