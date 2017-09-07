package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupportpocketstuff.api.modals.elements.ComplexElementType;
import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

public class ModalComplexUIElement extends ModalUIElement {
	private String type;

	public ModalComplexUIElement(ComplexElementType type) {
		this.type = type.getPeName();
	}
}
