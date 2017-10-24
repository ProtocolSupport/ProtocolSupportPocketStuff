package protocolsupportpocketstuff.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.PocketStuffAPI;
import protocolsupportpocketstuff.util.resourcepacks.ResourcePack;

public class ReloadPacksSubCommand implements SubCommand {
	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public boolean handle(CommandSender sender, String[] args) {
		ProtocolSupportPocketStuff.getInstance().reloadConfig();
		PocketStuffAPI.getResourcePackManager().reloadPacks();

		sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + "§aReloaded all packs!");
		StringBuilder enabledBehPacks = new StringBuilder();
		StringBuilder enabledResPacks = new StringBuilder();
		for (int i = 0; PocketStuffAPI.getResourcePackManager().getBehaviorPacks().size() > i; i++) {
			ResourcePack pack = PocketStuffAPI.getResourcePackManager().getBehaviorPacks().get(i);
			enabledBehPacks.append("§e" + ChatColor.stripColor(pack.getName()) + " " + pack.getPackVersion());
			if (PocketStuffAPI.getResourcePackManager().getBehaviorPacks().size() - 1 != i) {
				enabledBehPacks.append("§7, ");
			}
		}
		for (int i = 0; PocketStuffAPI.getResourcePackManager().getResourcePacks().size() > i; i++) {
			ResourcePack pack = PocketStuffAPI.getResourcePackManager().getResourcePacks().get(i);
			enabledResPacks.append("§e" + ChatColor.stripColor(pack.getName()) + " " + pack.getPackVersion());
			if (PocketStuffAPI.getResourcePackManager().getResourcePacks().size() - 1 != i) {
				enabledResPacks.append("§7, ");
			}
		}
		sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + "§aBehavior Packs: " + enabledBehPacks.toString());
		sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + "§aResource Packs: " + enabledResPacks.toString());
		return true;
	}

	@Override
	public String getHelp() {
		return null;
	}
}
