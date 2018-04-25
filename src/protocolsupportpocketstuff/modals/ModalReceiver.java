package protocolsupportpocketstuff.modals;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonElement;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.modals.ModalType;
import protocolsupportpocketstuff.api.modals.ModalUtils;
import protocolsupportpocketstuff.api.modals.elements.ElementResponse;
import protocolsupportpocketstuff.api.modals.events.ComplexFormResponseEvent;
import protocolsupportpocketstuff.api.modals.events.ModalResponseEvent;
import protocolsupportpocketstuff.api.modals.events.ModalWindowResponseEvent;
import protocolsupportpocketstuff.api.modals.events.SimpleFormResponseEvent;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.play.ModalResponsePacket;
import protocolsupportpocketstuff.util.GsonUtils;

public class ModalReceiver implements PocketPacketListener {

	@PocketPacketHandler
	public void onModalResponse(Connection connection, ModalResponsePacket packet) {
		ModalType type = ModalUtils.getSentType(connection);
		JsonElement element = GsonUtils.JSON_PARSER.parse(packet.getModalJSON());
		boolean isClosed = element.isJsonNull();
		ModalResponseEvent responseEvent;
		switch(type) {
			case MODAL_WINDOW: {
				responseEvent = new ModalWindowResponseEvent(connection, packet.getModalId(), packet.getModalJSON(), type, isClosed ? false : element.getAsBoolean());
				break;
			}
			case SIMPLE_FORM: {
				responseEvent = new SimpleFormResponseEvent(connection, packet.getModalId(), packet.getModalJSON(), type, isClosed ? 0 : element.getAsInt());
				break;
			}
			case COMPLEX_FORM: {
				List<ElementResponse> responses = new ArrayList<>();
				if (!isClosed) {
					element.getAsJsonArray().forEach(elementResponse -> responses.add(new ElementResponse(elementResponse)));
				}
				responseEvent = new ComplexFormResponseEvent(connection, packet.getModalId(), packet.getModalJSON(), type, responses);
				break;
			}
			default:
			case UNKNOWN: {
				responseEvent = new ModalResponseEvent(connection, packet.getModalId(), packet.getModalJSON(), type);
			}
		}
		responseEvent.setCancelled(isClosed);
		Bukkit.getScheduler().runTask(ProtocolSupportPocketStuff.getInstance(), () -> {
			Bukkit.getPluginManager().callEvent(responseEvent);
			ModalUtils.getCallback(connection).ifPresent(consumer -> consumer.accept(responseEvent));
		});
	}

}
