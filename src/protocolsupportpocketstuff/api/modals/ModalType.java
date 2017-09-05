package protocolsupportpocketstuff.api.modals;

public enum ModalType {
	
	SIMPLE_MODAL("modal"), SIMPLE_FORM("form"), CURSOM_FORM("custom_form");
	
	private String peName;
	
	ModalType(String peName) {
		this.peName = peName;
	}
	
	public String getPeName() {
		return peName;
	}
	
}
