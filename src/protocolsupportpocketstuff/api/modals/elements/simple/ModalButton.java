package protocolsupportpocketstuff.api.modals.elements.simple;

import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

/***
 * Implementation of the simple UIElement: Button.
 * A button needs to have either text or an image and text.
 */
public class ModalButton extends ModalUIElement {

	private ModalImage image;

	/***
	 * Constructs a new ModalButton with text.
	 * @param text - The button's text.
	 */
	public ModalButton(String text) {
		super(text);
	}

	/***
	 * Constructs a new ModalButton with text and image.
	 * @param text - The button's text.
	 * @param image - The button's image.
	 */
	public ModalButton(String text, ModalImage image) {
		super(text);
		this.setImage(image);
	}

	/**
	 * Sets the image of this button.
	 * @param image
	 * @return this
	 */
	public ModalButton setImage(ModalImage image) {
		this.image = image;
		return this;
	}

	/***
	 * Gets the image of this button.
	 * @return the image.
	 */
	public ModalImage getButtonImage() {
		return image;
	}

	/***
	 * Sets the text of this button.
	 * @param text
	 * @return this
	 */
	public ModalButton setText(String text) {
		super.setText(text); 
		return this;
	}

}
