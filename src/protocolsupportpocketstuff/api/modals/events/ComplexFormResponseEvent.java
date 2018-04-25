package protocolsupportpocketstuff.api.modals.events;

import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonArray;
import protocolsupportpocketstuff.api.modals.ModalType;

/***
 * This event is called when a player completes a {@link ComplexModal}
 * We still need to add response api for this, but for now you can hack something with the jsonarray :D
 */
public class ComplexFormResponseEvent extends ModalResponseEvent {

	//TODO: No JSON array, but an array of response objects.
	private JsonArray jsonArray;

	/***
	 * Creates a ComplexFormResponseEvent
	 * @param connection
	 * @param modalId
	 * @param modalJSON
	 * @param modalType
	 * @param jsonArray
	 */
	public ComplexFormResponseEvent(Connection connection, int modalId, String modalJSON, ModalType modalType, JsonArray jsonArray) {
		super(connection, modalId, modalJSON, modalType);
		this.jsonArray = jsonArray;
	}

	/***
	 * Gets the responses as a jsonarray.
	 * @return
	 */
	public JsonArray getJsonArray() {
		return jsonArray;
	}

	/***
	 * Sets the jsonArray to muck with other handlers.
	 * @param jsonArray
	 */
	public void setJsonArray(JsonArray jsonArray) {
		this.jsonArray = jsonArray;
	}

}
