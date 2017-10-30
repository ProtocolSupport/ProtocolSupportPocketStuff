package protocolsupportpocketstuff.util;

import protocolsupport.libs.com.google.gson.Gson;
import protocolsupport.libs.com.google.gson.JsonParser;

public class StuffUtils {
	public static final Gson GSON = new Gson();
	public static final JsonParser JSON_PARSER = new JsonParser();
	public static final int CHUNK_SIZE = 1048576;
	public static final String SKIN_PROPERTY_NAME = "textures";
	public static final String APPLY_SKIN_ON_JOIN_KEY = "applySkinOnJoin";
}
