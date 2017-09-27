package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonElement;
import protocolsupport.libs.com.google.gson.JsonParser;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.event.ComplexFormResponseEvent;
import protocolsupportpocketstuff.api.event.ModalWindowResponseEvent;
import protocolsupportpocketstuff.api.event.SimpleFormResponseEvent;
import protocolsupportpocketstuff.packet.PEPacket;

public class ModalResponsePacket extends PEPacket {

	private int modalId;
	private String modalJSON;
	
	public ModalResponsePacket() { }
	
	public ModalResponsePacket(int modalId, String modalJSON) {
		this.modalId = modalId;
		this.modalJSON = modalJSON;
	}

	@Override
	public int getPacketId() {
		return PEPacketIDs.MODAL_RESPONSE;
	}

	@Override
	public void toData(Connection connection, ByteBuf serializer) {
		VarNumberSerializer.writeVarInt(serializer, modalId);
		StringSerializer.writeString(serializer, connection.getVersion(), modalJSON);
	}

	@Override
	public void readFromClientData(Connection connection, ByteBuf clientData) {
		modalId = VarNumberSerializer.readVarInt(clientData);
		modalJSON = StringSerializer.readString(clientData, connection.getVersion());
	}
	
	public int getModalId() {
		return modalId;
	}
	
	public String getModalJSON() {
		return modalJSON;
	}

	public class decodeHandler extends PEPacket.decodeHandler {

		public decodeHandler(ProtocolSupportPocketStuff plugin, Connection connection) {
			super(plugin, connection);
		}

		@Override
		public void handle() {
			ModalResponsePacket parent = ModalResponsePacket.this;

			JsonElement element = new JsonParser().parse(parent.getModalJSON());

			if (element.isJsonArray()) {
				pm.callEvent(new ComplexFormResponseEvent(connection,
						parent.getModalId(), element.getAsJsonArray()));
			} else if (element.isJsonPrimitive()) {
				if (element.getAsJsonPrimitive().isBoolean()) {
					pm.callEvent(new ModalWindowResponseEvent(connection,
							parent.getModalId(), element.getAsBoolean()));
				} else if (element.getAsJsonPrimitive().isNumber()) {
					pm.callEvent(new SimpleFormResponseEvent(connection,
							parent.getModalId(), element.getAsNumber().intValue()));
				} else {
					throw new RuntimeException("Invalid form response! " + element);
				}
			}
		}
	}
}
