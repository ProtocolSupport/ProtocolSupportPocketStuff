package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonElement;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.event.ComplexFormResponseEvent;
import protocolsupportpocketstuff.api.event.ModalResponseEvent;
import protocolsupportpocketstuff.api.event.ModalWindowResponseEvent;
import protocolsupportpocketstuff.api.event.SimpleFormResponseEvent;
import protocolsupportpocketstuff.api.modals.ModalType;
import protocolsupportpocketstuff.api.util.PocketCon;
import protocolsupportpocketstuff.packet.PEPacket;
import protocolsupportpocketstuff.util.StuffUtils;

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
			ModalType modalType = PocketCon.getModalType(connection);
			JsonElement element = StuffUtils.JSON_PARSER.parse(parent.getModalJSON());
			boolean isClosedByClient = element.isJsonNull();

			if (modalType == ModalType.COMPLEX_FORM) {
				ComplexFormResponseEvent event = new ComplexFormResponseEvent(connection, parent.getModalId(), parent.getModalJSON(), modalType, isClosedByClient ? null : element.getAsJsonArray());
				event.setCancelled(isClosedByClient);
				pm.callEvent(event);
				return;
			} else if (modalType == ModalType.MODAL_WINDOW) {
				ModalWindowResponseEvent event = new ModalWindowResponseEvent(connection, parent.getModalId(), parent.getModalJSON(), modalType, isClosedByClient ? false : element.getAsBoolean());
				event.setCancelled(isClosedByClient);
				pm.callEvent(event);
				return;
			} else if (modalType == ModalType.SIMPLE_FORM) {
				SimpleFormResponseEvent event = new SimpleFormResponseEvent(connection, parent.getModalId(), parent.getModalJSON(), modalType, isClosedByClient ? -1 : element.getAsNumber().intValue());
				event.setCancelled(isClosedByClient);
				pm.callEvent(event);
				return;
			}

			ModalResponseEvent event = new ModalResponseEvent(connection, parent.getModalId(), parent.getModalJSON(), modalType);
			event.setCancelled(isClosedByClient);
			pm.callEvent(event);
		}
	}
}
