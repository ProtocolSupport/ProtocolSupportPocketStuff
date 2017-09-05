package protocolsupportpocketstuff.api.modals;

public interface Modal {
	
	public ModalType getType();
	
	public String getPeType();
	
	public String toJSON();
	
	public static Modal fromJson(String json) { return null; }
	
}