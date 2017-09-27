package protocolsupportpocketstuff.api.event;

import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonArray;

public class ComplexFormResponseEvent extends ClientResponseEvent {
	private JsonArray jsonArray;

	public ComplexFormResponseEvent(Connection connection, int modalId, JsonArray jsonArray) {
		super(connection, modalId);
		this.jsonArray = jsonArray;
	}

	public JsonArray getJsonArray() {
		return jsonArray;
	}
}
