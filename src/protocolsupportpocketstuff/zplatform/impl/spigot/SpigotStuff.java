package protocolsupportpocketstuff.zplatform.impl.spigot;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_12_R1.EntityHuman;

import protocolsupportpocketstuff.api.skins.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.zplatform.PlatformThings;

public class SpigotStuff extends PlatformThings {

	@Override
	public void setSkinProperties(Player player, SkinDataWrapper skindata) {
		CraftPlayer craftPlayer = ((CraftPlayer) player);
		EntityHuman entityHuman = craftPlayer.getHandle();
		try {
			Field gp2 = entityHuman.getClass().getSuperclass().getDeclaredField("g");
			gp2.setAccessible(true);
			GameProfile profile = (GameProfile) gp2.get(entityHuman);
			profile.getProperties().removeAll("textures");
			profile.getProperties().put("textures", new Property("textures", skindata.getValue(), skindata.getSignature()));
			gp2.set(entityHuman, profile);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
