package protocolsupportpocketstuff.api;

import org.bukkit.entity.Player;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolType;

public class Utils {

	/***
	 * Checks if the player is a pocket player.
	 * @param player
	 * @return the truth
	 */
	public static boolean isPocketPlayer(Player player) {
		return ProtocolSupportAPI.getProtocolVersion(player).getProtocolType().equals(ProtocolType.PE);
	}
	
}
