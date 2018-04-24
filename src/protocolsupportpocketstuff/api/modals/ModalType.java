package protocolsupportpocketstuff.api.modals;

import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupportpocketstuff.util.GsonUtils;

public enum ModalType {

	/***
	 * Simple modal. Always has true / false buttons.<br/>
	 * Implementation: {@link ModalWindow}
	 */
	MODAL_WINDOW("modal"),

	/***
	 * Simple form. Multiple buttons with possible images.<br/>
	 * Implementation: {@link SimpleForm}
	 */
	SIMPLE_FORM("form"),

	/***
	 * Complex form. All kinds of cool and custom stuff.<br/>
	 * Implementation: {@link ComplexForm}
	 */
	COMPLEX_FORM("custom_form"),

	/***
	 * Returned when no formtype could be identified.
	 * Might also be returned on event / callback.
	 */
	UNKNOWN("unknown");

	private final String peName;

	ModalType(String peName) {
		this.peName = peName;
	}

	/***
	 * @return the PE name of the modal type.
	 */
	public String getPeName() {
		return peName;
	}

	/***
	 * Gets a ModalType from its PE name.
	 * @param peName
	 * @return
	 */
	public static ModalType getByPeName(String peName) {
		for (ModalType type : ModalType.values()) {
			if (type.getPeName().equals(peName)) {
				return type;
			}
		}
		return UNKNOWN;
	}

	/***
	 * Gets the ModalType from a json modal. 
	 * @param modalJSON
	 * @return the modalType.
	 */
	public static ModalType fromModal(String modalJSON) {
		JsonObject jsonParser = GsonUtils.JSON_PARSER.parse(modalJSON).getAsJsonObject();
		String pocketType = jsonParser.get("type").getAsString();
		return ModalType.getByPeName(pocketType);
	}

	/***
	 * Gets the ModalType from a modal. 
	 * @param modal
	 * @return the modalType.
	 */
	public static ModalType fromModal(Modal modal) {
		return modal.getType();
	}

}
