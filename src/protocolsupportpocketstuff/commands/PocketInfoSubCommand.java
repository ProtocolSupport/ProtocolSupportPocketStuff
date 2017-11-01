package protocolsupportpocketstuff.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.util.PocketPlayer;

public class PocketInfoSubCommand implements SubCommand {
	@Override
	public int getMinArgs() {
		return 0;
	}

	@Override
	public boolean handle(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!PocketPlayer.isPocketPlayer(player)) {
				player.sendMessage(ProtocolSupportPocketStuff.PREFIX + "§cThis command can only be executed by PE players");
				return true;
			}

			player.sendMessage(ProtocolSupportPocketStuff.PREFIX + "§aClient Random ID: §e" + PocketPlayer.getClientRandomId(player));
			player.sendMessage(ProtocolSupportPocketStuff.PREFIX + "§aClient Version: §e" + PocketPlayer.getClientVersion(player));
			player.sendMessage(ProtocolSupportPocketStuff.PREFIX + "§aDevice Model: §e" + PocketPlayer.getDeviceModel(player));
			player.sendMessage(ProtocolSupportPocketStuff.PREFIX + "§aOperating System: §e" + PocketPlayer.getOperatingSystem(player));
		} else {
			sender.sendMessage(ProtocolSupportPocketStuff.PREFIX + "§cThis command can only be executed in game");
		}
		return true;
	}

	@Override
	public String getHelp() {
		return null;
	}
}
