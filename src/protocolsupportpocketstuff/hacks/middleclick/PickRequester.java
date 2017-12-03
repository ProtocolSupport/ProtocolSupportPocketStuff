package protocolsupportpocketstuff.hacks.middleclick;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import protocolsupport.protocol.utils.types.Position;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;

public class PickRequester {

	private static final PickRequester INSTANCE = new PickRequester();
	public static PickRequester getInstance() {
		return INSTANCE;
	}
	
	public void handleBlockPick(Player player, Position position) {
		Bukkit.getScheduler().runTask(ProtocolSupportPocketStuff.getInstance(), new Runnable() {
			@Override
			public void run() {
				Block picked = player.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
				System.out.println("BLOCKTYPE: " + picked.getType());
			}
		});
	}
	
	
}
