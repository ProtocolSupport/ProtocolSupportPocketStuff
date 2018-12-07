package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.api.resourcepacks.ResourcePack;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.util.packet.serializer.ResourcePackSerializer;

import java.util.List;

public class ResourcePackInfoPacket extends PEPacket {

	private boolean forceResources;
	private List<ResourcePack> behaviorPacks;
	private List<ResourcePack> resourcePacks;

	public ResourcePackInfoPacket() { }

	public ResourcePackInfoPacket(boolean forceResources, List<ResourcePack> behaviorPacks, List<ResourcePack> resourcePacks) {
		this.forceResources = forceResources;
		this.behaviorPacks = behaviorPacks;
		this.resourcePacks = resourcePacks;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.RESOURCE_PACK;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		serializer.writeBoolean(forceResources);
		serializer.writeShortLE(behaviorPacks.size()); // beh pack count
		for (ResourcePack pack : behaviorPacks) {
			ResourcePackSerializer.writePackInfo(serializer, pack);
		}
		serializer.writeShortLE(resourcePacks.size()); // res pack count
		for (ResourcePack pack : resourcePacks) {
			ResourcePackSerializer.writePackInfo(serializer, pack);
		}
	}

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		throw new UnsupportedOperationException();
	}

}
