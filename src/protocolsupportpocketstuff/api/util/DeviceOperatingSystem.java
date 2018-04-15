package protocolsupportpocketstuff.api.util;

public enum DeviceOperatingSystem {

	ANDROID(1),
	IOS(2),
	OSX(3),
	FIRE_OS(4),
	GEAR_VR(5),
	HOLOLENS(6),
	WINDOWS_10(7),
	WINDOWS(8),
	DEDICATED(9),
	ORBIS(10),
	NINTENDO_SWITCH(11),
	UNKNOWN(-1);

	private int id;

	DeviceOperatingSystem(int id) {
		this.id = id;
	}

	public static DeviceOperatingSystem getOperatingSystemById(int id) {
		for (DeviceOperatingSystem os : values()) {
			if (os.id == id) {
				return os;
			}
		}
		return DeviceOperatingSystem.UNKNOWN;
	}

}
