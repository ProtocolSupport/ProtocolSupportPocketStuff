package protocolsupportpocketstuff.api.modals.events;

import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.modals.ModalType;

/***
 * Event that is called when a PocketPlayer completes a {@link SimpleForm}.
 * This event allows to see the if of the button that was pressed.
 */
public class SimpleFormResponseEvent extends ModalResponseEvent {

	private int clickedButton;

	/***
	 * Creates a SimpleFormResponseEvent.
	 * @param connection
	 * @param modalId
	 * @param modalJSON
	 * @param modalType
	 * @param clickedButton
	 */
	public SimpleFormResponseEvent(Connection connection, int modalId, String modalJSON, ModalType modalType, int clickedButton) {
		super(connection, modalId, modalJSON, modalType);
		this.clickedButton = clickedButton;
	}

	/***
	 * Gets the index of the clicked button.
	 * @return the clicked button.
	 */
	public int getClickedButton() {
		return clickedButton;
	}

	/**
	 * Sets the clicked button to mess with other handlers.
	 * @param clickedButton
	 */
	public void setClickedButton(int clickedButton) {
		this.clickedButton = clickedButton;
	}

}
