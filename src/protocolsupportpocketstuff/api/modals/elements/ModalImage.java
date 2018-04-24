package protocolsupportpocketstuff.api.modals.elements;

/***
 * Implementation of Images objects used in modals.
 * An image can either be a path to resourcepack file
 * or url to remote image. PE will download the image
 * on its own accord.
 */
public class ModalImage {

	private final transient ModalImageType modalImageType;
	private final String type;
	private String data;

	/***
	 * Constructs a new ModalImage with the required parameters.
	 * @param imageType - The type of the image.
	 * @param data - The data (url / path) of the image.
	 */
	public ModalImage(ModalImageType imageType, String data) {
		this.modalImageType = imageType;
		this.type = imageType.getPeName();
		this.data = data;
	}

	/***
	 * @return the ImageType of this ModalImage.
	 */
	public ModalImageType getType() {
		return modalImageType;
	}

	/***
	 * @return the internal representation of this ModalImage's type.
	 */
	public String getPeType() {
		return type;
	}

	/***
	 * @return the url / path to the image.
	 */
	public String getData() {
		return data;
	}

	/***
	 * Sets the url / path to the image.
	 * @param data
	 * @return this
	 */
	public ModalImage setData(String data) {
		this.data = data;
		return this;
	}

	/***
	 * The types a ModalImage can be.
	 */
	public enum ModalImageType {

		/***
		 * Client or local image.
		 * The client will expect a local path to
		 * an image contained in the resourcestack
		 * if this type is used.
		 */
		CLIENT_IMAGE("path"),

		/***
		 * External or remote image.
		 * The client will expect an utl to
		 * an image if this type is used.
		 */
		EXTERNAL_IMAGE("url");

		private String peName;

		ModalImageType(String peName) {
			this.peName = peName;
		}

		/***
		 * @return the internal PE name.
		 */
		public String getPeName() {
			return peName;
		}

	}

}