package protocolsupportpocketstuff.api.event;

import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.modals.ModalType;

public class ModalWindowResponseEvent extends ModalResponseEvent {

	private boolean result;

	public ModalWindowResponseEvent(Connection connection, int modalId, String modalJSON, ModalType modalType, boolean result) {
		super(connection, modalId, modalJSON, modalType);
		this.result = result;
	}

	public boolean getResult() {
		return result;
	}
	
	public void setResult(boolean result) {
		this.result = result;
	}

}
