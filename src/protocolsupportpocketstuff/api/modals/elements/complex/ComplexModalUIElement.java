package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

/***
 * Abstract housing of ComplexModalUI implementations
 * All ComplexModalUIElement's have at least a text and type component.
 */
public abstract class ComplexModalUIElement extends ModalUIElement {

	private final transient ComplexElementType elementType;
	private final String type;

	/**
	 * Constructs a new ComplexModalUIElement with the required parameters.
	 * @param type - the type of the element.
	 * @param text - the text of the element.
	 */
	public ComplexModalUIElement(ComplexElementType type, String text) {
		super(text);
		this.elementType = type;
		this.type = type.getPeName();
	}

	/***
	 * @return the ComplexModalUIElement's type.
	 */
	public ComplexElementType getComplexType() {
		return elementType;
	}

	/***
	 * @return the ComplexModalUIElement's types internal name.
	 */
	public String getPeType() {
		return type;
	}

}
