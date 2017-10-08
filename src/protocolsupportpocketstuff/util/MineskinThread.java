package protocolsupportpocketstuff.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_12_R1.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import protocolsupport.api.Connection;
import protocolsupport.libs.com.google.gson.JsonObject;
import protocolsupportpocketstuff.ProtocolSupportPocketStuff;
import protocolsupportpocketstuff.api.util.SkinUtils;
import protocolsupportpocketstuff.libs.kevinsawicki.http.HttpRequest;
import protocolsupportpocketstuff.storage.Skins;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

public class MineskinThread extends Thread {
	Connection connection;
	String uniqueSkinId;
	byte[] skinByteArray;
	boolean isSlim;

	public MineskinThread(Connection connection, String uniqueSkinId, byte[] skinByteArray, boolean isSlim) {
		this.connection = connection;
		this.uniqueSkinId = uniqueSkinId;
		this.skinByteArray = skinByteArray;
		this.isSlim = isSlim;
	}

	@Override
	public void run() {
		super.run();
		System.out.println("Sending skin " + uniqueSkinId + " to MineSkin...");
		BufferedImage skin = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);

		int x = 0;
		int y = 0;
		for (int i = 0; skinByteArray.length > i; i += 4) {
			if (x == 64) {
				x = 0;
				y++;
			}
			int r = (skinByteArray[i]) & 0xFF;
			int g = (skinByteArray[i + 1]) & 0xFF;
			int b = (skinByteArray[i + 2]) & 0xFF;
			int a = (skinByteArray[i + 3]) & 0xFF;

			skin.setRGB(x, y, new Color(r, g, b, a).getRGB());
			x++;
		}

		HttpRequest httpRequest = HttpRequest.post("http://api.mineskin.org/generate/upload?name=&model=" + (isSlim ? "slim" : "steve") + "&visibility=1")
				.userAgent("ProtocolSupportPocketStuff");

		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(skin, "png", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			httpRequest.part("file", "mcpe_skin.png", null, is);

			JsonObject mineskinResponse = StuffUtils.JSON_PARSER.parse(httpRequest.body()).getAsJsonObject();

			System.out.println(mineskinResponse);

			JsonObject skinData = mineskinResponse.get("data").getAsJsonObject();
			JsonObject skinTexture = skinData.get("texture").getAsJsonObject();
			String signature = skinTexture.get("signature").getAsString();
			String value = skinTexture.get("value").getAsString();

			System.out.println("Storing skin on cache...");
			Skins.INSTANCE.cachePcSkin(uniqueSkinId, new SkinUtils.SkinDataWrapper(value, signature, isSlim));
			hackyStuff(connection, value, signature);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hackyStuff(Connection connection, String value, String signature) {
		// Wait until the player is logged in
		while (connection.getPlayer() == null) {
			if (!connection.isConnected()) {
				return;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Player is logged in, applying skin...");

		Player player = connection.getPlayer();
		CraftPlayer craftPlayer = ((CraftPlayer) player);
		EntityHuman entityHuman = craftPlayer.getHandle();

		try {
			Field gp2 = entityHuman.getClass().getSuperclass().getDeclaredField("g");
			gp2.setAccessible(true);
			GameProfile profile = (GameProfile) gp2.get(entityHuman);
			profile.getProperties().put("textures", new Property("textures", value, signature));
			gp2.set(entityHuman, profile);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		//triggers an update for others player to see the new skin
		new BukkitRunnable() {
			public void run() {
				Bukkit.getOnlinePlayers().stream()
						.filter(onlinePlayer -> !onlinePlayer.equals(player))
						.filter(onlinePlayer -> onlinePlayer.canSee(player))
						.forEach(onlinePlayer -> {
							//removes the entity and display the new skin
							onlinePlayer.hidePlayer(player);
							onlinePlayer.showPlayer(player);
						});
			}
		}.runTask(ProtocolSupportPocketStuff.getInstance());
	}
}
