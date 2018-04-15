package protocolsupportpocketstuff.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BukkitUtils {

	/**
	 * Gets the closest matching online player given name entered.
	 * @param name
	 * @return
	 */
	public static Player getBestMatchingPlayer(String name) {
		Player bestMatch = Bukkit.getPlayer(name);
		if (bestMatch == null) {
			List<Player> matching = Bukkit.matchPlayer(name);
			if (matching.size() > 0) {
				return matching.get(0);
			}
		}
		return bestMatch;
	}

}
