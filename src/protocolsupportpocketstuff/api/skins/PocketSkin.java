package protocolsupportpocketstuff.api.skins;

import protocolsupportpocketstuff.packet.SkinPacket;

public class PocketSkin {

	private String name;
	private SkinPacket skinPacket;
	
	public PocketSkin(String name, SkinPacket skinPacket) {
		this.name = name;
		this.skinPacket = skinPacket;
	}
	
	public String getName() {
		return name;
	}
	
	public SkinPacket getPacket() {
		return skinPacket;
	}
	
}
