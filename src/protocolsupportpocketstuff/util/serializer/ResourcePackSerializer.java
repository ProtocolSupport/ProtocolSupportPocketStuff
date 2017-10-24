package protocolsupportpocketstuff.util.serializer;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupportpocketstuff.util.resourcepacks.ResourcePack;

public class ResourcePackSerializer {
	public static void writePackInfo(ByteBuf serializer, ResourcePack pack) {
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, pack.getPackId());
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, pack.getPackVersion());
		serializer.writeLongLE(pack.getPackSize());
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, ""); // ???
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, ""); // ???
	}

	public static void writePackStack(ByteBuf serializer, ResourcePack pack) {
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, pack.getPackId());
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, pack.getPackVersion());
		StringSerializer.writeString(serializer, ProtocolVersion.MINECRAFT_PE, ""); // ???
	}
}
