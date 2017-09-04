package protocolsupportpocketstuff.api.modals;

import protocolsupport.libs.com.google.gson.Gson;
import protocolsupport.libs.com.google.gson.JsonElement;
import protocolsupport.libs.com.google.gson.JsonObject;

public class ModalBuilder {
	
	private static Gson gson = new Gson();
	private JsonObject json;
	
	public ModalBuilder(JsonObject json) {
		this.json = json;
	}
	
	public ModalBuilder(String json) {
		this(gson.fromJson(json, JsonElement.class).getAsJsonObject());
	}
	
	public ModalBuilder() {
		this(new JsonObject());
	}
	
	public void addBool(String key, Boolean bool) {
		json.addProperty(key, bool);
	}
	
	public void addChar(String key, Character character) {
		json.addProperty(key, character);
	}
	
	public void addNum(String key, Number num) {
		json.addProperty(key, num);
	}
	
	public void addString(String key, String str) {
		json.addProperty(key, str);
	}
	
	//TODO: Write convenience functions for implemented modal thingy's.
	
	public Modal toModal() {
		return new Modal(this.toString());
	}
	
	public JsonObject toJson() {
		return json;
	}
	
	@Override
	public String toString() {
		return json.toString();
	}
	
}
