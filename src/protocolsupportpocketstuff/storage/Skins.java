package protocolsupportpocketstuff.storage;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import protocolsupportpocketstuff.libs.jodah.expiringmap.ExpiringMap;

public class Skins {
	
	private Map<String, byte[]> pocketSkinCache = ExpiringMap.builder().maxSize(250).expiration(1, TimeUnit.DAYS).build();
	
	public static final Skins INSTANCE = new Skins();
	private Skins() { }
	
	public void cachePeSkin(String url, byte[] skin) {
		pocketSkinCache.putIfAbsent(url, skin);
	}
	
	public void clearPeSkin(String url) {
		pocketSkinCache.remove(url);
	}
	
	public boolean hasPeSkin(String url) {
		return pocketSkinCache.containsKey(url);
	}
	
	public byte[] getPeSkin(String name) {
		return pocketSkinCache.get(name);
	}
	
	public Set<Entry<String, byte[]>> getPeSkins() {
		return pocketSkinCache.entrySet();
	}
	
}
