package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupportpocketstuff.api.modals.elements.ComplexElementType;

import java.util.ArrayList;
import java.util.List;

public class ModalStepSlider extends ModalComplexUIElement {
	private List<String> steps = new ArrayList<String>();
	private int defaultStepIndex;

	public ModalStepSlider() {
		super(ComplexElementType.STEP_SLIDER);
	}

	public ModalStepSlider setText(String text) {
		super.setText(text);
		return this;
	}

	public ModalStepSlider addStep(String optionText) {
		return addStep(optionText, false);
	}

	public ModalStepSlider addStep(String optionText, boolean isDefault) {
		if (isDefault) {
			defaultStepIndex = steps.size();
		}
		steps.add(optionText);
		return this;
	}

	public ModalStepSlider setDefaultStepIndex(int defaultStepIndex) {
		this.defaultStepIndex = defaultStepIndex;
		return this;
	}

	public List<String> getOptions() {
		return steps;
	}

	public ModalStepSlider setSteps(List<String> steps) {
		this.steps = steps;
		return this;
	}
}
