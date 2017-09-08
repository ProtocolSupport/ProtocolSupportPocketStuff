package protocolsupportpocketstuff.skin;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;

import protocolsupport.protocol.typeremapper.pe.PESkin;
import protocolsupportpocketstuff.api.skins.PocketSkin;
import protocolsupportpocketstuff.api.util.SkinUtils;
import protocolsupportpocketstuff.api.util.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.misc.UUIDLookup;
import protocolsupportpocketstuff.packet.SkinPacket;
import protocolsupportpocketstuff.storage.Skins;

public class SkinDownloader implements Runnable {

	private String name;
	private String skinProperty;
	
	public SkinDownloader(String name, String skinProperty) {
		this.name = name;
		this.skinProperty = skinProperty;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Getting UUID...");
			UUID uuid = UUIDLookup.getUuid(name);
			System.out.println("Started downloading task for " + name + ". Reading skin data from property...");
			SkinDataWrapper sdw = SkinUtils.readSkinDataFromProperty(skinProperty);
			System.out.println("Skindata decoded, url:" + sdw.getSkinUrl());
			String skinType = sdw.isSlim() ? "Standard_Alex" : "Standard_Steve";
			System.out.println("Skintype: " + skinType);
			byte[] skin = PESkin.toNetworkData(ImageIO.read(new URL(sdw.getSkinUrl())));
			System.out.println("Image downloaded, caching packet into the skincache.");
			SkinPacket skinPacket = new SkinPacket(uuid, name, skinType, skinType, skin, new byte[0], "", new byte[0]);
			Skins.INSTANCE.cacheSkin(name, new PocketSkin(name, skinPacket));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
