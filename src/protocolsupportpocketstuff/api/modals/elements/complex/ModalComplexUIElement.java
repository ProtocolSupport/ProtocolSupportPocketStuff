package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

public class ModalComplexUIElement extends ModalUIElement {
	
	private final transient ComplexElementType elementType;
	private final String type;

	public ModalComplexUIElement(ComplexElementType type) {
		this.elementType = type;
		this.type = type.getPeName();
	}
	
	public String getPeType() {
		return type;
	}
	
	public ComplexElementType getComplexType() {
		return elementType;
	}
	
}
