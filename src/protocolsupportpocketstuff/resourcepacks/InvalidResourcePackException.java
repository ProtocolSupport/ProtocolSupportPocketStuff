package protocolsupportpocketstuff.resourcepacks;

public class InvalidResourcePackException extends RuntimeException {
	
	private static final long serialVersionUID = -1813622725947809260L;

	public InvalidResourcePackException(String reason) {
		super(reason);
	}
}
