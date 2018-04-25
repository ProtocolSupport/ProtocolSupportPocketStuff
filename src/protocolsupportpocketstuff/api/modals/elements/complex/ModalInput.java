package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupport.libs.com.google.gson.annotations.SerializedName;

/***
 * Represents an input complex element.
 * An input is just a textfield the user can fill in.
 * It can have both a placeholder or default text.
 */
public class ModalInput extends ComplexModalUIElement {

	private String placeholder;
	@SerializedName("default")
	private String defaultText;

	/***
	 * Constructs a new ModalInput with the required parameters.
	 * @param text - The label of the input.
	 */
	public ModalInput(String text) {
		super(ComplexElementType.INPUT, text);
	}

	/***
	 * Sets the text or label of the input element.
	 * @param text
	 * @return this
	 */
	public ModalInput setText(String text) {
		super.setText(text);
		return this;
	}

	/***
	 * Sets the placeholder for this input.
	 * The placeholder vanishes if you type
	 * and is only send when the user types it.
	 * @param placeholderText
	 * @return this
	 */
	public ModalInput setPlaceholderText(String placeholderText) {
		this.placeholder = placeholderText;
		return this;
	}

	/***
	 * @return the input's placeholder.
	 */
	public String getPlaceHolderText() {
		return placeholder;
	}

	/***
	 * Sets the default text for this input.
	 * The default text needs to be removed 
	 * by the client or it will be send as is.
	 * @param defaultText
	 * @return
	 */
	public ModalInput setDefaultText(String defaultText) {
		this.defaultText = defaultText;
		return this;
	}

	/***
	 * @return the input's defualt text.
	 */
	public String getDefaultText() {
		return defaultText;
	}

	/***
	 * Fully clones a ModalInput.
	 * @return the new Input.
	 */
	public ModalInput clone() {
		return new ModalInput(getText()).setPlaceholderText(getPlaceHolderText()).setDefaultText(getDefaultText());
	}

}
