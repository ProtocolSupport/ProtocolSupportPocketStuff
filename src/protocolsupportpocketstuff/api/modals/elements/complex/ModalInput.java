package protocolsupportpocketstuff.api.modals.elements.complex;

import protocolsupport.libs.com.google.gson.annotations.SerializedName;

public class ModalInput extends ModalComplexUIElement {
	
	private String placeholder;
	@SerializedName("default")
	private String defaultText;

	public ModalInput(String text) {
		super(ComplexElementType.INPUT);
		super.setText(text);
	}

	public ModalInput setText(String text) {
		super.setText(text);
		return this;
	}

	public ModalInput setPlaceholderText(String placeholderText) {
		this.placeholder = placeholderText;
		return this;
	}

	public ModalInput setDefaultText(String defaultText) {
		this.defaultText = defaultText;
		return this;
	}
	
	public String getPlaceHolder() {
		return placeholder;
	}
	
	public String getDefaultText() {
		return defaultText;
	}
	
}
