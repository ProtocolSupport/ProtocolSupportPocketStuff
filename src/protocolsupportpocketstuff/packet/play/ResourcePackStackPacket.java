package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.api.resourcepacks.ResourcePack;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.util.packet.serializer.ResourcePackSerializer;

import java.util.List;

public class ResourcePackStackPacket extends PEPacket {

	private boolean forceResources;
	private List<ResourcePack> behaviorPacks;
	private List<ResourcePack> resourcePacks;

	public ResourcePackStackPacket() { }

	public ResourcePackStackPacket(boolean forceResources, List<ResourcePack> behaviorPacks, List<ResourcePack> resourcePacks) {
		this.forceResources = forceResources;
		this.behaviorPacks = behaviorPacks;
		this.resourcePacks = resourcePacks;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.RESOURCE_STACK;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		serializer.writeBoolean(forceResources);
		VarNumberSerializer.writeVarInt(serializer, behaviorPacks.size()); // beh pack count
		for (ResourcePack pack : behaviorPacks) {
			ResourcePackSerializer.writePackStack(serializer, pack);
		}
		VarNumberSerializer.writeVarInt(serializer, resourcePacks.size()); // res pack count
		for (ResourcePack pack : resourcePacks) {
			ResourcePackSerializer.writePackStack(serializer, pack);
		}
	}

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientData) {
		throw new UnsupportedOperationException();
	}

}
