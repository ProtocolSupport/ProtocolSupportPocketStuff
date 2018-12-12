package protocolsupportpocketstuff.util.packet.serializer;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.utils.ProtocolVersionsHelper;
import protocolsupportpocketstuff.api.resourcepacks.ResourcePack;

public class ResourcePackSerializer {

	public static void writePackInfo(ByteBuf serializer, ResourcePack pack) {
		StringSerializer.writeString(serializer, ProtocolVersionsHelper.LATEST_PE, pack.getPackId());
		StringSerializer.writeString(serializer, ProtocolVersionsHelper.LATEST_PE, pack.getPackVersion());
		serializer.writeLongLE(pack.getPackSize());
		StringSerializer.writeString(serializer, ProtocolVersionsHelper.LATEST_PE, ""); // ???
		StringSerializer.writeString(serializer, ProtocolVersionsHelper.LATEST_PE, ""); // ???
	}

	public static void writePackStack(ByteBuf serializer, ResourcePack pack) {
		StringSerializer.writeString(serializer, ProtocolVersionsHelper.LATEST_PE, pack.getPackId());
		StringSerializer.writeString(serializer, ProtocolVersionsHelper.LATEST_PE, pack.getPackVersion());
		StringSerializer.writeString(serializer, ProtocolVersionsHelper.LATEST_PE, ""); // ???
	}

}
