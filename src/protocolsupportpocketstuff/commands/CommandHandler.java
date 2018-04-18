package protocolsupportpocketstuff.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandHandler implements CommandExecutor, TabCompleter {

	private final Map<String, SubCommand> subcommands = new LinkedHashMap<>();
	{
		subcommands.put("reloadpacks", new ReloadPacksSubCommand());
		subcommands.put("pocketinfo", new PocketInfoSubCommand());
		subcommands.put("skinz", new SkinsSubCommand());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("protocolsupport.admin")) {
			sender.sendMessage(ChatColor.DARK_RED + "You have no power here!");
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "ProtocolSupportPocketStuff");
			sender.sendMessage(ChatColor.GRAY + "/psps reloadpacks");
			sender.sendMessage(ChatColor.GRAY + "/psps pocketinfo");
			sender.sendMessage(ChatColor.GRAY + "/psps skinz");
			return true;
		}
		SubCommand subcommand = subcommands.get(args[0]);
		if (subcommand == null) {
			return false;
		}
		String[] subcommandargs = Arrays.copyOfRange(args, 1, args.length);
		if (subcommandargs.length < subcommand.getMinArgs()) {
			sender.sendMessage(ChatColor.DARK_RED + "Not enough args");
			return true;
		}
		return subcommand.handle(sender, subcommandargs);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return subcommands.keySet().stream()
				.filter(subcommand -> subcommand.startsWith(args[0]))
				.collect(Collectors.toList());
	}

}