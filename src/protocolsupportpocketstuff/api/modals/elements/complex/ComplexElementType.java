package protocolsupportpocketstuff.api.modals.elements.complex;

public enum ComplexElementType {
	
	DROPDOWN("dropdown"), INPUT("input"), LABEL("label"), SLIDER("slider"), STEP_SLIDER("step_slider"), TOGGLE("toggle");

	private final String peName;

	ComplexElementType(String peName) {
		this.peName = peName;
	}

	public String getPeName() {
		return peName;
	}
	
}
