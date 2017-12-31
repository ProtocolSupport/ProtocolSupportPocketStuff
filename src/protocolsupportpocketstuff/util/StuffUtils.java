package protocolsupportpocketstuff.util;

import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.ChatModifier;
import net.minecraft.server.v1_12_R1.EnumChatFormat;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import org.bukkit.ChatColor;
import protocolsupport.libs.com.google.gson.Gson;
import protocolsupport.libs.com.google.gson.JsonParser;

import java.util.List;

public class StuffUtils {
	public static final Gson GSON = new Gson();
	public static final JsonParser JSON_PARSER = new JsonParser();
	public static final int CHUNK_SIZE = 1048576;
	public static final String SKIN_PROPERTY_NAME = "textures";
	public static final String APPLY_SKIN_ON_JOIN_KEY = "applySkinOnJoin";
	public static final String CLIENT_INFO_KEY = "clientInformationMap";
	public static final String CLIENT_UUID_KEY = "clientUniqueId";

	public static String toLegacy(IChatBaseComponent s) {
		StringBuilder builder = new StringBuilder();
		legacy(builder, s);
		return builder.toString();
	}

	private static void legacy(StringBuilder builder, IChatBaseComponent s) {
		ChatModifier modifier = s.getChatModifier();
		colorize(builder, modifier);
		if (s instanceof ChatComponentText) {
			builder.append(((ChatComponentText) s).g());
		} else
			throw new RuntimeException("Unhandled type: " + s.getClass().getSimpleName());

		for (IChatBaseComponent c : getExtra(s)) {
			legacy(builder, c);
		}
	}

	private static void colorize(StringBuilder builder, ChatModifier modifier) {
		if (modifier == null)
			return;
		// Color first
		EnumChatFormat color = getColor(modifier);
		if (color == null) {
			color = EnumChatFormat.BLACK;
		}
		builder.append(color.toString());

		if (isBold(modifier)) {
			builder.append(ChatColor.BOLD);
		}
		if (isItalic(modifier)) {
			builder.append(ChatColor.ITALIC);
		}
		if (isRandom(modifier)) {
			builder.append(ChatColor.MAGIC);
		}
		if (isStrikethrough(modifier)) {
			builder.append(ChatColor.STRIKETHROUGH);
		}
		if (isUnderline(modifier)) {
			builder.append(ChatColor.UNDERLINE);
		}
	}

	// Helpers
	private static List<IChatBaseComponent> getExtra(IChatBaseComponent c) {
		return c.a();
	}

	private static EnumChatFormat getColor(ChatModifier c) {
		return c.getColor();
	}

	private static boolean isBold(ChatModifier c) {
		return c.isBold();
	}

	private static boolean isItalic(ChatModifier c) {
		return c.isItalic();
	}

	private static boolean isStrikethrough(ChatModifier c) {
		return c.isStrikethrough();
	}

	private static boolean isUnderline(ChatModifier c) {
		return c.isUnderlined();
	}

	private static boolean isRandom(ChatModifier c) {
		return c.isRandom();
	}
}
