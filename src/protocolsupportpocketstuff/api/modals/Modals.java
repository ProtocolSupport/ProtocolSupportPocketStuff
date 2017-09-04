package protocolsupportpocketstuff.api.modals;

import java.util.concurrent.ConcurrentHashMap;

public class Modals {

	private static ConcurrentHashMap<Integer, Modal> modals = new ConcurrentHashMap<Integer, Modal>();
	
	/***
	 * Replaces / updates a modal.
	 * @param id
	 * @param modal
	 * @return the old Modal or null, if none was present.
	 */
	public static Modal replace(int id, Modal modal) {
		return modals.replace(id, modal);
	}
	
	/***
	 * Registers a modal for sending and listening.
	 * @param modal
	 * @return the id of the modal added.
	 */
	public static int register(Modal modal) {
		modals.put(modals.size(), modal);
		return modals.size() - 1;
	}
	
	/***
	 * Gets a modal from the registry.
	 * @param id
	 * @return the modal of the id given or null, if no modal is found.
	 */
	public static Modal get(int id) {
		return modals.get(id);
	}
	
}
