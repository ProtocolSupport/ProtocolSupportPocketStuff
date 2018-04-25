package protocolsupportpocketstuff.api.modals;

import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.complex.ComplexModalUIElement;
import protocolsupportpocketstuff.util.GsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Implementation of COMPLEX_FORM.
 * Used to build and send ComplexForm modals.
 * A complex form can have ComplexUIElements
 * (such as sliders, dropdowns, input fields, buttons, etc)
 * but always has at least a title.
 */
public class ComplexForm implements Modal {

	private final transient ModalType modalType = ModalType.COMPLEX_FORM;
	private final String type = modalType.getPeName();

	private String title;
	private List<ComplexModalUIElement> content = new ArrayList<>();
	private ModalImage iconUrl; // Only for server settings ~ https://sel-utils.github.io/protocol/pocket134/play/server-settings-response

	/***
	 * Constructs a ComplexForm with the required parameters.
	 * @param title - The title of the modal.
	 */
	public ComplexForm(String title) {
		this.title = title;
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
	public ComplexForm setTitle(String title) {
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
	 * Adds a ModalElement to the ComplexForm.
	 * @param element
	 * @return this
	 */
	public ComplexForm addElement(ComplexModalUIElement element) {
		this.content.add(element);
		return this;
	}

	/***
	 * Adds multiple ModalElements to the ComplexForm.
	 * @param elements
	 * @return this
	 */
	public ComplexForm addElements(ComplexModalUIElement... elements) {
		this.content.addAll(Arrays.asList(elements));
		return this;
	}

	/***
	 * Gets the Complex Elements currently inside the modal.
	 * @return the elements.
	 */
	public List<ComplexModalUIElement> getElements() {
		return content;
	}

	/***
	 * Gets the iconUrl of the modal. <br/>
	 * <em>NOTE: This is only for when used inside server settings!</em>
	 * @return the current icon.
	 */
	public ModalImage getIconUrl() {
		return iconUrl;
	}

	/***
	 * Sets the icon of this modal. <br/>
	 * <em>NOTE: This is only for when used inside server settings!</em>
	 * @param image
	 * @return this
	 */
	public ComplexForm setIcon(ModalImage image) {
		iconUrl = image;
		return this;
	}

	/***
	 * Fully clones the ComplexForm.
	 * You don't need to clone to resent a modal,
	 * but it can be useful for construction.
	 * @return a new ComplexForm.
	 */
	public ComplexForm clone() {
		ComplexForm clone = new ComplexForm(getTitle());
		getElements().forEach(element -> clone.addElement(element.clone()));
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
	 * Creates a ComplexForm from JSON string.
	 * @param JSON
	 * @return a new ComplexForm.
	 */
	public static ComplexForm fromJson(String JSON) {
		return GsonUtils.GSON.fromJson(JSON, ComplexForm.class);
	}

}
