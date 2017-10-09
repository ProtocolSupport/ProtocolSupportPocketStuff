package protocolsupportpocketstuff.storage;

import protocolsupportpocketstuff.api.util.SkinUtils;
import protocolsupportpocketstuff.libs.jodah.expiringmap.ExpiringMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Skins {
	
	private Map<String, byte[]> pocketSkinCache;
	private Map<String, SkinUtils.SkinDataWrapper> pcSkinCache;

	public static final Skins INSTANCE = new Skins();
	private Skins() { }
	
	public void buildCache(int size, int rate) {
		if(size > 0 && rate > 0) {
			pocketSkinCache = ExpiringMap.builder().maxSize(size).expiration(rate, TimeUnit.HOURS).build();
			pcSkinCache = ExpiringMap.builder().maxSize(size).expiration(rate, TimeUnit.HOURS).build();
		}
	}
	
	public void cachePeSkin(String url, byte[] skin) {
		pocketSkinCache.putIfAbsent(url, skin);
	}
	public void cachePcSkin(String uuid, SkinUtils.SkinDataWrapper skinDataWrapper) { pcSkinCache.putIfAbsent(uuid, skinDataWrapper); }

	public void clearPeSkin(String url) { 
		pocketSkinCache.remove(url);
	}
	public void clearPcSkin(String uuid) {
		pcSkinCache.remove(uuid);
	}

	public boolean hasPeSkin(String url) {
		return pocketSkinCache.containsKey(url);
	}

	public boolean hasPcSkin(String uuid) {
		return pcSkinCache.containsKey(uuid);
	}

	public byte[] getPeSkin(String name) {
		return pocketSkinCache.get(name);
	}
	public SkinUtils.SkinDataWrapper getPcSkin(String name) {
		return pcSkinCache.get(name);
	}

	public Set<Entry<String, byte[]>> getPeSkins() {
		return pocketSkinCache.entrySet();
	}
	public Set<Entry<String, SkinUtils.SkinDataWrapper>> getPcSkins() {
		return pcSkinCache.entrySet();
	}
}
