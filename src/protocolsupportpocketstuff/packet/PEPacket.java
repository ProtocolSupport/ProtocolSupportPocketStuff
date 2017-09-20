package protocolsupportpocketstuff.packet;

import org.bukkit.plugin.PluginManager;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import protocolsupport.api.Connection;
import protocolsupport.api.Connection.PacketListener;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;

public abstract class PEPacket {

	public abstract int getPacketId();
	
	public abstract void toData(Connection connection, ByteBuf serializer);
	
	public abstract void readFromClientData(Connection connection, ByteBuf clientData);
		
	public ByteBuf encode(Connection connection) {
		ByteBuf serializer = Unpooled.buffer();
		VarNumberSerializer.writeVarInt(serializer, getPacketId());
		serializer.writeByte(0);
		serializer.writeByte(0);
		toData(connection, serializer);
		return serializer;
	}
	
	public void decode(Connection connection, ByteBuf clientData) {
		clientData.readByte();
		clientData.readByte();
		readFromClientData(connection, clientData);
	}
	
	public abstract class decodeHandler extends PacketListener {
		
		protected ProtocolSupportPocketStuff plugin;
		protected Connection connection;
		protected PluginManager pm;
		
		public decodeHandler(ProtocolSupportPocketStuff plugin, Connection connection) {
			this.plugin = plugin;
			this.connection = connection;
			this.pm = plugin.getServer().getPluginManager();
		}
		
		public void onRawPacketReceiving(RawPacketEvent e) {
			ByteBuf clientData = e.getData();
			if(VarNumberSerializer.readVarInt(clientData) == PEPacket.this.getPacketId()) {
				PEPacket.this.decode(connection, e.getData());
				handle();
			}
		}
		
		public abstract void handle();
		
	}
	
}
