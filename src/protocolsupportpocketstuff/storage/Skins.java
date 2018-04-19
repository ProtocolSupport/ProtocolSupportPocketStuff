package protocolsupportpocketstuff.storage;

import protocolsupportpocketstuff.api.skins.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.libs.jodah.expiringmap.ExpiringMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * This class houses all cached skins for PE and PC.
 */
public class Skins {

	private Map<String, byte[]> pocketSkinCache; //Skin images for pocket.
	private Map<String, SkinDataWrapper> pcSkinCache; //Skin datas for java.

	private Skins() { }
	private static final Skins INSTANCE = new Skins();

	/**
	 * @return the skin storage.
	 */
	public static Skins getInstance() {
		return INSTANCE;
	}

	/**
	 * Builds the skincache. (this is called in onEnable in PSPS, calling it again will mean clearing the cache!)
	 * @param size - the size of the cache.
	 * @param rate - the rate at which items in the cash expire after last use.
	 */
	public void buildCache(int size, int rate) {
		if(size > 0 && rate > 0) {
			pocketSkinCache = ExpiringMap.builder().maxSize(size).expiration(rate, TimeUnit.HOURS).build();
			pcSkinCache = ExpiringMap.builder().maxSize(size).expiration(rate, TimeUnit.HOURS).build();
		}
	}

	/**
	 * Caches a PC skin for PE. 
	 * @param url - the skinUrl beloing to the pcSkin.
	 * @param skin - the byteArray with ARGB pixels to send to pe.
	 */
	public void cachePcSkin(String url, byte[] skin) {
		pocketSkinCache.putIfAbsent(url, skin);
	}

	/**
	 * Caches a PE skin for PC. 
	 * @param uuid - the uuid of the player.
	 * @param skinDataWrapper - the wrapped mc data from mineskin.
	 */
	public void cachePeSkin(String uuid, SkinDataWrapper skinDataWrapper) { 
		pcSkinCache.putIfAbsent(uuid, skinDataWrapper);
	}

	/**
	 * Clears a PC skin for PE.
	 * @param url - the skinUrl beloging to pc's skin.
	 */
	public void clearPcSkin(String url) { 
		pocketSkinCache.remove(url);
	}

	/**
	 * Clears a PE skin for PC.
	 * @param uuid - the uuid of the player of the skin.
	 */
    public void clearPeSkin(String uuid) {
        pcSkinCache.remove(uuid);
    }

    /**
     * Checks if PC skin is cached.
     * @param url - the skinUrl.
     * @return true if the skin was cached.
     */
	public boolean hasPcSkin(String url) {
		return pocketSkinCache.containsKey(url);
	}

	/**
	 * Checks if PE skin is cached
	 * @param uuid - the uuid of the skin's player.
	 * @return true if the skin was cached
	 */
	public boolean hasPeSkin(String uuid) {
		System.out.println("MEEp" + uuid);
		return pcSkinCache.containsKey(uuid);
	}

	/**
	 * Gets the image of the cached pc skin.
	 * @param url - the skinUrl.
	 * @return the skin in ARGB byte array.
	 */
	public byte[] getPcSkin(String url) {
		return pocketSkinCache.get(url);
	}

	/**
	 * Gets the skin information of the PE skin.
	 * @param uuid - the uuid of the player.
	 * @return the skinData for PC.
	 */
	public SkinDataWrapper getPeSkin(String uuid) {
		return pcSkinCache.get(uuid);
	}

	/**
	 * @return all cached PC skins.
	 */
	public Set<Entry<String, byte[]>> getPCSkins() {
		return pocketSkinCache.entrySet();
	}

	/**
	 * @return all cached PE skins.
	 */
	public Set<Entry<String, SkinDataWrapper>> getPESkins() {
		return pcSkinCache.entrySet();
	}
	
}
