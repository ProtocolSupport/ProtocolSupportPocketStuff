package protocolsupportpocketstuff.packet;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;

public class SkinPacket extends PePacket {

	private UUID uuid;
	private String skinId;
	private String skinName;
	private String serializeName;
	private byte[] skinData;
	private byte[] capeData;
	private String geometryModel;
	private byte[] geometryData;
	
	public SkinPacket() { }
	
	public SkinPacket(UUID uuid, String skinId, String skinName, String serializeName, byte[] skinData, byte[] capeData, String geometryModel, byte[] geometryData) {
		this.uuid = uuid;
		this.skinId = skinId;
		this.skinName = skinName;
		this.serializeName = serializeName;
		this.skinData = skinData;
		this.capeData = capeData;
		this.geometryModel = geometryModel;
		this.geometryData = geometryData;
	}
	
	@Override
	public int getPacketId() {
		return PEPacketIDs.PLAYER_SKIN;
	}

	@Override
	public void encodePayload(Connection connection, ByteBuf serializer) {
		ProtocolVersion version = connection.getVersion();
		MiscSerializer.writeUUID(serializer, uuid);
		StringSerializer.writeString(serializer, version, skinId);
		StringSerializer.writeString(serializer, version, skinName);
		StringSerializer.writeString(serializer, version, serializeName);
		ArraySerializer.writeByteArray(serializer, version, skinData);
		ArraySerializer.writeByteArray(serializer, version, capeData);
		StringSerializer.writeString(serializer, version, geometryModel);
		ArraySerializer.writeByteArray(serializer, version, geometryData);
	}

	@Override
	public void decodePayload(Connection connection, ByteBuf clientData) {
		ProtocolVersion version = connection.getVersion();
		uuid = MiscSerializer.readUUID(clientData);
		skinId = StringSerializer.readString(clientData, version);
		skinName = StringSerializer.readString(clientData, version);
		serializeName = StringSerializer.readString(clientData, version);
		skinData = ArraySerializer.readByteArray(clientData, version);
		capeData = ArraySerializer.readByteArray(clientData, version);
		geometryModel = StringSerializer.readString(clientData, version);
		geometryData = ArraySerializer.readByteArray(clientData, version);
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getSkinId() {
		return skinId;
	}

	public String getSkinName() {
		return skinName;
	}

	public String getSerializeName() {
		return serializeName;
	}

	public byte[] getSkinData() {
		return skinData;
	}

	public byte[] getCapeData() {
		return capeData;
	}

	public String getGeometryModel() {
		return geometryModel;
	}

	public byte[] getGeometryData() {
		return geometryData;
	}

}
