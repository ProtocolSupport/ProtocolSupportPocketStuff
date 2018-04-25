package protocolsupportpocketstuff.api.modals.elements.complex;

import java.util.ArrayList;
import java.util.List;

/***
 * Represents a dropdown complex element.
 * A dropdown menu must have one and can have
 * multiple options and a ready selected default.
 */
public class ModalDropdown extends ComplexModalUIElement {

	private List<String> options = new ArrayList<String>();
	private int defaultOptionIndex;

	/***
	 * Constructs a dropdown menu with the required parameters.
	 * @param text - The label of the menu.
	 */
	public ModalDropdown(String text) {
		super(ComplexElementType.DROPDOWN, text);
	}

	/***
	 * Adds a non-default option to the dropdown menu.
	 * @param optionText
	 * @return this
	 */
	public ModalDropdown addOption(String optionText) {
		return addOption(optionText, false);
	}

	/***
	 * Adds an option to the dropdown menu,
	 * @param optionText
	 * @param isDefault
	 * @return this
	 */
	public ModalDropdown addOption(String optionText, boolean isDefault) {
		if (isDefault) {
			defaultOptionIndex = options.size();
		}
		options.add(optionText);
		return this;
	}

	/***
	 * Sets all options and defaults to first element (0).
	 * @param options
	 * @param defaultIndex
	 * @return this
	 */
	public ModalDropdown setOptions(List<String> options) {
		return setOptions(options, 0);
	}

	/***
	 * Sets all options and the default.
	 * @param options
	 * @param defaultIndex
	 * @return this
	 */
	public ModalDropdown setOptions(List<String> options, int defaultIndex) {
		this.options = options;
		this.defaultOptionIndex = defaultIndex;
		return this;
	}

	/***
	 * Sets the default option of this modal. <br/>
	 * <em>WARNING: Don't exceed the size of the options!</em>
	 * @param defaultOptionIndex
	 * @return this
	 */
	public ModalDropdown setDefaultOptionIndex(int defaultOptionIndex) {
		this.defaultOptionIndex = defaultOptionIndex;
		return this;
	}

	/***
	 * Gets the current options in the dropdown menu.
	 * @return the options.
	 */
	public List<String> getOptions() {
		return options;
	}

	/***
	 * Gets the current default option index in the dropdown menu.
	 * @return the default index.
	 */
	public int getDefaultOptionIndex() {
		return defaultOptionIndex;
	}

	/***
	 * Fully clones a ModalDropdown.
	 * @return the new Dropdown.
	 */
	public ModalDropdown clone() {
		return new ModalDropdown(getText()).setOptions(getOptions(), getDefaultOptionIndex());
	}

}
