package protocolsupportpocketstuff.api.modals.elements.simple;

import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

public class ModalButton extends ModalUIElement {
	private ModalButtonImage image;

	public ModalButton setImage(ModalImageType imageType, String imagePath) {
		image = new ModalButtonImage()
				.setType(imageType.getInternalType())
				.setData(imagePath);
		return this;
	}

	public ModalButton setText(String text) {
		super.setText(text);
		return this;
	}

	public enum ModalImageType {
		CLIENT_IMAGE("path"), EXTERNAL_IMAGE("url");

		private String internalType;

		ModalImageType(String internalType) {
			this.internalType = internalType;
		}

		public String getInternalType() {
			return internalType;
		}
	}

	class ModalButtonImage {
		private String type;
		private String data;

		public ModalButtonImage setType(String type) {
			this.type = type;
			return this;
		}

		public ModalButtonImage setData(String data) {
			this.data = data;
			return this;
		}
	}
}
