package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.utils.CollectionsUtils.ArrayMap;
import protocolsupportpocketstuff.packet.PEPacket;

public class EntityDataPacket extends PEPacket {

	private long entityId;
	//private CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata;

	public EntityDataPacket() { }

	public EntityDataPacket(long entityId, ArrayMap<DataWatcherObject<?>> metadata) {
		this.entityId = entityId;
		//this.metadata = metadata;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.SET_ENTITY_DATA;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		VarNumberSerializer.writeVarLong(serializer, entityId);
		//TODO: fix
		VarNumberSerializer.writeVarInt(serializer, 0);
		//EntityMetadata.encodeMeta(serializer, connection.getVersion(), I18NData.DEFAULT_LOCALE, metadata);
	}

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		this.entityId = VarNumberSerializer.readVarLong(clientdata);
		clientdata.skipBytes(clientdata.readableBytes());
	}

	public long getEntityId() {
		return entityId;
	}

}