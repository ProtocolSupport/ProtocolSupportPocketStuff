package protocolsupportpocketstuff.storage;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import protocolsupportpocketstuff.libs.jodah.expiringmap.ExpiringMap;

public class Skins {
	
	private Map<String, byte[]> pocketSkinCache;
	
	public static final Skins INSTANCE = new Skins();
	private Skins() { }
	
	public void buildCache(int size, int rate) {
		if(size > 0 && rate > 0) {
			pocketSkinCache = ExpiringMap.builder().maxSize(size).expiration(rate, TimeUnit.HOURS).build();
		}
	}
	
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
