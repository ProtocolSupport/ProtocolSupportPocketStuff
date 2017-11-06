package protocolsupportpocketstuff.skin;

import protocolsupport.api.unsafe.peskins.PESkinsProvider;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.storage.Skins;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.function.Consumer;

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
	public void scheduleGetSkinData(String url, UUID uuid, Consumer<byte[]> skindataApplyCallback) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			try {
				if (skinCache.hasPeSkin(url)) {
					skindataApplyCallback.accept(skinCache.getPeSkin(url));
				} else {
					byte[] skin = toData(ImageIO.read(new URL(url)));
					skinCache.cachePeSkin(url, skin);
					skinCache.cacheUUIDUrl(uuid, url);
					skindataApplyCallback.accept(skin);
				}
			} catch (IOException e) { }
		});
		
	}

}
