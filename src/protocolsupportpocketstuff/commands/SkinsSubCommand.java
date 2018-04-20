package protocolsupportpocketstuff.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import protocolsupportpocketstuff.api.skins.SkinUtils;
import protocolsupportpocketstuff.util.BukkitUtils;

public class SkinsSubCommand implements SubCommand {

	@Override
	public int getMinArgs() {
		return 2;
	}

	@Override
	public boolean handle(CommandSender sender, String[] args) {
		Player p = BukkitUtils.getBestMatchingPlayer(args[0]);
		SkinUtils.updateSkin(p, args[1], false);
		return true;
	}

	@Override
	public String getHelp() {
		return null;
	}

}
