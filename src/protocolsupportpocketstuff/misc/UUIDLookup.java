package protocolsupportpocketstuff.misc;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UUIDLookup {

	public static UUID getUUIDFromProperty(final String input) {
		if (input == null) {
			return null;
		}
		return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
	}
	
	private static final String profileurl = "https://api.mojang.com/profiles/minecraft";
	public static UUID getUuid(String nickname) {
		try {
			//open connection
			HttpURLConnection connection = (HttpURLConnection) setupConnection(new URL(profileurl));
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			//write body
			DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
			writer.write(JSONArray.toJSONString(Arrays.asList(nickname)).getBytes(StandardCharsets.UTF_8));
			writer.flush();
			writer.close();
			//check response code
			if (connection.getResponseCode() == 429) {
				//TODO: Handle rate limited?
			}
			//read response
			InputStream is = connection.getInputStream();
			String result = IOUtils.toString(is, StandardCharsets.UTF_8);
			IOUtils.closeQuietly(is);
			JSONArray jsonProfiles = (JSONArray) new JSONParser().parse(result);
			if (jsonProfiles.size() > 0) {
				JSONObject jsonProfile = (JSONObject) jsonProfiles.get(0);
				return getUUIDFromProperty((String) jsonProfile.get("id"));
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return UUID.randomUUID();
		//TODO: Handle fault?
	}
	
	private static URLConnection setupConnection(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(10000);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		return connection;
	}
	
}
