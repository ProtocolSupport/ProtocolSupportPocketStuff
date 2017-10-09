package protocolsupportpocketstuff.api.modals.callback;

import org.bukkit.entity.Player;

public abstract class SimpleFormCallback extends ModalCallback {
	public abstract void onSimpleFormResponse(Player player, String modalJSON, boolean isClosedByClient, int clickedButton);

	@Override
	public void onModalResponse(Player player, String modalJSON, boolean isClosedByClient) {
	}
}
