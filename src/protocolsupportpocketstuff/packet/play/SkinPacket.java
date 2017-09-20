package protocolsupportpocketstuff.packet.play;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.event.PocketChangeSkinEvent;
import protocolsupportpocketstuff.api.util.SkinUtils;
import protocolsupportpocketstuff.packet.PEPacket;

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
		MiscSerializer.writeUUID(serializer, uuid);
		StringSerializer.writeString(serializer, version, skinId);
		StringSerializer.writeString(serializer, version, skinName);
		StringSerializer.writeString(serializer, version, previousName);
		ArraySerializer.writeByteArray(serializer, version, skinData);
		ArraySerializer.writeByteArray(serializer, version, capeData);
		StringSerializer.writeString(serializer, version, geometryId);
		StringSerializer.writeString(serializer, version, geometryData);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) {
		ProtocolVersion version = connection.getVersion();
		uuid = MiscSerializer.readUUID(clientData);
		skinId = StringSerializer.readString(clientData, version);
		skinName = StringSerializer.readString(clientData, version);
		previousName = StringSerializer.readString(clientData, version);
		skinData = ArraySerializer.readByteArray(clientData, version);
		capeData = ArraySerializer.readByteArray(clientData, version);
		geometryId = StringSerializer.readString(clientData, version);
		geometryData = StringSerializer.readString(clientData, version);
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

	public class decodeHandler extends PEPacket.decodeHandler {

		public decodeHandler(ProtocolSupportPocketStuff plugin, Connection connection) {
			super(plugin, connection);
		}

		@Override
		public void handle() {
			SkinPacket parent = SkinPacket.this;
			pm.callEvent(new PocketChangeSkinEvent(connection, parent.uuid, SkinUtils.fromData(parent.skinData), parent.getSkinName().equals("skin.Standard.CustomSlim")));
		}
		
	}
	
}
