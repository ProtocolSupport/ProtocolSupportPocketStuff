package protocolsupportpocketstuff.api.modals.elements.simple;

import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

public class ModalButton extends ModalUIElement {
	
	private ModalImage image;
	
	public ModalButton(String text) {
		this.setText(text);
	}
	
	public ModalButton(String text, ModalImage image) {
		this.setText(text);
		this.setImage(image);
	}

	public ModalButton setImage(ModalImage image) {
		this.image = image;
		return this;
	}

	public ModalButton setText(String text) {
		super.setText(text); 
		return this;
	}
	
	public ModalImage getButtonImage() {
		return image;
	}
	
}
