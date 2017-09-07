package protocolsupportpocketstuff.api.modals.elements.simple;

import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

public class ModalButton extends ModalUIElement {
	
	private ModalImage image;

	public ModalButton setImage(ModalImage.ModalImageType imageType, String imagePath) {
		image = new ModalImage().setType(imageType.getInternalType()).setData(imagePath);
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
