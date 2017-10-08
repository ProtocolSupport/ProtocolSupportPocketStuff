package protocolsupportpocketstuff.api.modals.callback;

import org.bukkit.entity.Player;

public abstract class ModalCallback {
	public abstract void onModalResponse(Player player, String modalJSON, boolean isClosedByClient);
}
