package protocolsupportpocketstuff.storage;

import java.util.concurrent.ConcurrentHashMap;

import protocolsupportpocketstuff.api.skins.PocketSkin;

public class Skins {
	
	private ConcurrentHashMap<String, PocketSkin> cache = new ConcurrentHashMap<String, PocketSkin>();
	
	public static final Skins INSTANCE = new Skins();
	private Skins() { }
	
	public void cacheSkin(String name, PocketSkin skin) {
		cache.put(name, skin);
	}
	
	public void clearSkin(String name) {
		cache.remove(name);
	}
	
	public PocketSkin getSkin(String name) {
		return cache.get(name);
	}
	
	public ConcurrentHashMap<String, PocketSkin> getPeSkins() {
		return cache;
	}
	
}
