package protocolsupportpocketstuff.util.resourcepacks;

import protocolsupportpocketstuff.ProtocolSupportPocketStuff;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourcePackManager {
	ProtocolSupportPocketStuff plugin;

	public ResourcePackManager(ProtocolSupportPocketStuff plugin) {
		this.plugin = plugin;
		forceResources = plugin.getConfig().getBoolean("resource-and-behavior-packs.force-resources", false);
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
		List<String> fileNames = plugin.getConfig().getStringList("resource-and-behavior-packs.behavior-packs");
		ArrayList<ResourcePack> behaviorPacks = new ArrayList<ResourcePack>();

		for (String fileName : fileNames) {
			File file = new File(plugin.getDataFolder(), "behavior_packs/" + fileName);
			ResourcePack pack = new ZippedResourcePack(file);
			behaviorPacks.add(pack);
		}
		this.behaviorPacks = behaviorPacks;
	}

	public void reloadResourcePacks() {
		List<String> fileNames = plugin.getConfig().getStringList("resource-and-behavior-packs.resource-packs");
		ArrayList<ResourcePack> resourcePacks = new ArrayList<ResourcePack>();

		for (String fileName : fileNames) {
			File file = new File(plugin.getDataFolder(), "resource_packs/" + fileName);
			ResourcePack pack = new ZippedResourcePack(file);
			resourcePacks.add(pack);
		}
		this.resourcePacks = resourcePacks;
	}
}
