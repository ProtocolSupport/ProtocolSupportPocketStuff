package protocolsupportpocketstuff.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {

	int getMinArgs();

	boolean handle(CommandSender sender, String[] args);

	String getHelp();

}
