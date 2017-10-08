package protocolsupportpocketstuff.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.modals.ModalType;

public class ModalResponseEvent extends Event implements Cancellable {
	
	static final HandlerList handlers = new HandlerList();
	
	private Connection connection;
	private int modalId;
	private String modalJSON;
	private boolean cancelled = false;
	private ModalType modalType;

	public ModalResponseEvent(Connection connection, int modalId, String modalJSON, ModalType modalType) {
		this.connection = connection;
		this.modalId = modalId;
		this.modalJSON = modalJSON;
		this.modalType = modalType;
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

	public String getModalJSON() {
		return modalJSON;
	}

	public ModalType getModalType() { return modalType; }

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
