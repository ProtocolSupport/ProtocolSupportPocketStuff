package protocolsupportpocketstuff.api.modals;

/***
 * Representation of a Modal object.
 */
public interface Modal {

	/***
	 * @return the ModalType of this modal.
	 */
	ModalType getType();

	/***
	 * @return the internal type of this modal.
	 */
	String getPeType();

	/***
	 * Converts this modal to JSON.
	 * @return the JSON representation of this modal.
	 */
	String toJSON();

	/***
	 * Fully clones this modal.
	 * @return the new modal.
	 */
	Modal clone();

	/***
	 * Gets a generic modal object decompiled from json.
	 * @param json
	 * @return the modal
	 */
	static Modal fromJson(String json) {
		switch(ModalType.fromModal(json)) {
			case COMPLEX_FORM: return ComplexForm.fromJson(json);
			case MODAL_WINDOW: return ModalWindow.fromJson(json);
			case SIMPLE_FORM : return SimpleForm .fromJson(json);
			default: case UNKNOWN: return null;
		}
	}

}