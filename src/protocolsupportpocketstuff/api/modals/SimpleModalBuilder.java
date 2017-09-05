package protocolsupportpocketstuff.api.modals;

import com.google.gson.Gson;
import protocolsupportpocketstuff.api.modals.elements.ModalUIElement;

import java.util.ArrayList;
import java.util.List;

public class SimpleModalBuilder {
	private String type = "form";
	private String title;
	private String content;
	private List<ModalUIElement> buttons = new ArrayList<>();

	public SimpleModalBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public SimpleModalBuilder setContent(String content) {
		this.content = content;
		return this;
	}

	public String getContent() {
		return content;
	}

	public SimpleModalBuilder addButton(ModalUIElement button) {
		buttons.add(button);
		return this;
	}

	public List<ModalUIElement> getButtons() {
		return buttons;
	}

	public String toJSON() {
		Gson gson = new Gson();

		return gson.toJson(this);
	}
}
