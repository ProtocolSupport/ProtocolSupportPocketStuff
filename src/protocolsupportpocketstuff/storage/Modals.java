package protocolsupportpocketstuff.storage;

public class Modals {

	private int id = 0;

	private Modals() { }
	private static final Modals INSTANCE = new Modals();
	public static Modals getInstance() {
		return INSTANCE;
	}
	
	public int takeId() {
		return id++;
	}

}
