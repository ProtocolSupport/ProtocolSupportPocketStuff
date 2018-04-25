package protocolsupportpocketstuff.api.modals.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.modals.ModalType;

/***
 * Event that is always called when a PocketPlayer completes a modal.
 * Based on the ModalType information can be get after casting to sub responseEvents
 * ({@link ModalResponseEvent}, {@link SimpleFormResponseEvent} and {@link ComplexFormResponseEvent})
 * Cancelling this event can be used to prevent other plugins / handlers from executing code.
 */
public class ModalResponseEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private Connection connection;
	private int modalId;
	private String responseString;
	private boolean cancelled = false;
	private ModalType modalType;

	/***
	 * Creates a ResponseEvent.
	 * @param connection - The connection beloning to the event.
	 * @param modalId - The 
	 * @param responseString
	 * @param modalType
	 */
	public ModalResponseEvent(Connection connection, int modalId, String responseString, ModalType modalType) {
		this.connection = connection;
		this.modalId = modalId;
		this.responseString = responseString;
		this.modalType = modalType;
		this.cancelled = ((modalType == ModalType.UNKNOWN) ? true : false);
	}

	/***
	 * @return The connection of the responding player.
	 */
	public Connection getConnection() {
		return connection;
	}

	/***
	 * @return The responding player.
	 */
	public Player getPlayer() {
		return connection.getPlayer();
	}

	/***
	 * @return the raw response string.
	 */
	public String getResponseString() {
		return responseString;
	}

	/***
	 * @return The modalId responsible for this response.
	 */
	public int getModalId() {
		return modalId;
	}

	/***
	 * @return The ModalType responsible for this event.
	 */
	public ModalType getModalType() {
		return modalType;
	}

	/***
	 * Transforms into a ModalWindow response.
	 * @return the casted response.
	 * @throws ClassCastException - If the modalresponse is not of that type.
	 */
	public ModalWindowResponseEvent asModalWindowResponse() throws ClassCastException {
		return (ModalWindowResponseEvent) this;
	}

	/***
	 * Transforms into a SimpleForm response.
	 * @return the casted response.
	 * @throws ClassCastException - If the modalresponse is not of that type.
	 */
	public SimpleFormResponseEvent asSimpleFormResponse() throws ClassCastException {
		return (SimpleFormResponseEvent) this;
	}

	/***
	 * Transforms into a ComplexForm response.
	 * @return the casted response.
	 * @throws ClassCastException - If the modalresponse is not of that type.
	 */
	public ComplexFormResponseEvent asComplexFormResponse() throws ClassCastException {
		return (ComplexFormResponseEvent) this;
	}

	/***
	 * @return If modal handling is cancelled.
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	/***
	 * Sets if modal handling should be cancelled.
	 * @param cancelled
	 */
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/***
	 * @return the ResponseEvent handlers.
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	/***
	 * @return the ResponseEvent handlers.
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
