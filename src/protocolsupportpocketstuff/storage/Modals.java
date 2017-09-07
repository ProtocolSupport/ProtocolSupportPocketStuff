package protocolsupportpocketstuff.storage;

public class Modals {

	private int id = 0;
	
	public static final Modals INSTANCE = new Modals();
	private Modals() { }
	
	public int takeId() {
		return id++;
	}
	
}
