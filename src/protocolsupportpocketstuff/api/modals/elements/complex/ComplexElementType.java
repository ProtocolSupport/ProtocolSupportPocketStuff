package protocolsupportpocketstuff.api.modals.elements.complex;

/***
 * Specifies the type of a ComplexElement.
 */
public enum ComplexElementType {

	/***
	 * A dropdown menu.
	 * Implementation: {@link ModalDropdown}
	 */
	DROPDOWN("dropdown"),

	/***
	 * A input field.
	 * Implementation: {@link ModalInput}
	 */
	INPUT("input"),

	/***
	 * A simple text label.
	 * Implementation: {@link ModalLabel}
	 */
	LABEL("label"),

	/***
	 * A slider with number values.
	 * Implementation: {@link ModalSlider}
	 */
	SLIDER("slider"),

	/***
	 * A slider with text options.
	 * Implementation: {@link ModalStepSlider}
	 */
	STEP_SLIDER("step_slider"),

	/***
	 * A toggleable (true / false) option.
	 * Implementation: {@link ModalToggle}
	 */
	TOGGLE("toggle");

	private final String peName;

	ComplexElementType(String peName) {
		this.peName = peName;
	}

	/***
	 * @return The type's internal name.
	 */
	public String getPeName() {
		return peName;
	}

}
