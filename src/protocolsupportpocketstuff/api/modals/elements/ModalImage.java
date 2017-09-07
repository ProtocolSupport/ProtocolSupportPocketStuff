package protocolsupportpocketstuff.api.modals.elements;

public class ModalImage {
	private String type;
	private String data;

	public String getType() {
		return type;
	}

	public String getData() {
		return data;
	}

	public ModalImage setType(String type) {
		this.type = type; return this;
	}

	public ModalImage setData(String data) {
		this.data = data; return this;
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
}