package protocolsupportpocketstuff.packet.play;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.packet.PEPacket;

public class ResourcePackResponsePacket extends PEPacket {

	protected int status;
	ArrayList<String> missingPacks = new ArrayList<String>();

	@Override
	public int getPacketId() {
		return PEPacketIDs.RESOURCE_RESPONSE;
	}

	@Override
	public void toData(ConnectionImpl connection, ByteBuf serializer) {
		throw new UnsupportedOperationException();
	}

	public static final int REFUSED = 1;
	public static final int SEND_PACKS = 2;
	public static final int HAVE_ALL_PACKS = 3;
	public static final int COMPLETED = 4;

	@Override
	public void readFromClientData(ConnectionImpl connection, ByteBuf clientdata) {
		this.status = clientdata.readByte();
		if (status != REFUSED) {
			int entryCount = clientdata.readShortLE();
			for (int idx = 0; entryCount > idx; idx++) {
				missingPacks.add(StringSerializer.readString(clientdata, connection.getVersion()));
			}
		}
	}

	public int getStatus() {
		return status;
	}

	public ArrayList<String> getMissingPacks() {
		return missingPacks;
	}

}
