package protocolsupportpocketstuff.zplatform.impl.spigot;

import java.lang.reflect.Field;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupportpocketstuff.api.skins.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.zplatform.PlatformThings;

public class SpigotStuff extends PlatformThings {

	@Override
	public void setSkinProperties(Player player, SkinDataWrapper skindata) {
		CraftPlayer craftPlayer = ((CraftPlayer) player);
		EntityHuman entityHuman = craftPlayer.getHandle();
		try {
			Field gp2 = EntityHuman.class.getDeclaredField("h");
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

	@SuppressWarnings("deprecation")
	@Override
	public void sendPlayerSkin(Player player, SkinDataWrapper skindata) {
		Connection connection = ProtocolSupportAPI.getConnection(player);
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
		connection.sendPacket(new PacketPlayOutRespawn(DimensionManager.a(0), entityPlayer.world.getDifficulty(), WorldType.NORMAL, EnumGamemode.getById(player.getGameMode().getValue())));
		player.setHealth(player.getHealth());
		player.setMaxHealth(player.getMaxHealth());
		player.setFlying(player.isFlying());
		player.teleport(player.getLocation());
		player.setLevel(player.getLevel());
		player.setExp(player.getExp());
		player.updateInventory();
		connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
	}

}
