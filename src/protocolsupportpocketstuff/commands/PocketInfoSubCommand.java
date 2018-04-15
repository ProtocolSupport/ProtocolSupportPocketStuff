package protocolsupportpocketstuff.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.util.PocketPlayer;
import protocolsupportpocketstuff.util.BukkitUtils;

public class PocketInfoSubCommand implements SubCommand {

	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public boolean handle(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			if (args.length > 0) {
				Player target = BukkitUtils.getBestMatchingPlayer(args[0]);
				displayPocketInfo(sender, target);
			} else {
				Player player = (Player) sender;
				displayPocketInfo(sender, player);
			}
		} else {
			sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + ChatColor.RED + "This command can only be executed in game");
		}
		return true;
	}

	@Override
	public String getHelp() {
		return "Displays information about currect pocket connection.";
	}

	private void displayPocketInfo(CommandSender sender, Player pocketPlayer) {
		if (pocketPlayer != null && PocketPlayer.isPocketPlayer(pocketPlayer)) {
			sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + ChatColor.GREEN + "Client Random ID: §e" + PocketPlayer.getClientRandomId(pocketPlayer));
			sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + ChatColor.GREEN + "Client Version: §e" + PocketPlayer.getClientVersion(pocketPlayer));
			sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + ChatColor.GREEN + "Device Model: §e" + PocketPlayer.getDeviceModel(pocketPlayer));
			sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + ChatColor.GREEN + "Operating System: §e" + PocketPlayer.getOperatingSystem(pocketPlayer));
		} else {
			sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + ChatColor.RED + "This command can only be executed on PE players");
		}
	}
	
}
