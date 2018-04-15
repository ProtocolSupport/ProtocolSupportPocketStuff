package protocolsupportpocketstuff.api.skins;

import protocolsupport.libs.com.google.gson.Gson;
import protocolsupport.libs.com.google.gson.GsonBuilder;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupportpocketstuff.util.GsonUtils;

public class PocketSkinModel {

	private String skinId;
	private String skinName;
	private String geometryId;
	private JsonObject geometryData;

	/**
	 * Builder constructor.
	 */
	public PocketSkinModel() { }

	/**
	 * Creates a new skinmodel using all information in strings.
	 * @param skinName
	 * @param skinId
	 * @param geometryId
	 * @param geometryData
	 */
	public PocketSkinModel(String skinName, String skinId, String geometryId, String geometryData) {
		this.skinId = skinId;
		this.skinName = skinName;
		this.geometryId = geometryId;
		this.geometryData = GsonUtils.JSON_PARSER.parse(geometryData).getAsJsonObject();
	}

	/**
	 * @return the identifier of the skin.
	 */
	public String getSkinId() {
		return skinId;
	}

	/**
	 * Sets the identifier of the skin.
	 * @param skinId
	 */
	public void setSkinId(String skinId) {
		this.skinId = skinId;
	}

	/**
	 * @return the name of the skin.
	 */
	public String getSkinName() {
		return skinName;
	}

	/**
	 * Sets the name of the skin.
	 * @param skinName
	 */
	public void setSkinName(String skinName) {
		this.skinName = skinName;
	}

	/**
	 * @return the geometry identifier.
	 */
	public String getGeometryId() {
		return geometryId;
	}

	/**
	 * Sets the geometry id.
	 * This is the identifier of the geometrydata.
	 * @param geometryId
	 */
	public void setGeometryId(String geometryId) {
		this.geometryId = geometryId;
	}

	/**
	 * Gets the geometry data.
	 * @return the model's geometry in json.
	 */
	public JsonObject getGeometryDataJSON() {
		return geometryData;
	}

	/**
	 * Gets the geometry data.
	 * @return the model's geometry in json string.
	 */
	public String getGeometryData() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(geometryData);
	}

	/**
	 * Sets the geometry object of the model from json string.
	 * @param geometryData
	 */
	public void setGeometryData(String geometryData) {
		setGeometryData(GsonUtils.JSON_PARSER.parse(geometryData).getAsJsonObject());
	}
	
	/**
	 * Sets the geometry object of the model from json.
	 * @param geometryData
	 */
	public void setGeometryData(JsonObject geometryData) {
		this.geometryData = geometryData;
	}

	/**
	 * Transforms the skinModel to JSON.
	 * @return string with json
	 */
	public String toJSON() {
		return GsonUtils.GSON.toJson(this);
	}

	/**
	 * Gets a skin modal object from JSON string.
	 * @param skinJSON
	 * @return
	 */
	public static PocketSkinModel fromJSON(String skinJSON) {
		return GsonUtils.GSON.fromJson(skinJSON, PocketSkinModel.class);
	}
	
}
