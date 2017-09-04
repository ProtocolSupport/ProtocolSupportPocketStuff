package protocolsupportpocketstuff.api.modals;

public class Modal {
	
	private final String modalJSON;
	
	public Modal(String modalJSON) {
		this.modalJSON = modalJSON;
	}
	
	/***
	 * Get this modal's JSON.
	 * @return the modal's JSON code.
	 */
	public String getModalJSON() {
		return modalJSON;
	}
}
