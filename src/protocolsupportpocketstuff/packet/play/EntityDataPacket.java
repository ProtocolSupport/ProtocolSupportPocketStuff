package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe.EntityMetadata;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.protocol.utils.datawatcher.DataWatcherObject;
import protocolsupport.protocol.utils.i18n.I18NData;
import protocolsupport.utils.CollectionsUtils;
import protocolsupport.utils.CollectionsUtils.ArrayMap;
import protocolsupportpocketstuff.packet.PEPacket;

public class EntityDataPacket extends PEPacket {

	private long entityId;
	private CollectionsUtils.ArrayMap<DataWatcherObject<?>> metadata;

	public EntityDataPacket() { }

	public EntityDataPacket(long entityId, ArrayMap<DataWatcherObject<?>> metadata) {
		this.entityId = entityId;
		this.metadata = metadata;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.SET_ENTITY_DATA;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeVarLong(serializer, entityId);
		EntityMetadata.encodeMeta(serializer, connection.getVersion(), I18NData.DEFAULT_LOCALE, metadata);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		this.entityId = VarNumberSerializer.readVarLong(clientdata);
		clientdata.skipBytes(clientdata.readableBytes());
	}

	public long getEntityId() {
		return entityId;
	}

}