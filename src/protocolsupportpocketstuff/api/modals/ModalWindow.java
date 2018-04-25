package protocolsupportpocketstuff.api.modals;

import protocolsupportpocketstuff.util.GsonUtils;

/***
 * Implementation of MODAL_WINDOW.
 * Used to build and send ModalWindow modals.
 * A modal window is a simple dialog with
 * adjustable true / false buttons.
 */
public class ModalWindow implements Modal {

	private final transient ModalType modalType = ModalType.MODAL_WINDOW;
	private final String type = modalType.getPeName();

	private String title;
	private String content;
	private String button1;
	private String button2;

	/***
	 * Constructs a ModalWindow with the required parameters.
	 * @param title - The title of the modal.
	 * @param content - The text / question to display.
	 * @param trueButtonText - The text of the trueButton.
	 * @param falseButtonText - The text of the falseButton.
	 */
	public ModalWindow(String title, String content, String trueButtonText, String falseButtonText) {
		this.title = title;
		this.content = content;
		this.button1 = trueButtonText;
		this.button2 = falseButtonText;
	}

	/***
	 * @return the ModalType of this modal.
	 */
	public ModalType getType() {
		return modalType;
	}

	/***
	 * @return the internal type of this modal.
	 */
	public String getPeType() {
		return type;
	}

	/***
	 * Sets the title of this Modal.
	 * @param title
	 * @return this
	 */
	public ModalWindow setTitle(String title) {
		this.title = title;
		return this;
	}

	/***
	 * @return the title of the modal.
	 */
	public String getTitle() {
		return title;
	}

	/***
	 * Set the content of this modal.
	 * @param content
	 * @return this
	 */
	public ModalWindow setContent(String content) {
		this.content = content;
		return this;
	}

	/***
	 * @return the content of this modal.
	 */
	public String getContent() {
		return content;
	}

	/***
	 * Sets the text of the true button.
	 * @param text
	 * @return this
	 */
	public ModalWindow setTrueButtonText(String text) {
		button1 = text;
		return this;
	}

	/***
	 * Sets the text of the false button.
	 * @param text
	 * @return this
	 */
	public ModalWindow setFalseButtonText(String text) {
		button2 = text;
		return this;
	}

	/**
	 * @return the true button's text.
	 */
	public String getTrueButtonText() {
		return button1;
	}

	/**
	 * @return the false button's text.
	 */
	public String getFalseButtonText() {
		return button2;
	}

	/**
	 * Fully clones the ModalWindow.
	 * You don't need to clone to resent a modal,
	 * but it can be useful for construction.
	 * @return a new ModalWindow.
	 */
	public ModalWindow clone() {
		return new ModalWindow(getTitle(), getContent(), getTrueButtonText(), getFalseButtonText());
	}

	/***
	 * Converts this modal to JSON.
	 * @return the JSON representation of this modal.
	 */
	public String toJSON() {
		return GsonUtils.GSON.toJson(this);
	}

	/***
	 * Creates a ModalWindow from JSON string.
	 * @param JSON
	 * @return a new ModalWindow.
	 */
	public static ModalWindow fromJson(String JSON) {
		return GsonUtils.GSON.fromJson(JSON, ModalWindow.class);
	}

}
