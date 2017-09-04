package protocolsupportpocketstuff.api.modals;

public class Modal {
	
	private final String modalString;
	
	public Modal(String modalString) {
		this.modalString = modalString;
	}
	
	/***
	 * Get this modal's JSON.
	 * @return the modal's JSON code.
	 */
	public String getModalString() {
		return modalString;
	}
}
