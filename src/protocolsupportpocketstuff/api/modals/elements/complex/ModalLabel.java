package protocolsupportpocketstuff.api.modals.elements.complex;

/***
 * Represents a label complex element.
 * A label is just a piece of text you can throw in
 * your complex modal. Labels are not send in response.
 */
public class ModalLabel extends ComplexModalUIElement {

	/***
	 * Creates label with the required parameters.
	 * @param text - The text of the label.
	 */
	public ModalLabel(String text) {
		super(ComplexElementType.LABEL, text);
	}

	/***
	 * Sets the text of the label.
	 * @param text
	 * @return this
	 */
	public ModalLabel setText(String text) {
		super.setText(text);
		return this;
	}

	/***
	 * Fully clones a ModalLabel.
	 * @return the new label.
	 */
	public ModalLabel clone() {
		return new ModalLabel(getText());
	}

}
