package protocolsupportpocketstuff.skin;

import protocolsupport.api.unsafe.peskins.PESkinsProvider;
import protocolsupportpocketstuff.storage.Skins;

import java.util.function.Consumer;

public class PcToPeProvider extends PESkinsProvider {

	private static final Skins skins = Skins.getInstance();

	@Override
	public byte[] getSkinData(String url) {
		if (skins.hasPeSkin(url)) {
			return skins.getPcSkin(url);
		}
		return null;
	}

	@Override
	public void scheduleGetSkinData(String url, Consumer<byte[]> skindataApplyCallback) {
		new DownloadskinThread(url, skindataApplyCallback).start();		
	}

}
