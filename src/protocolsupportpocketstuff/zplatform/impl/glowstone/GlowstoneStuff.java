package protocolsupportpocketstuff.zplatform.impl.glowstone;

import org.bukkit.entity.Player;

import com.destroystokyo.paper.profile.ProfileProperty;

import net.glowstone.entity.GlowPlayer;

import protocolsupportpocketstuff.api.skins.SkinUtils.SkinDataWrapper;
import protocolsupportpocketstuff.zplatform.PlatformThings;

public class GlowstoneStuff extends PlatformThings {

	@Override
	public void setSkinProperties(Player player, SkinDataWrapper skindata) {
		GlowPlayer glowPlayer = (GlowPlayer) player;
		glowPlayer.getProfile().removeProperty("textures");
		glowPlayer.getProfile().setProperty(new ProfileProperty("textures", skindata.getValue(), skindata.getSignature()));
	}

}
