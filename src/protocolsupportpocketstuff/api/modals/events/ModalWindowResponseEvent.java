package protocolsupportpocketstuff.api.modals.events;

import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.modals.ModalType;

/***
 * Event that is called when a PocketPlayer completes a {@link ModalWindow}.
 * This event allows to see what button was pressed.
 * Cancelling this event can be used to prevent other plugins / handlers from executing code.
 */
public class ModalWindowResponseEvent extends ModalResponseEvent {

	private boolean result;

	/***
	 * Creates a ModalWindowResponseEvent.
	 * @param connection
	 * @param modalId
	 * @param modalJSON
	 * @param modalType
	 * @param result
	 */
	public ModalWindowResponseEvent(Connection connection, int modalId, String modalJSON, ModalType modalType, boolean result) {
		super(connection, modalId, modalJSON, modalType);
		this.result = result;
	}

	/***
	 * Gets the result of the modal.
	 * When the true button was pressed, the result is true.
	 * When the false button or cancel was pressed, the result is false.
	 * @return
	 */
	public boolean getResult() {
		return result;
	}

	/***
	 * Sets the result of this response to muck with other handlers.
	 * @param result
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

}
