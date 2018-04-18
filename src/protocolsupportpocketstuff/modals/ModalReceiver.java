package protocolsupportpocketstuff.modals;

import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.util.PocketPacketHandler;
import protocolsupportpocketstuff.api.util.PocketPacketListener;
import protocolsupportpocketstuff.packet.play.ModalResponsePacket;

public class ModalReceiver implements PocketPacketListener {

	@PocketPacketHandler
	public void onModalResponse(Connection connection, ModalResponsePacket packet) {
		
	}

}
