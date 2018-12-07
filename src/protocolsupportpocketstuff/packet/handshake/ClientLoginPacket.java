package protocolsupportpocketstuff.packet.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.utils.JsonUtils;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.util.GsonUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;

import protocolsupport.libs.com.nimbusds.jose.JWSObject;

public class ClientLoginPacket extends PEPacket {

	protected int protocolVersion;
	protected Map<String, List<String>> chainData;
	protected JsonObject chainPayload;
	protected JsonObject jsonPayload;

	public ClientLoginPacket() { }

	@Override
	public int getPacketId() {
		return PEPacketIDs.LOGIN;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		throw new UnsupportedOperationException();
	}

	private JsonObject decodeToken(String token) {
		String[] base = token.split("\\.");
		if (base.length < 2) {
			return null;
		}
		return GsonUtils.GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(Base64.getDecoder().decode(base[1]))), JsonObject.class);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) {
		protocolVersion = clientData.readInt(); //protocol version
		ByteBuf logindata = Unpooled.wrappedBuffer(ArraySerializer.readVarIntByteArraySlice(clientData));
		chainData = GsonUtils.GSON.fromJson(
				new InputStreamReader(new ByteBufInputStream(logindata, logindata.readIntLE())),
				new TypeToken<Map<String, List<String>>>() { private static final long serialVersionUID = 1L; }.getType()
		);
		List<String> chain = chainData.get("chain");
		chain.forEach(element -> { //decode chain data
			JWSObject jwsobject;
			try {
				jwsobject = JWSObject.parse(element);
				JsonObject jsonobject = GsonUtils.GSON.fromJson(jwsobject.getPayload().toString(), JsonObject.class);
				if (jsonobject.has("extraData")) {
					chainPayload = JsonUtils.getJsonObject(jsonobject, "extraData");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		});
		try { //decode skin data
			InputStream inputStream = new ByteBufInputStream(logindata, logindata.readIntLE());
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			jsonPayload = decodeToken(result.toString("UTF-8"));
			inputStream.close();
			result.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public JsonObject getChainPayload() {
		return chainPayload;
	}

	public JsonObject getJsonPayload() {
		return jsonPayload;
	}

}
