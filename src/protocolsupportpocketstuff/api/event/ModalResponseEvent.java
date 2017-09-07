package protocolsupportpocketstuff.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.modals.response.ModalResponse;

public class ModalResponseEvent extends Event implements Cancellable {
	
	static final HandlerList handlers = new HandlerList();
	
	private Connection connection;
	private int modalId;
	private ModalResponse response;
	private boolean cancelled = false;
	
	public ModalResponseEvent(Connection connection, int modalId, ModalResponse response) {
		this.connection = connection;
		this.modalId = modalId;
		this.response = response;
	}
	
	public int getModalId() {
		return modalId;
	}
	
	public void setModalId(int modalId) {
		this.modalId = modalId;
	}
	
	public ModalResponse getResponse() {
		return response;
	}
	
	public void setResponse(ModalResponse response) {
		this.response = response;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public Player getPlayer() {
		return connection.getPlayer();
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
