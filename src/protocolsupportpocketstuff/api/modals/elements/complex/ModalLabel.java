package protocolsupportpocketstuff.api.modals.elements.complex;

public class ModalLabel extends ModalComplexUIElement {
	
	public ModalLabel() {
		super(ComplexElementType.LABEL);
	}

	public ModalLabel setText(String text) {
		super.setText(text);
		return this;
	}
	
}
