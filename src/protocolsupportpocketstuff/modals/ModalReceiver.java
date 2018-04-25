package protocolsupportpocketstuff.modals;

import org.bukkit.Bukkit;

import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonElement;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.event.ComplexFormResponseEvent;
import protocolsupportpocketstuff.api.event.ModalResponseEvent;
import protocolsupportpocketstuff.api.event.ModalWindowResponseEvent;
import protocolsupportpocketstuff.api.event.SimpleFormResponseEvent;
import protocolsupportpocketstuff.api.modals.ModalType;
import protocolsupportpocketstuff.api.modals.ModalUtils;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.play.ModalResponsePacket;
import protocolsupportpocketstuff.util.GsonUtils;

public class ModalReceiver implements PocketPacketListener {

	@PocketPacketHandler
	public void onModalResponse(Connection connection, ModalResponsePacket packet) {
		ModalType type = ModalUtils.getSentType(connection);
		JsonElement element = GsonUtils.JSON_PARSER.parse(packet.getModalJSON());
		ModalResponseEvent responseEvent;
		switch(type) {
			case MODAL_WINDOW: {
				responseEvent = new ModalWindowResponseEvent(connection, packet.getModalId(), packet.getModalJSON(), type, element.getAsBoolean());
				break;
			}
			case SIMPLE_FORM: {
				responseEvent = new SimpleFormResponseEvent(connection, packet.getModalId(), packet.getModalJSON(), type, element.getAsInt());
				break;
			}
			case COMPLEX_FORM: {
				responseEvent = new ComplexFormResponseEvent(connection, packet.getModalId(), packet.getModalJSON(), type, element.getAsJsonArray());
				break;
			}
			default:
			case UNKNOWN: {
				responseEvent = new ModalResponseEvent(connection, packet.getModalId(), packet.getModalJSON(), type);
			}
		}
		Bukkit.getScheduler().runTask(ProtocolSupportPocketStuff.getInstance(), () -> {
			Bukkit.getPluginManager().callEvent(responseEvent);
			ModalUtils.getCallback(connection).ifPresent(consumer -> consumer.accept(responseEvent));
		});
	}

}
