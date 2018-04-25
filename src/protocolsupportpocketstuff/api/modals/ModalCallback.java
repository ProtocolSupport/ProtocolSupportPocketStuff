package protocolsupportpocketstuff.api.modals;

import java.util.function.Consumer;

import protocolsupportpocketstuff.api.modals.events.ModalResponseEvent;

/***
 * Callback for modals.
 * Callbacks are handled after events.
 */
public interface ModalCallback extends Consumer<ModalResponseEvent> { }