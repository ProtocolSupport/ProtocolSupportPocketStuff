package protocolsupportpocketstuff.util.datawatcher.objects;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.utils.datawatcher.ReadableDataWatcherObject;

public class DataWatcherObjectFloatLe extends ReadableDataWatcherObject<Float> {

	public DataWatcherObjectFloatLe() {
	}

	public DataWatcherObjectFloatLe(float value) {
		this.value = value;
	}

	@Override
	public void readFromStream(ByteBuf from) {
		this.value = from.readFloatLE();
	}

	@Override
	public void writeToStream(ByteBuf to, ProtocolVersion version, String locale) {
		to.writeFloatLE(value);
	}

}