package protocolsupportpocketstuff.api.modals;

public enum ModalType {

	MODAL_WINDOW("modal"), SIMPLE_FORM("form"), COMPLEX_FORM("custom_form");

	private final String peName;

	ModalType(String peName) {
		this.peName = peName;
	}

	public String getPeName() {
		return peName;
	}

	public static ModalType getByPeName(String peName) {
		for (ModalType type : ModalType.values()) {
			if (type.getPeName().equals(peName)) {
				return type;
			}
		}
		return null;
	}
}
