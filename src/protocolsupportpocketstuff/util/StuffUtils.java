package protocolsupportpocketstuff.util;

import org.apache.commons.io.IOUtils;
import protocolsupport.protocol.utils.types.Position;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StuffUtils {
	public static final int CHUNK_SIZE = 1048576;
	public static final String SKIN_PROPERTY_NAME = "textures";
	public static final String APPLY_SKIN_ON_JOIN_KEY = "applySkinOnJoin";
	public static final String CLIENT_INFO_KEY = "PEClientInformationMap";
	public static final String CLIENT_UUID_KEY = "clientUniqueId";
	private static final String RESOURCES_DIRECTORY = "resources";

	public static BufferedReader getResource(String name) {
		return new BufferedReader(new InputStreamReader(getResourceAsStream(name), StandardCharsets.UTF_8));
	}

	public static String getResourceAsString(String name) {
		try {
			return IOUtils.toString(getResource(name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream getResourceAsStream(String name) {
		return ProtocolSupportPocketStuff.class.getClassLoader().getResourceAsStream(RESOURCES_DIRECTORY + "/" + name);
	}

	public static long convertPositionToLong(Position position) {
		return convertCoordinatesToLong(position.getX(), position.getY(), position.getZ());
	}

	public static long convertCoordinatesToLong(int x, int y, int z) {
		return ((x & 0x3FFFFFFL) << 38) | ((y & 0xFFFL) << 26) | ((z & 0x3FFFFFFL));
	}

	// TODO Remove hack / spigot code from this class!
//	public static String toLegacy(IChatBaseComponent s) {
//		StringBuilder builder = new StringBuilder();
//		legacy(builder, s);
//		return builder.toString();
//	}
//
//	private static void legacy(StringBuilder builder, IChatBaseComponent s) {
//		ChatModifier modifier = s.getChatModifier();
//		colorize(builder, modifier);
//		if (s instanceof ChatComponentText) {
//			builder.append(((ChatComponentText) s).g());
//		} else
//			throw new RuntimeException("Unhandled type: " + s.getClass().getSimpleName());
//
//		for (IChatBaseComponent c : getExtra(s)) {
//			legacy(builder, c);
//		}
//	}
//
//	private static void colorize(StringBuilder builder, ChatModifier modifier) {
//		if (modifier == null)
//			return;
//		// Color first
//		EnumChatFormat color = getColor(modifier);
//		if (color == null) {
//			color = EnumChatFormat.BLACK;
//		}
//		builder.append(color.toString());
//
//		if (isBold(modifier)) {
//			builder.append(ChatColor.BOLD);
//		}
//		if (isItalic(modifier)) {
//			builder.append(ChatColor.ITALIC);
//		}
//		if (isRandom(modifier)) {
//			builder.append(ChatColor.MAGIC);
//		}
//		if (isStrikethrough(modifier)) {
//			builder.append(ChatColor.STRIKETHROUGH);
//		}
//		if (isUnderline(modifier)) {
//			builder.append(ChatColor.UNDERLINE);
//		}
//	}
//
//	// Helpers
//	private static List<IChatBaseComponent> getExtra(IChatBaseComponent c) {
//		return c.a();
//	}
//
//	private static EnumChatFormat getColor(ChatModifier c) {
//		return c.getColor();
//	}
//
//	private static boolean isBold(ChatModifier c) {
//		return c.isBold();
//	}
//
//	private static boolean isItalic(ChatModifier c) {
//		return c.isItalic();
//	}
//
//	private static boolean isStrikethrough(ChatModifier c) {
//		return c.isStrikethrough();
//	}
//
//	private static boolean isUnderline(ChatModifier c) {
//		return c.isUnderlined();
//	}
//
//	private static boolean isRandom(ChatModifier c) {
//		return c.isRandom();
//	}
}
