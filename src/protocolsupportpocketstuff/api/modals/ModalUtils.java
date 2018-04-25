package protocolsupportpocketstuff.api.modals;

import java.util.Optional;

import protocolsupport.api.Connection;

public class ModalUtils {

	private static final String modalTypeKey = "PEModalType";
	private static final String modalCallbackKey = "PEModalCallback";

	/***
	 * Sets the last modalType send to the client.
	 * Used to gather information about the response.
	 * <em>Unless you're actually sending a modal, don't muck with this.</em>
	 * @param connection
	 * @param type
	 */
	public static void setSendType(Connection connection, ModalType type) {
		connection.addMetadata(modalTypeKey, type);
	}

	/***
	 * Gets the last (send) modalType from the client.
	 * <em>This method can only be called once 
	 * as it removes the metadata from client when called.</em>
	 * @param connection
	 * @return the type.
	 */
	public static ModalType getSentType(Connection connection) {
		if (connection.hasMetadata(modalTypeKey)) {
			return (ModalType) connection.removeMetadata(modalTypeKey);
		} else {
			return ModalType.UNKNOWN;
		}
	}

	/***
	 * Sets a callback that is to be called when the player
	 * completes a modal and after the events are handled.
	 * @param connection
	 * @param callback
	 */
	public static void setCallback(Connection connection, ModalCallback callback) {
		connection.addMetadata(modalCallbackKey, callback);
	}

	/***
	 * Gets the optional ModalCallback that is to be called
	 * after the player completes the last sent modal and
	 * the events have been executed.
	 * <em>This method only returns the callback once 
	 * as it removes the metadata from the client when called.</em>
	 * @param connection
	 * @return
	 */
	public static Optional<ModalCallback> getCallback(Connection connection) {
		if (connection.hasMetadata(modalCallbackKey)) {
			return Optional.of((ModalCallback) connection.removeMetadata(modalCallbackKey));
		} else {
			return Optional.empty();
		}
	}

}