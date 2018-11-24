package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

import java.util.UUID;

public class SkinPacket extends PEPacket {

	private UUID uuid;
	private String skinId;
	private String skinName;
	private String previousName;
	private byte[] skinData;
	private byte[] capeData;
	private String geometryId;
	private String geometryData;
	
	public SkinPacket() { }
	
	public SkinPacket(UUID uuid, String skinId, String skinName, String previousName, byte[] skinData, byte[] capeData, String geometryId, String geometryData) {
		this.uuid = uuid;
		this.skinId = skinId;
		this.skinName = skinName;
		this.previousName = previousName;
		this.skinData = skinData;
		this.capeData = capeData;
		this.geometryId = geometryId;
		this.geometryData = geometryData;
	}
	
	@Override
	public int getPacketId() {
		return PEPacketIDs.PLAYER_SKIN;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		ProtocolVersion version = connection.getVersion();
		MiscSerializer.writeUUID(serializer, version, uuid);
		StringSerializer.writeString(serializer, version, skinId);
		StringSerializer.writeString(serializer, version, skinName);
		StringSerializer.writeString(serializer, version, previousName);
		ArraySerializer.writeVarIntByteArray(serializer, skinData);
		ArraySerializer.writeVarIntByteArray(serializer, capeData);
		StringSerializer.writeString(serializer, version, geometryId);
		StringSerializer.writeString(serializer, version, geometryData);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		ProtocolVersion version = connection.getVersion();
		uuid = MiscSerializer.readUUID(clientdata);
		skinId = StringSerializer.readString(clientdata, version);
		skinName = StringSerializer.readString(clientdata, version);
		previousName = StringSerializer.readString(clientdata, version);
		skinData = ArraySerializer.readVarIntByteArray(clientdata);
		capeData = ArraySerializer.readVarIntByteArray(clientdata);
		geometryId = StringSerializer.readString(clientdata, version);
		geometryData = StringSerializer.readString(clientdata, version);
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

	public String getPreviousName() {
		return previousName;
	}

	public byte[] getSkinData() {
		return skinData;
	}

	public byte[] getCapeData() {
		return capeData;
	}

	public String getGeometryId() {
		return geometryId;
	}

	public String getGeometryData() {
		return geometryData;
	}

}
