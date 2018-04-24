package protocolsupportpocketstuff.api.modals.elements;

/***
 * Abstract housing of ModalUI implementations
 * All ModalUIElement's have at least a text component.
 */
public abstract class ModalUIElement {

	private String text;

	/***
	 * Constructs a ModalUIElement with the required parameters.
	 * @param text - the text of the element.
	 */
	protected ModalUIElement(String text) {
		this.text = text;
	}

	/***
	 * Sets this ModalUIElement's text.
	 * @param text
	 * @return this
	 */
	public ModalUIElement setText(String text) {
		this.text = text;
		return this;
	}

	/***
	 * Gets the text of a ModalUIElement.
	 * @return the text.
	 */
	public String getText() {
		return text;
	}

}