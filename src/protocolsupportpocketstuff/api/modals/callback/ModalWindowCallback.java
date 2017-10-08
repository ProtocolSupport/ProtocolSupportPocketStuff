package protocolsupportpocketstuff.api.modals.callback;

import org.bukkit.entity.Player;

public abstract class ModalWindowCallback extends ModalCallback {
	public abstract void onModalWindowResponse(Player player, String modalJSON, boolean result);

	@Override
	public void onModalResponse(Player player, String modalJSON) {
	}
}
