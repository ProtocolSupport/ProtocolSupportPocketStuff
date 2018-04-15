package protocolsupportpocketstuff.skin;

import protocolsupport.api.unsafe.peskins.PESkinsProvider;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.storage.Skins;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class PcToPeProvider extends PESkinsProvider {

	Skins skinCache = Skins.getInstance();
	
	@Override
	public byte[] getSkinData(String url) {
		if (skinCache.hasPeSkin(url)) {
			return skinCache.getPcSkin(url);
		}
		return null;
	}

	@Override
	public void scheduleGetSkinData(String url, Consumer<byte[]> skindataApplyCallback) {
		Bukkit.getScheduler().runTaskAsynchronously(ProtocolSupportPocketStuff.getInstance(), () -> {
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
