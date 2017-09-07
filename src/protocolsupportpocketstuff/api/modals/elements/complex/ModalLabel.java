package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupport.libs.com.google.gson.annotations.SerializedName;
import protocolsupportpocketstuff.api.modals.elements.ComplexElementType;
import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

public class ModalLabel extends ModalComplexUIElement {
	public ModalLabel(ComplexElementType type) {
		super(ComplexElementType.LABEL);
	}

	public ModalLabel setText(String text) {
		super.setText(text);
		return this;
	}
}
