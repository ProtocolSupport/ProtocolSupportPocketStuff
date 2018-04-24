package protocolsupportpocketstuff.api.modals.elements.complex;

/***
 * Represents a ModalSlider complex element.
 * A slider has a minimum and maximum, step and
 * default value.
 */
public class ModalSlider extends ComplexModalUIElement {

	private float min;
	private float max;
	private float step = 1f;
	private float defaultValue;

	/***
	 * Constructs a new ModalSlider with the required parameters.
	 * @param text - The text of the slider.
	 */
	public ModalSlider(String text) {
		super(ComplexElementType.SLIDER, text);
	}

	/***
	 * Sets the text / label of this slider.
	 * @param text
	 * @return this
	 */
	public ModalSlider setText(String text) {
		super.setText(text);
		return this;
	}

	/***
	 * Sets the step value of this slider.
	 * The step is the minimal value the user can slide
	 * the slider.
	 * @param step
	 * @return this
	 */
	public ModalSlider setStep(float step) {
		this.step = step;
		return this;
	}

	/***
	 * @return the slider's step value.
	 */
	public float getStep() {
		return step;
	}

	/***
	 * Sets the slider's default step value.
	 * @param defaultValue
	 * @return this
	 */
	public ModalSlider setDefaultValue(float defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	/***
	 * @return the slider's default value.
	 */
	public float getDefaultValue() {
		return defaultValue;
	}

	/***
	 * Sets the slider's minimum value.
	 * @param min
	 * @return this
	 */
	public ModalSlider setMinimumValue(float min) {
		this.min = min;
		return this;
	}

	/**
	 * @return the slider's minimum value.
	 */
	public float getMinimumValue() {
		return min;
	}

	/***
	 * Sets the slider's maximum value.
	 * @param max
	 * @return this
	 */
	public ModalSlider setMaximumValue(float max) {
		this.max = max;
		return this;
	}

	/***
	 * @return the slider's maximum value.
	 */
	public float getMaximumValue() {
		return max;
	}

}
