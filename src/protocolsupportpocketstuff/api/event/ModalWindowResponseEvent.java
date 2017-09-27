package protocolsupportpocketstuff.api.event;

import protocolsupport.api.Connection;

public class ModalWindowResponseEvent extends ModalResponseEvent {
	
	private boolean result;

	public ModalWindowResponseEvent(Connection connection, int modalId, String modalJSON, boolean result) {
		super(connection, modalId, modalJSON);
		this.result = result;
	}

	public boolean getResult() {
		return result;
	}
	
	public void setResult(boolean result) {
		this.result = result;
	}
	
}
