package protocolsupportpocketstuff.api.event;

import protocolsupport.api.Connection;

public class ModalWindowResponseEvent extends ClientResponseEvent {
	private boolean result;

	public ModalWindowResponseEvent(Connection connection, int modalId, boolean result) {
		super(connection, modalId);
		this.result = result;
	}

	public boolean getResult() {
		return result;
	}
}
