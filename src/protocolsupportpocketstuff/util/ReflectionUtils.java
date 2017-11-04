package protocolsupportpocketstuff.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
	public static Object get(Field field, Object source) {
		try {
			return field.get(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error while getting field \"" + field.getName() + "\" from " + source + "!");
	}

	public static void set(Field field, Object source, Object value) {
		try {
			field.set(source, value);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error while setting field \"" + field.getName() + "\" from " + source + "!");
	}

	public static int getInt(Field field, Object source) {
		try {
			return field.getInt(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error while getting field \"" + field.getName() + "\" from " + source + "!");
	}

	public static void setInt(Field field, Object source, int value) {
		try {
			field.setInt(source, value);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error while setting field \"" + field.getName() + "\" from " + source + "!");
	}

	public static double getDouble(Field field, Object source) {
		try {
			return field.getDouble(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error while getting field \"" + field.getName() + "\" from " + source + "!");
	}

	public static float getFloat(Field field, Object source) {
		try {
			return field.getFloat(source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error while getting field \"" + field.getName() + "\" from " + source + "!");
	}
}
