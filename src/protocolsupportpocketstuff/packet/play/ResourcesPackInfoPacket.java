package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.api.resourcepacks.ResourcePack;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.util.packet.serializer.ResourcePackSerializer;

import java.util.List;

public class ResourcesPackInfoPacket extends PEPacket {

	private boolean forceResources;
	private List<ResourcePack> behaviorPacks;
	private List<ResourcePack> resourcePacks;

	public ResourcesPackInfoPacket() { }

	public ResourcesPackInfoPacket(boolean forceResources, List<ResourcePack> behaviorPacks, List<ResourcePack> resourcePacks) {
		this.forceResources = forceResources;
		this.behaviorPacks = behaviorPacks;
		this.resourcePacks = resourcePacks;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.RESOURCE_PACK;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
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
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		throw new UnsupportedOperationException();
	}

}
