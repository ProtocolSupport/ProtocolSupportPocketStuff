package protocolsupportpocketstuff.api.skins;

import protocolsupport.libs.com.google.gson.Gson;
import protocolsupport.libs.com.google.gson.GsonBuilder;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.libs.com.google.gson.JsonParser;

public class PocketSkinModel {

	private String skinId;
	private String skinName;
	private String geometryId;
	private JsonObject geometryData;
	
	public PocketSkinModel(String skinName, String skinId, String geometryId, String geometryData) {
		this.skinId = skinId;
		this.skinName = skinName;
		this.geometryId = geometryId;
		this.geometryData = new JsonParser().parse(geometryData).getAsJsonObject();
	}

	public String getSkinId() {
		return skinId;
	}

	public void setSkinId(String skinId) {
		this.skinId = skinId;
	}
	
	public String getSkinName() {
		return skinName;
	}

	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}

	public String getGeometryId() {
		return geometryId;
	}

	public void setGeometryId(String geometryId) {
		this.geometryId = geometryId;
	}

	public JsonObject getGeometryDataJSON() {
		return geometryData;
	}
	
	public String getGeometryData() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(geometryData);
	}

	public void setGeometryData(String geometryData) {
		setGeometryData(new JsonParser().parse(geometryData).getAsJsonObject());
	}
	
	public void setGeometryData(JsonObject geometryData) {
		this.geometryData = geometryData;
	}

	public String toJSON() {
		return new Gson().toJson(this);
	}
	
	public static PocketSkinModel fromJSON(String skinJSON) {
		return new Gson().fromJson(skinJSON, PocketSkinModel.class);
	}
	
}
