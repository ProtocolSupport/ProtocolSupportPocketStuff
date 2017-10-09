package protocolsupportpocketstuff.hacks.dimensions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import protocolsupportpocketstuff.api.util.PocketPlayer;

public class DimensionListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onTeleport(PlayerTeleportEvent e) {
		if((!e.getFrom().getWorld().getEnvironment().equals(e.getTo().getWorld().getEnvironment()))
				&& (PocketPlayer.isPocketPlayer(e.getPlayer()))) {
			PocketPlayer.sendDimensionChange(e.getPlayer(), e.getTo().getWorld().getEnvironment(), e.getTo().toVector());
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onRespawn(PlayerRespawnEvent e) {
		if((!e.getPlayer().getWorld().getEnvironment().equals(e.getRespawnLocation().getWorld().getEnvironment()))
				&& (PocketPlayer.isPocketPlayer(e.getPlayer()))) {
			PocketPlayer.sendDimensionChange(e.getPlayer(), e.getPlayer().getWorld().getEnvironment(), e.getRespawnLocation().toVector());
		}
	}

}
