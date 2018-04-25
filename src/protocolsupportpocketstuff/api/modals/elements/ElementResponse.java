package protocolsupportpocketstuff.api.modals.elements;

import protocolsupport.libs.com.google.gson.JsonElement;

public class ElementResponse {

	private JsonElement value;

	/***
	 * Creates a new response object using the original json value from the array.
	 * @param value
	 */
	public ElementResponse(JsonElement value) {
		this.value = value;
	}

	/***
	 * Checks whether the value of the response is null.
	 * This can also be on labels, which are still in the response array.
	 * @return
	 */
	public boolean isNull() {
		return value == null || value.isJsonNull();
	}

	/***
	 * Gets the String contained in the response.
	 * This can (only) be done when the element was a:
	 * {@link ModalInput}.
	 * @return the String.s
	 */
	public String getString() {
		return value.getAsString();
	}

	/***
	 * Gets the int contained in the response.
	 * This can (only) be done when the element was a:
	 * {@link ModalDropdown} or a {@link ModalStepSlider}.
	 * @return the int.
	 */
	public int getInt() {
		return value.getAsInt();
	}

	/***
	 * Gets the float contained in the response.
	 * This can (only) be done when the element was a:
	 * {@link ModalSlider}.
	 * @return the float.
	 */
	public float getFloat() {
		return value.getAsFloat();
	}

	/***
	 * Gets the boolean contained in the response.
	 * This can (only) be done when the element was a:
	 * {@link ModalToggle}.
	 * @return the float.
	 */
	public boolean getBoolean() {
		return value.getAsBoolean();
	}

}
