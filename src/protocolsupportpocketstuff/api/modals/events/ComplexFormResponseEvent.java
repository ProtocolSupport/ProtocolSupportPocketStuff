package protocolsupportpocketstuff.api.modals.events;

import java.util.List;

import protocolsupport.api.Connection;
import protocolsupportpocketstuff.api.modals.ModalType;
import protocolsupportpocketstuff.api.modals.elements.ElementResponse;

/***
 * This event is called when a player completes a {@link ComplexModal}
 * We still need to add response api for this, but for now you can hack something with the jsonarray :D
 */
public class ComplexFormResponseEvent extends ModalResponseEvent {

	private List<ElementResponse> responses;

	/***
	 * Creates a ComplexFormResponseEvent
	 * @param connection
	 * @param modalId
	 * @param modalJSON
	 * @param modalType
	 * @param jsonArray
	 */
	public ComplexFormResponseEvent(Connection connection, int modalId, String modalJSON, ModalType modalType, List<ElementResponse> responses) {
		super(connection, modalId, modalJSON, modalType);
		this.responses = responses;
	}

	/***
	 * Gets the responses as a list.
	 * @return
	 */
	public List<ElementResponse> getResponses() {
		return responses;
	}

	/***
	 * Gets the modal response beloning to the index of that element.
	 * @param index
	 * @return the response.
	 */
	public ElementResponse getResponse(int index) {
		return responses.get(index);
	}

	/***
	 * Sets the Response list to muck with other handlers.
	 * @param responses
	 */
	public void setResponses(List<ElementResponse> responses) {
		this.responses = responses;
	}

}
