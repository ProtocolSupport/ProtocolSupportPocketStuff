package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupport.libs.com.google.gson.annotations.SerializedName;
import protocolsupportpocketstuff.api.modals.elements.ComplexElementType;

public class ModalToggle extends ModalComplexUIElement {
	@SerializedName("default")
	private boolean defaultValue;

	public ModalToggle() {
		super(ComplexElementType.TOGGLE);
	}

	public ModalToggle setText(String text) {
		super.setText(text);
		return this;
	}

	public ModalToggle setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
}
