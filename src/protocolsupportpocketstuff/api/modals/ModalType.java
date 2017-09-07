package protocolsupportpocketstuff.api.modals;

public enum ModalType {
	
	MODAL_WINDOW("modal"), SIMPLE_FORM("form"), CUSTOM_FORM("custom_form");
	
	private String peName;
	
	ModalType(String peName) {
		this.peName = peName;
	}
	
	public String getPeName() {
		return peName;
	}
	
}
