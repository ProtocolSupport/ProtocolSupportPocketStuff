package protocolsupportpocketstuff.packet.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.Connection;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
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
	public void readFromClientData(Connection connection, ByteBuf clientdata) {
		modalId = VarNumberSerializer.readVarInt(clientdata);
		modalJSON = StringSerializer.readString(clientdata, connection.getVersion());
	}

	public int getModalId() {
		return modalId;
	}

	public String getModalJSON() {
		return modalJSON;
	}

/**	public class decodeHandler extends PEPacket.decodeHandler {

		public decodeHandler(ProtocolSupportPocketStuff plugin, Connection connection) {
			super(plugin, connection);
		}

		@Override
		public void handle() {
			ModalResponsePacket parent = ModalResponsePacket.this;
			ModalType modalType = PocketCon.getModalType(connection);
			JsonElement element = GsonUtils.JSON_PARSER.parse(parent.getModalJSON());
			boolean isClosedByClient = element.isJsonNull();

			new BukkitRunnable() {
				public void run() {
					// Don't ask me why this is needed, but it is... if we don't do this, an IllegalReferenceCountException is thrown
					Connection _connection = ProtocolSupportAPI.getConnection(connection.getAddress());
					if (modalType == ModalType.COMPLEX_FORM) {
						ComplexFormResponseEvent event = new ComplexFormResponseEvent(_connection, parent.getModalId(), parent.getModalJSON(), modalType, isClosedByClient ? null : element.getAsJsonArray());
						event.setCancelled(isClosedByClient);
						pm.callEvent(event);
						return;
					} else if (modalType == ModalType.MODAL_WINDOW) {
						ModalWindowResponseEvent event = new ModalWindowResponseEvent(_connection, parent.getModalId(), parent.getModalJSON(), modalType, isClosedByClient ? false : element.getAsBoolean());
						event.setCancelled(isClosedByClient);
						pm.callEvent(event);
						return;
					} else if (modalType == ModalType.SIMPLE_FORM) {
						SimpleFormResponseEvent event = new SimpleFormResponseEvent(_connection, parent.getModalId(), parent.getModalJSON(), modalType, isClosedByClient ? -1 : element.getAsNumber().intValue());
						event.setCancelled(isClosedByClient);
						pm.callEvent(event);
						return;
					}

					ModalResponseEvent event = new ModalResponseEvent(_connection, parent.getModalId(), parent.getModalJSON(), modalType);
					event.setCancelled(isClosedByClient);
					pm.callEvent(event);
				}
			}.runTask(ProtocolSupportPocketStuff.getInstance());
		}
	}*/
}
