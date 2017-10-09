package protocolsupportpocketstuff.api.modals;

import protocolsupport.libs.com.google.gson.Gson;
import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.complex.ModalComplexUIElement;
import protocolsupportpocketstuff.util.StuffUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComplexForm implements Modal {
	
	private final transient ModalType modalType = ModalType.COMPLEX_FORM;
	private final String type = modalType.getPeName();
	
	private String title;
	private List<ModalComplexUIElement> content = new ArrayList<>();
	private ModalImage iconUrl; // Only for server settings ~ https://sel-utils.github.io/protocol/pocket134/play/server-settings-response

	public ComplexForm(String title) {
		this.title = title;
	}

	public ModalType getType() {
		return modalType;
	}
	
	public String getPeType() {
		return type;
	}
	
	public ComplexForm setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public ComplexForm addElement(ModalComplexUIElement element) {
		this.content.add(element);
		return this;
	}

	public ComplexForm addElements(ModalComplexUIElement... elements) {
		this.content.addAll(Arrays.asList(elements));
		return this;
	}

	public List<ModalComplexUIElement> getElements() {
		return content;
	}
	
	public ModalImage getIconUrl() {
		return iconUrl;
	}

	public ComplexForm setIcon(ModalImage image) {
		iconUrl = image;
		return this;
	}

	public String toJSON() {
		Gson gson = StuffUtils.GSON;
		return gson.toJson(this);
	}
	
	public static ComplexForm fromJson(String JSON) {
		Gson gson = StuffUtils.GSON;
		return gson.fromJson(JSON, ComplexForm.class);
	}
	
}
