package protocolsupportpocketstuff.api.modals.elements.complex;

public class ModalSlider extends ModalComplexUIElement {
	
	private float min;
	private float max;
	private float step = 1f;
	private float defaultValue;

	public ModalSlider(String text) {
		super(ComplexElementType.SLIDER);
		super.setText(text);
	}

	public ModalSlider setText(String text) {
		super.setText(text);
		return this;
	}

	public ModalSlider setStep(float step) {
		this.step = step;
		return this;
	}

	public ModalSlider setDefaultValue(float defaultValue) {
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
	
	public float getMinimumValue() {
		return min;
	}
	
	public float getMaximumValue() {
		return max;
	}
	
	public float getDefaultValue() {
		return defaultValue;
	}
	
	public float getStep() {
		return step;
	}
	
}
