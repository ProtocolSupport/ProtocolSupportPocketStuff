//package protocolsupportpocketstuff.zplatform.impl.glowstone;
//
//import org.bukkit.entity.Player;
//
//import com.destroystokyo.paper.profile.ProfileProperty;
//
//import net.glowstone.entity.GlowPlayer;
//import net.glowstone.net.message.play.game.RespawnMessage;
//import net.glowstone.net.message.play.game.UserListItemMessage;
//import net.glowstone.net.message.play.game.UserListItemMessage.Action;
//import protocolsupport.api.Connection;
//import protocolsupport.api.ProtocolSupportAPI;
//import protocolsupportpocketstuff.api.skins.SkinUtils.SkinDataWrapper;
//import protocolsupportpocketstuff.zplatform.PlatformThings;
//
//public class GlowstoneStuff extends PlatformThings {
//
//	@Override
//	public void setSkinProperties(Player player, SkinDataWrapper skindata) {
//		GlowPlayer glowPlayer = (GlowPlayer) player;
//		glowPlayer.getProfile().removeProperty("textures");
//		glowPlayer.getProfile().setProperty(new ProfileProperty("textures", skindata.getValue(), skindata.getSignature()));
//	}
//
//	@SuppressWarnings("deprecation")
//	@Override
//	public void sendPlayerSkin(Player player, SkinDataWrapper skindata) {
//		Connection connection = ProtocolSupportAPI.getConnection(player);
//		GlowPlayer glowPlayer = (GlowPlayer) player;
//		connection.sendPacket(new UserListItemMessage(Action.REMOVE_PLAYER, UserListItemMessage.add(glowPlayer.getProfile())));
//		connection.sendPacket(new RespawnMessage(player.getWorld().getEnvironment().getId(), player.getWorld().getDifficulty().getValue(), player.getGameMode().getValue(), "NORMAL"));
//		player.setHealth(player.getHealth());
//		player.setMaxHealth(player.getMaxHealth());
//		player.setFlying(player.isFlying());
//		player.teleport(player.getLocation());
//		player.setLevel(player.getLevel());
//		player.setExp(player.getExp());
//		player.updateInventory();
//		connection.sendPacket(new UserListItemMessage(Action.ADD_PLAYER, UserListItemMessage.add(glowPlayer.getProfile())));
//	}
//
//}
