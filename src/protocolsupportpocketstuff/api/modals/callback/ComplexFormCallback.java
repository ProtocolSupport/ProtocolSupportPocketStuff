package protocolsupportpocketstuff.api.modals.callback;

import org.bukkit.entity.Player;
import protocolsupport.libs.com.google.gson.JsonArray;

public abstract class ComplexFormCallback extends ModalCallback {
	public abstract void onComplexFormResponse(Player player, String modalJSON, boolean isClosedByClient, JsonArray jsonArray);

	@Override
	public void onModalResponse(Player player, String modalJSON, boolean isClosedByClient) {
	}
}
