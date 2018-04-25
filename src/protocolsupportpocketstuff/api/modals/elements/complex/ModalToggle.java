package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupport.libs.com.google.gson.annotations.SerializedName;

/***
 * Represents a ModalToggle complex element.
 * A toggle is an option that can be either true or false (on or off).
 */
public class ModalToggle extends ComplexModalUIElement {
	@SerializedName("default")
	private boolean defaultValue;

	/***
	 * Constructs a new ModalToggle with the required parameters.
	 * @param text - The text / label of the toggle.
	 */
	public ModalToggle(String text) {
		super(ComplexElementType.TOGGLE, text);
	}

	/***
	 * Sets the text of the toggle.
	 * @param text
	 * @return this
	 */
	public ModalToggle setText(String text) {
		super.setText(text);
		return this;
	}

	/***
	 * Sets whether the toggle is enabled by default.
	 * @param defaultValue
	 * @return this
	 */
	public ModalToggle setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	/***
	 * Gets the default value of this toggle.
	 * @return the defaultValue.
	 */
	public boolean getDefaultValue() {
		return defaultValue;
	}

	/***
	 * Fully clones the ModalToggle.
	 * @return the new Toggle.
	 */
	public ModalToggle clone() {
		return new ModalToggle(getText()).setDefaultValue(getDefaultValue());
	}

}
