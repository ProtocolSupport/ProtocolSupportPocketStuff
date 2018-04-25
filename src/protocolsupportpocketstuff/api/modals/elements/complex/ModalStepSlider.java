package protocolsupportpocketstuff.api.modals.elements.complex;

import java.util.ArrayList;
import java.util.List;

/***
 * Represents a ModalStepSlider complex element.
 * A StepSlider is a slider that has text options instead of values.
 * It also has a default option.
 */
public class ModalStepSlider extends ComplexModalUIElement {

	private List<String> steps = new ArrayList<String>();
	private int defaultStepIndex;

	/***
	 * Constructs a new ModalStepSlider using the required parameters.
	 * @param text - The text / label of the StepSlider.
	 */
	public ModalStepSlider(String text) {
		super(ComplexElementType.STEP_SLIDER, text);
	}

	/***
	 * Sets the text / label of the StepSlider.
	 * @param text
	 * @return this
	 */
	public ModalStepSlider setText(String text) {
		super.setText(text);
		return this;
	}

	/***
	 * Adds a non-default step option.
	 * @param optionText
	 * @return this
	 */
	public ModalStepSlider addStep(String optionText) {
		return addStep(optionText, false);
	}

	/***
	 * Adds a step option.
	 * @param optionText
	 * @param isDefault
	 * @return this
	 */
	public ModalStepSlider addStep(String optionText, boolean isDefault) {
		if (isDefault) {
			defaultStepIndex = steps.size();
		}
		steps.add(optionText);
		return this;
	}

	/***
	 * Adds multiple steps and sets the default to the first step (0).
	 * @param steps
	 * @return this
	 */
	public ModalStepSlider setSteps(List<String> steps) {
		return setSteps(steps, 0);
	}

	/***
	 * Adds multiple steps and sets the default.
	 * @param steps
	 * @param defaultStep
	 * @return this
	 */
	public ModalStepSlider setSteps(List<String> steps, int defaultStep) {
		this.steps = steps;
		this.defaultStepIndex = defaultStep;
		return this;
	}

	/***
	 * @return all step options.
	 */
	public List<String> getSteps() {
		return steps;
	}

	/***
	 * Sets the default step option.
	 * @param defaultStepIndex
	 * @return this
	 */
	public ModalStepSlider setDefaultStepIndex(int defaultStepIndex) {
		this.defaultStepIndex = defaultStepIndex;
		return this;
	}

	/***
	 * @return the index of the default step option.
	 */
	public int getDefaultStepIndex() {
		return defaultStepIndex;
	}

	/***
	 * Fully clones a ModalStepSlider.
	 * @return the new StepSlider.
	 */
	public ModalStepSlider clone() {
		return new ModalStepSlider(getText()).setSteps(getSteps(), getDefaultStepIndex());
	}

}
