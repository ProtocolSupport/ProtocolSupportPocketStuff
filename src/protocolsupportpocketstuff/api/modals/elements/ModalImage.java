package protocolsupportpocketstuff.api.modals.elements;

public class ModalImage {
	
	private final transient ModalImageType modalImageType;
	private final String type;
	private String data;
	
	public ModalImage(ModalImageType imageType, String data) {
		this.modalImageType = imageType;
		this.type = imageType.getPeName();
		this.data = data;
	}
	
	public String getPeType() {
		return type;
	}
	
	public ModalImageType getType() {
		return modalImageType;
	}

	public String getData() {
		return data;
	}

	public ModalImage setData(String data) {
		this.data = data;
		return this;
	}

	public enum ModalImageType {

		CLIENT_IMAGE("path"), EXTERNAL_IMAGE("url");

		private String peName;

		ModalImageType(String peName) {
			this.peName = peName;
		}

		public String getPeName() {
			return peName;
		}
		
	}
	
}