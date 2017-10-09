package protocolsupportpocketstuff.api.event;

import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.modals.ModalType;

public class SimpleFormResponseEvent extends ModalResponseEvent {

	private int clickedButton;

	public SimpleFormResponseEvent(Connection connection, int modalId, String modalJSON, ModalType modalType, int clickedButton) {
		super(connection, modalId, modalJSON, modalType);
		this.clickedButton = clickedButton;
	}

	public int getClickedButton() {
		return clickedButton;
	}
	
	public void setClickedButton(int clickedButton) {
		this.clickedButton = clickedButton;
	}

}
