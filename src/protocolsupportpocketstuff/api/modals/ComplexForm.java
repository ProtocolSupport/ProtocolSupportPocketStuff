package protocolsupportpocketstuff.api.modals;

import protocolsupport.libs.com.google.gson.Gson;
import protocolsupportpocketstuff.api.modals.elements.ModalImage;
import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;
import protocolsupportpocketstuff.api.modals.elements.complex.ModalComplexUIElement;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;

import java.util.ArrayList;
import java.util.List;

public class ComplexForm implements Modal {
	private final transient ModalType modalType = ModalType.COMPLEX_FORM;
	private final String type = modalType.getPeName();
	
	private String title;
	private List<ModalComplexUIElement> elements = new ArrayList<>();
	private ModalImage iconUrl; // Only for server settings ~ https://sel-utils.github.io/protocol/pocket134/play/server-settings-response

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
		this.elements.add(element);
		return this;
	}

	public ComplexForm addElements(ModalComplexUIElement... elements) {
		for (ModalComplexUIElement element : elements) {
			addElement(element);
		}
		return this;
	}

	public List<ModalComplexUIElement> getElements() {
		return elements;
	}

	public ComplexForm setIcon(ModalImage.ModalImageType imageType, String imagePath) {
		iconUrl = new ModalImage().setType(imageType.getInternalType()).setData(imagePath);
		return this;
	}

	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public static ComplexForm fromJson(String JSON) {
		Gson gson = new Gson(); 
		return gson.fromJson(JSON, ComplexForm.class);
	}
	
}
