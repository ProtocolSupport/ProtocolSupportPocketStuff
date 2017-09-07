package protocolsupportpocketstuff.skin;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;

import protocolsupport.protocol.typeremapper.pe.PESkin;
import protocolsupportpocketstuff.api.skins.PocketSkin;
import protocolsupportpocketstuff.api.util.SkinUtils;
import protocolsupportpocketstuff.api.util.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.packet.SkinPacket;
import protocolsupportpocketstuff.storage.Skins;

public class SkinDownloader implements Runnable {

	private UUID uuid;
	private String name;
	private String skinProperty;
	
	public SkinDownloader(UUID uuid, String name, String skinProperty) {
		this.uuid = uuid;
		this.name = name;
		this.skinProperty = skinProperty;
	}
	
	@Override
	public void run() {
		try {
			SkinDataWrapper sdw = SkinUtils.readSkinDataFromProperty(skinProperty);
			String skinType = sdw.isSlim() ? "Standard_Alex" : "Standard_Steve";
			byte[] skin = PESkin.toNetworkData(ImageIO.read(new URL(sdw.getSkinUrl())));
			SkinPacket skinPacket = new SkinPacket(uuid, name, skinType, skinType, skin, new byte[0], "", new byte[0]);
			Skins.INSTANCE.cacheSkin(name, new PocketSkin(name, skinPacket));
		} catch (IOException e) { }
	}

}
