package protocolsupportpocketstuff.api.modals.elements;

public class ModalUIElement {
	
	private String text;

	public ModalUIElement setText(String text) {
		this.text = text;
		return this;
	}

	public String getText() {
		return text;
	}
}
