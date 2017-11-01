package protocolsupportpocketstuff.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import protocolsupport.api.Connection;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class PocketChangeSkinEvent extends Event implements Cancellable {

	static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private Connection connection;
	private String skinStorageId;
	private UUID uuid;
	private BufferedImage skin;
	private boolean isSlim;
	
	public PocketChangeSkinEvent(Connection connection, String skinStorageId, UUID uuid, BufferedImage skin, boolean isSlim) {
		this.connection = connection;
		this.skinStorageId = skinStorageId;
		this.uuid = uuid;
		this.skin = skin;
		this.isSlim = isSlim;
	}

	public Connection getConnection() {
		return connection;
	}
	
	public Player getPlayer() {
		return connection.getPlayer();
	}

	public UUID getUuid() {
		return uuid;
	}

	public BufferedImage getSkin() {
		return skin;
	}

	public void setSkin(BufferedImage skin) {
		this.skin = skin;
	}

	public boolean isSlim() {
		return isSlim;
	}

	public void setSlim(boolean isSlim) {
		this.isSlim = isSlim;
	}

	public String getSkinStorageId(String skinStorageId) { return skinStorageId; }

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
