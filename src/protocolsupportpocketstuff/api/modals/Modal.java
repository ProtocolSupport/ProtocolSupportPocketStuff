package protocolsupportpocketstuff.api.modals;

public interface Modal {
	
	ModalType getType();
	
	String getPeType();
	
	String toJSON();
	
	static Modal fromJson(String json) { return null; }
	
}