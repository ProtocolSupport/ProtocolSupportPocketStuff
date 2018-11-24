package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.EntityMetadata;
import protocolsupport.protocol.serializer.ItemStackSerializer;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.protocol.utils.i18n.I18NData;
import protocolsupport.utils.CollectionsUtils;
import protocolsupportpocketstuff.packet.PEPacket;

import java.util.UUID;

public class SpawnPlayerPacket extends PEPacket {

	private UUID uuid;
	private String name;
	private long entityId;
	private float x;
	private float y;
	private float z;
	private float motionX;
	private float motionY;
	private float motionZ;
	private float pitch;
	private float headYaw;
	private float yaw;
	private CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata;

	public SpawnPlayerPacket() { }

	public SpawnPlayerPacket(UUID uuid, String name, long entityId, 
			float x, float y, float z, 
			float motionX, float motionY, float motionZ, 
			float pitch, float headYaw, float yaw, 
			CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata) {
		this.uuid = uuid;
		this.name = name;
		this.entityId = entityId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		this.pitch = pitch;
		this.headYaw = headYaw;
		this.yaw = yaw;
		this.metadata = metadata;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.SPAWN_PLAYER;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		MiscSerializer.writeUUID(serializer, connection.getVersion(), uuid);
		StringSerializer.writeString(serializer, connection.getVersion(), name);
		StringSerializer.writeString(serializer, connection.getVersion(), ""); //ThirdPartyName :F
		VarNumberSerializer.writeVarInt(serializer, 0); //Platform
		VarNumberSerializer.writeSVarLong(serializer, entityId); // entity ID
		VarNumberSerializer.writeVarLong(serializer, entityId); // runtime ID
		StringSerializer.writeString(serializer, connection.getVersion(), ""); //Chat :F
		serializer.writeFloatLE(x); // x
		serializer.writeFloatLE(y); // y
		serializer.writeFloatLE(z); // z
		serializer.writeFloatLE(motionX); // motx
		serializer.writeFloatLE(motionY); // moty
		serializer.writeFloatLE(motionZ); // motz
		serializer.writeFloatLE(pitch); // pitch
		serializer.writeFloatLE(headYaw); // yaw
		serializer.writeFloatLE(yaw); // yaw
		//TODO: fix
		//ItemStackSerializer.writeItemStack(serializer, connection.getVersion(), I18NData.DEFAULT_LOCALE, ItemStackWrapper.NULL, true); //held item.
		//TODO: fix
		//EntityMetadata.encodeMeta(serializer, connection.getVersion(), I18NData.DEFAULT_LOCALE, metadata);
		//adventure settings
		VarNumberSerializer.writeVarInt(serializer, 0);
		VarNumberSerializer.writeVarInt(serializer, 0);
		VarNumberSerializer.writeVarInt(serializer, 0);
		VarNumberSerializer.writeVarInt(serializer, 0);
		VarNumberSerializer.writeVarInt(serializer, 0);
		serializer.writeLongLE(0); //?
		VarNumberSerializer.writeSVarInt(serializer, 0); //links, not used
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		throw new UnsupportedOperationException();
	}

}