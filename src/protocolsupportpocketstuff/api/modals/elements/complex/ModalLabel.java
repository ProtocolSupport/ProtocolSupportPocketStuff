package protocolsupportpocketstuff.api.modals.elements.complex;

public class ModalLabel extends ModalComplexUIElement {
	
	public ModalLabel(String text) {
		super(ComplexElementType.LABEL);
		super.setText(text);
	}

	public ModalLabel setText(String text) {
		super.setText(text);
		return this;
	}
	
}
