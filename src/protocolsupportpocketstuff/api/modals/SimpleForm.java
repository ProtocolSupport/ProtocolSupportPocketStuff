package protocolsupportpocketstuff.api.modals;

import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;
import protocolsupportpocketstuff.util.GsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Implementation of SIMPLE_FORM.
 * Used to build and send SimpleForm modals.
 * A simple form has a title, some text
 * and a variable amount of buttons.
 */
public class SimpleForm implements Modal {

	private final transient ModalType modalType = ModalType.SIMPLE_FORM;
	private final String type = modalType.getPeName();

	private String title;
	private String content;
	private List<ModalButton> buttons = new ArrayList<>();

	/***
	 * Constructs a SimpleForm with the required parameters.
	 * @param title - The title of the modal.
	 * @param content - The text / question to display.
	 */
	public SimpleForm(String title, String content) {
		this.title = title;
		this.content = content;
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
	public SimpleForm setTitle(String title) {
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
	public SimpleForm setContent(String content) {
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
	 * Adds a button to the SimpleForm.
	 * @param button
	 * @return this
	 */
	public SimpleForm addButton(ModalButton button) {
		buttons.add(button);
		return this;
	}

	/***
	 * Adds multiple buttons to the SimpleForm.
	 * @param buttons
	 * @return this
	 */
	public SimpleForm addButtons(ModalButton... buttons) {
		this.buttons.addAll(Arrays.asList(buttons));
		return this;
	}

	/***
	 * Gets all buttons currently inside the modal.
	 * @return the buttons.
	 */
	public List<ModalButton> getButtons() {
		return buttons;
	}

	/***
	 * Fully clones the SimpleForm.
	 * You don't need to clone to resent a modal,
	 * but it can be useful for construction.
	 * @return a new SimpleForm.
	 */
	public SimpleForm clone() {
		SimpleForm clone = new SimpleForm(getTitle(), getContent());
		getButtons().forEach(button -> clone.addButton(button));
		return clone;
	}

	/***
	 * Converts this modal to JSON.
	 * @return the JSON representation of this modal.
	 */
	public String toJSON() {
		return GsonUtils.GSON.toJson(this);
	}

	/***
	 * Creates a SimpleForm from JSON string.
	 * @param JSON
	 * @return a new SimpleForm.
	 */
	public static SimpleForm fromJson(String JSON) {
		return GsonUtils.GSON.fromJson(JSON, SimpleForm.class);
	}

}
