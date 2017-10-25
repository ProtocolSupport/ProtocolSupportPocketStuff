package protocolsupportpocketstuff.resourcepacks;

import protocolsupportpocketstuff.ProtocolSupportPocketStuff;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourcePackManager {
	public static final String FOLDER_NAME = "pocketpacks";

	ProtocolSupportPocketStuff plugin;

	public ResourcePackManager(ProtocolSupportPocketStuff plugin) {
		this.plugin = plugin;
		forceResources = plugin.getConfig().getBoolean("pocketpacks.force-resources", false);
	}

	private boolean forceResources;
	private List<ResourcePack> behaviorPacks = new ArrayList<ResourcePack>();
	private List<ResourcePack> resourcePacks = new ArrayList<ResourcePack>();

	public boolean resourcePacksRequired() {
		return forceResources;
	}

	public List<ResourcePack> getBehaviorPacks() {
		return behaviorPacks;
	}

	public List<ResourcePack> getResourcePacks() {
		return resourcePacks;
	}

	public void reloadPacks() {
		reloadBehaviorPacks();
		reloadResourcePacks();
	}

	public void reloadBehaviorPacks() {
		List<String> fileNames = plugin.getConfig().getStringList("pocketpacks.behavior-packs");
		ArrayList<ResourcePack> behaviorPacks = new ArrayList<ResourcePack>();

		for (String fileName : fileNames) {
			File file = new File(plugin.getDataFolder(), FOLDER_NAME + "/" + fileName);
			ResourcePack pack = new ZippedResourcePack(file);
			behaviorPacks.add(pack);
		}
		this.behaviorPacks = behaviorPacks;
	}

	public void reloadResourcePacks() {
		List<String> fileNames = plugin.getConfig().getStringList("pocketpacks.resource-packs");
		ArrayList<ResourcePack> resourcePacks = new ArrayList<ResourcePack>();

		for (String fileName : fileNames) {
			File file = new File(plugin.getDataFolder(), FOLDER_NAME + "/" + fileName);
			ResourcePack pack = new ZippedResourcePack(file);
			resourcePacks.add(pack);
		}
		this.resourcePacks = resourcePacks;
	}
}
