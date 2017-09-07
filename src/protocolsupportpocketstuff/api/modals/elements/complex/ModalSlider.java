package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupportpocketstuff.api.modals.elements.ComplexElementType;

import java.util.ArrayList;
import java.util.List;

public class ModalSlider extends ModalComplexUIElement {
	private float min;
	private float max;
	private float step;
	private float defaultValue;

	public ModalSlider() {
		super(ComplexElementType.SLIDER);
	}

	public ModalSlider setText(String text) {
		super.setText(text);
		return this;
	}

	public ModalSlider setStep(float step) {
		this.step = step;
		return this;
	}

	public ModalSlider setDefaultValue(float step) {
		this.defaultValue = defaultValue;
		return this;
	}

	public ModalSlider setMinimumValue(float min) {
		this.min = min;
		return this;
	}

	public ModalSlider setMaximumValue(float max) {
		this.max = max;
		return this;
	}
}
