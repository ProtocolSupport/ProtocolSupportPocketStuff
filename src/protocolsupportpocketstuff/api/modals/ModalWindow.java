package protocolsupportpocketstuff.api.modals;

import protocolsupport.libs.com.google.gson.Gson;
import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

import java.util.ArrayList;
import java.util.List;

public class ModalWindow implements Modal {
	
	private final transient ModalType modalType = ModalType.MODAL_WINDOW;
	private final String type = modalType.getPeName();
	
	private String title;
	private String content;
	private String button1;
	private String button2;

	public ModalType getType() {
		return modalType;
	}
	
	public String getPeType() {
		return type;
	}
	
	public ModalWindow setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public ModalWindow setContent(String content) {
		this.content = content;
		return this;
	}

	public String getContent() {
		return content;
	}

	public ModalWindow setTrueButtonText(String text) {
		button1 = text;
		return this;
	}

	public ModalWindow setFalseButtonText(String text) {
		button2 = text;
		return this;
	}

	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	public static ModalWindow fromJson(String JSON) {
		Gson gson = new Gson(); 
		return gson.fromJson(JSON, ModalWindow.class);
	}
	
}
