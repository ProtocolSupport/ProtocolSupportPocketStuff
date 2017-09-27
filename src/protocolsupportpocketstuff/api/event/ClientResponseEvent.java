package protocolsupportpocketstuff.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import protocolsupport.api.Connection;

public class ClientResponseEvent extends Event implements Cancellable {
	
	static final HandlerList handlers = new HandlerList();
	
	private Connection connection;
	private int modalId;
	private boolean cancelled = false;
	
	public ClientResponseEvent(Connection connection, int modalId) {
		this.connection = connection;
		this.modalId = modalId;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Player getPlayer() {
		return connection.getPlayer();
	}
	
	public int getModalId() {
		return modalId;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
