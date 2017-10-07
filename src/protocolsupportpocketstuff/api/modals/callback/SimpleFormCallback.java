package protocolsupportpocketstuff.api.modals.callback;

import org.bukkit.entity.Player;

public abstract class SimpleFormCallback extends ModalCallback {
	public abstract void onSimpleFormResponse(Player player, String modalJSON, int clickedButton);

	@Override
	public void onModalResponse(Player player, String modalJSON) {
	}
}
