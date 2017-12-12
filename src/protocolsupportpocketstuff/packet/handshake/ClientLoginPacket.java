package protocolsupportpocketstuff.packet.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolType;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.storage.Skins;
import protocolsupportpocketstuff.util.MineskinThread;
import protocolsupportpocketstuff.util.StuffUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

public class ClientLoginPacket extends PEPacket {
	int protocolVersion;
	JsonObject clientPayload;

	public ClientLoginPacket() { }

	@Override
	public int getPacketId() {
		return PEPacketIDs.LOGIN;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {

	}

	@SuppressWarnings("resource")
	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) {
		protocolVersion = clientData.readInt(); //protocol version

		ByteBuf logindata = Unpooled.wrappedBuffer(ArraySerializer.readByteArray(clientData, connection.getVersion()));

		// skip chain data
		logindata.skipBytes(logindata.readIntLE());

		// decode skin data
		try {
			InputStream inputStream = new ByteBufInputStream(logindata, logindata.readIntLE());
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			clientPayload = decodeToken(result.toString("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JsonObject decodeToken(String token) {
		String[] base = token.split("\\.");
		if (base.length < 2) {
			return null;
		}
		return StuffUtils.GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(Base64.getDecoder().decode(base[1]))), JsonObject.class);
	}

	public class decodeHandler extends PEPacket.decodeHandler {
		
		public decodeHandler(ProtocolSupportPocketStuff plugin, Connection connection) {
			super(plugin, connection);
		}
		
		@Override
		public void onRawPacketReceiving(RawPacketEvent e) {
			//Custom prevention logic because we couldn't get the version yet.
			if(connection.getVersion() == null) {
				return;
			} else if(connection.getVersion().getProtocolType() != ProtocolType.PE) {
				connection.removePacketListener(this);
				return;
			}
			super.onRawPacketReceiving(e);
		}

		@Override
		public void handle() {
			ClientLoginPacket clientLoginPacket = ClientLoginPacket.this;
			JsonObject clientPayload = clientLoginPacket.clientPayload;

			HashMap<String, Object> clientInfo = new HashMap<>();
			// "In general you shouldn't really expect the payload to be sent with psbpe" -Shevchik
			if (clientPayload.has("ClientRandomId"))
				clientInfo.put("ClientRandomId", clientPayload.get("ClientRandomId").getAsLong());
			if (clientPayload.has("DeviceModel"))
				clientInfo.put("DeviceModel", clientPayload.get("DeviceModel").getAsString());
			if (clientPayload.has("DeviceOS"))
				clientInfo.put("DeviceOS", clientPayload.get("DeviceOS").getAsInt());
			if (clientPayload.has("GameVersion"))
				clientInfo.put("GameVersion", clientPayload.get("GameVersion").getAsString());

			connection.addMetadata(StuffUtils.CLIENT_INFO_KEY, clientInfo);

			String skinData = clientPayload.get("SkinData").getAsString();
			String uniqueSkinId = UUID.nameUUIDFromBytes(skinData.getBytes()).toString();

			if (Skins.INSTANCE.hasPcSkin(uniqueSkinId)) {
				plugin.debug("Already cached skin, adding to the Connection's metadata...");
				connection.addMetadata(StuffUtils.APPLY_SKIN_ON_JOIN_KEY, Skins.INSTANCE.getPcSkin(uniqueSkinId));
				return;
			}
			byte[] skinByteArray = Base64.getDecoder().decode(skinData);

			MineskinThread mineskinThread = new MineskinThread(plugin, connection, uniqueSkinId, skinByteArray, clientLoginPacket.clientPayload.get("SkinGeometryName").getAsString().equals("geometry.humanoid.customSlim"));
			mineskinThread.start();
		}
	}
}
