package protocolsupportpocketstuff.api.event;

import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonArray;
import protocolsupportpocketstuff.api.modals.ModalType;

public class ComplexFormResponseEvent extends ModalResponseEvent {

	//TODO: No JSON array, but an array of response objects.
	private JsonArray jsonArray;

	public ComplexFormResponseEvent(Connection connection, int modalId, String modalJSON, ModalType modalType, JsonArray jsonArray) {
		super(connection, modalId, modalJSON, modalType);
		this.jsonArray = jsonArray;
	}

	public JsonArray getJsonArray() {
		return jsonArray;
	}
	
	public void setJsonArray(JsonArray jsonArray) {
		this.jsonArray = jsonArray;
	}

}
