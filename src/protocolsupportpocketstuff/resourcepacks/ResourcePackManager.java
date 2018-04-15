package protocolsupportpocketstuff.resourcepacks;

import protocolsupportpocketstuff.ProtocolSupportPocketStuff;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourcePackManager {
	public static final String FOLDER_NAME = "pocketpacks";
	private boolean forceResources;
	private List<ResourcePack> behaviorPacks = new ArrayList<ResourcePack>();
	private List<ResourcePack> resourcePacks = new ArrayList<ResourcePack>();

	/**
	 * @return whether or nor PE clients are forced to accept resourcepacks.
	 */
	public boolean resourcePacksRequired() {
		return forceResources;
	}

	/**
	 * @return the list of enabled behaviourpacks.
	 */
	public List<ResourcePack> getBehaviorPacks() {
		return behaviorPacks;
	}

	/**
	 * @return the list of enabled resourcepacks.
	 */
	public List<ResourcePack> getResourcePacks() {
		return resourcePacks;
	}
	
	/**
	 * @return whether or not there are any resources.
	 */
	public boolean isEmpty() {
		return getBehaviorPacks().isEmpty() && getResourcePacks().isEmpty();
	}

	/**
	 * Reloads all resource and behaviour packs and options.
	 */
	public void reloadPacks() {
		forceResources = ProtocolSupportPocketStuff.getInstance().getConfig().getBoolean("pocketpacks.force-resources", false);
		createResourcesFolder();
		reloadBehaviorPacks();
		reloadResourcePacks();
	}
	
	/**
	 * Creates the resourcepack directory.
	 */
	public void createResourcesFolder() {
		new File(ProtocolSupportPocketStuff.getInstance().getDataFolder(), FOLDER_NAME + "/").mkdirs();
	}

	/**
	 * Reloads behaviour packs from config and disk.
	 */
	public void reloadBehaviorPacks() {
		List<String> fileNames = ProtocolSupportPocketStuff.getInstance().getConfig().getStringList("pocketpacks.behavior-packs");
		ArrayList<ResourcePack> behaviorPacks = new ArrayList<ResourcePack>();
		for (String fileName : fileNames) {
			File file = new File(ProtocolSupportPocketStuff.getInstance().getDataFolder(), FOLDER_NAME + "/" + fileName);
			ResourcePack pack = new ZippedResourcePack(file);
			behaviorPacks.add(pack);
		}
		this.behaviorPacks = behaviorPacks;
	}

	/**
	 * Reloads resourcepacks from config and disk.
	 */
	public void reloadResourcePacks() {
		List<String> fileNames = ProtocolSupportPocketStuff.getInstance().getConfig().getStringList("pocketpacks.resource-packs");
		ArrayList<ResourcePack> resourcePacks = new ArrayList<ResourcePack>();
		for (String fileName : fileNames) {
			File file = new File(ProtocolSupportPocketStuff.getInstance().getDataFolder(), FOLDER_NAME + "/" + fileName);
			ResourcePack pack = new ZippedResourcePack(file);
			resourcePacks.add(pack);
		}
		this.resourcePacks = resourcePacks;
	}
}
