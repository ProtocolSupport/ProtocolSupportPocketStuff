package protocolsupportpocketstuff.api.event;

import protocolsupport.api.Connection;

public class SimpleFormResponseEvent extends ModalResponseEvent {

	private int clickedButton;

	public SimpleFormResponseEvent(Connection connection, int modalId, String modalJSON, boolean isClosedByClient, int clickedButton) {
		super(connection, modalId, modalJSON, isClosedByClient);
		this.clickedButton = clickedButton;
	}

	public int getClickedButton() {
		return clickedButton;
	}
	
	public void setClickedButton(int clickedButton) {
		this.clickedButton = clickedButton;
	}

}
