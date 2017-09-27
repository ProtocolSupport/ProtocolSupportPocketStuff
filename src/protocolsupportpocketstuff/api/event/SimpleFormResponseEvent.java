package protocolsupportpocketstuff.api.event;

import protocolsupport.api.Connection;

public class SimpleFormResponseEvent extends ClientResponseEvent {
	private int clickedButton;

	public SimpleFormResponseEvent(Connection connection, int modalId, int clickedButton) {
		super(connection, modalId);
		this.clickedButton = clickedButton;
	}

	public int getClickedButton() {
		return clickedButton;
	}
}
