package protocolsupportpocketstuff.util;

import java.util.HashMap;
import java.util.UUID;

import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.utils.JsonUtils;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.handshake.ClientLoginPacket;

public class PocketInfoReceiver implements PocketPacketListener {

	@PocketPacketHandler
	public void onConnect(Connection connection, ClientLoginPacket packet) {
		JsonObject clientPayload = packet.getJsonPayload();
		JsonObject chain = packet.getChainPayload();
		HashMap<String, Object> clientInfo = new HashMap<>();
		// "In general you shouldn't really expect the payload to be sent with psbpe" -Shevchik
		if (chain != null && clientPayload != null) {
			clientInfo.put("UUID", UUID.fromString(JsonUtils.getString(chain, "identity")));
			clientInfo.put("ClientRandomId", clientPayload.get("ClientRandomId").getAsLong());
			clientInfo.put("DeviceModel", clientPayload.get("DeviceModel").getAsString());
			clientInfo.put("DeviceOS", clientPayload.get("DeviceOS").getAsInt());
			clientInfo.put("GameVersion", clientPayload.get("GameVersion").getAsString());
		}
		connection.addMetadata(StuffUtils.CLIENT_INFO_KEY, clientInfo);
	}

}
