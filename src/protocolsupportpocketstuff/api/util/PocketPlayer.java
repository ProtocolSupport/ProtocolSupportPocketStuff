package protocolsupportpocketstuff.api.util;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolType;
import protocolsupportpocketstuff.api.modals.Modal;
import protocolsupportpocketstuff.api.skins.PocketSkin;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.storage.Modals;

/***
 * Utility class to get and do pocket-only-stuff for pocket 
 */
public class PocketPlayer {

    //=====================================================\\
    //						Getting						   \\
    //=====================================================\\
	
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
	 * <br/><br/>
	 * <i>If your goal is sending packets, we advise to use
	 * {@link PocketCon.getPocketConnections} instead for better performance.</i>
	 * @return
	 */
	public static Collection<? extends Player> getPocketPlayers() {
		return Bukkit.getOnlinePlayers().stream().filter(pocketFilter()).collect(Collectors.toList());
	}
	
	/***
	 * Filter to filter PE players.
	 * @return
	 */
	public static Predicate<Player> pocketFilter() {
		return p -> isPocketPlayer(p);
	}
	
    //=====================================================\\
    //						Packets						   \\
    //=====================================================\\
	
	/***
	 * Sends a modal to a player and gets the callback id. 
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param modal
	 * @return
	 */
	public static int sendModal(Player player, Modal modal) {
		return PocketCon.sendModal(ProtocolSupportAPI.getConnection(player), modal);
	}
	
	/***
	 * Sends a modal with an id specified.
	 * Nonono, don't use custom ids!
	 * If you like you can use this function in combination with
	 * {@link Modals.INSTANCE.takeId} to send custom JSON to the player.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param modalId
	 * @param modalJSON
	 * @return
	 */
	public static int sendModal(Player player, int modalId, String modalJSON) {
		return PocketCon.sendModal(ProtocolSupportAPI.getConnection(player), modalId, modalJSON);
	}
	
	/***
	 * Sends a PocketSkin to a pocket connection.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param connection
	 * @param skin
	 */
	public static void sendSkin(Player player, PocketSkin skin) {
		PocketCon.sendSkin(ProtocolSupportAPI.getConnection(player), skin);
	}
	
	/***
	 * Sends a packet to pocket.
	 * <br/><br/>
	 * <i>When sending multiple packets to pocket it is advised
	 * to get the connection using {@link ProtocolSupportAPI.getConnection}
	 * first and then use {@link PocketCon} to send the packets.</i>
	 * @param connection
	 * @param packet
	 */
	public static void sendPocketPacket(Player player, PEPacket packet) {
		PocketCon.sendPocketPacket(ProtocolSupportAPI.getConnection(player), packet);
	}
	
}
