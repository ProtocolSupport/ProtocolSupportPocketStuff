package protocolsupportpocketstuff.skin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import protocolsupport.api.Connection;
import protocolsupport.api.events.PlayerPropertiesResolveEvent.ProfileProperty;
import protocolsupportpocketstuff.api.skins.PocketSkinModel;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.api.util.SkinUtils;
import protocolsupportpocketstuff.api.util.SkinUtils.SkinDataWrapper;

public class SkinRunner implements Runnable {

	Connection connection;
	private UUID uuid;
	private String name;
	ProfileProperty[] properties;
	
	public SkinRunner(Connection connection, UUID uuid, String name, ProfileProperty[] properties) {
		this.connection = connection;
		this.uuid = uuid;
		this.name = name;
		this.properties = properties;
	}

	@Override
	public void run() {
		try {
			System.out.println("Started running task for " + name + ". Reading skin data from property...");
			Optional<ProfileProperty> texture = Arrays.stream(properties).filter(property -> property.getName().equals(SkinUtils.skinPropertyName)).findFirst();
			if(texture.isPresent()) {
				SkinDataWrapper sdw = SkinUtils.readSkinDataFromProperty(texture.get().getValue());
				System.out.println("Skindata decoded, url: " + sdw.getSkinUrl());
				String skinType = sdw.isSlim() ? "Standard_Alex" : "Standard_Steve";
				System.out.println("Skintype: " + skinType);
				byte[] skin = SkinUtils.getOrDownloadAndCache(sdw.getSkinUrl());
				if(PocketCon.isPocketConnection(connection)) {
					PocketSkinModel psm = SkinUtils.getDefaultSkin(sdw.isSlim());
					System.out.println(psm.getSkinId());
					System.out.println(psm.getSkinName());
					System.out.println(psm.getGeometryId());
					System.out.println(psm.getGeometryData());
					PocketCon.sendSkin(connection, uuid, skin, psm);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
