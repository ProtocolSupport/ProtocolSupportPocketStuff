package protocolsupportpocketstuff.api.util;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolType;

public class PocketUtils {

	/***
	 * Checks if the player is a pocket player.
	 * @param player
	 * @return the truth
	 */
	public static boolean isPocketPlayer(Player player) {
		return ProtocolSupportAPI.getProtocolVersion(player).getProtocolType().equals(ProtocolType.PE);
	}
	
	/***
	 * Gets all pocket players on the server.
	 * @return
	 */
	public static Collection<? extends Player> getPocketPlayers() {
		return Bukkit.getOnlinePlayers().stream().filter(pocketFilter()).collect(Collectors.toList());
	}
	
	/***
	 * Filter to filter PE players.
	 * @return
	 */
	public static Predicate <Player> pocketFilter() {
		return p -> isPocketPlayer(p);
	}
	
}
