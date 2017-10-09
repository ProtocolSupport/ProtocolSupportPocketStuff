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
		BufferedImage skin = SkinUtils.fromData(skinByteArray);

		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(skin, "png", os);
			InputStream inputStream = new ByteArrayInputStream(os.toByteArray());

			int tries = 0;

			JsonObject mineskinResponse = sendToMineSkin(inputStream, isSlim);

			System.out.println("[#" + (tries + 1) + "] " + mineskinResponse);

			while (mineskinResponse.has("error")) {
				if (!connection.isConnected()) {
					System.out.println("[#" + (tries + 1) + "] Failed again... but the client disconnected, so we are going to ignore the skin!");
					return;
				}
				if (tries > 4) {
					System.out.println("[#" + (tries + 1) + "] Too many fails, aborting... sorry... :(");
					return;
				}
				System.out.println("[#" + (tries + 1) + "] Failed to send skin! Retrying again in 1s...");
				Thread.sleep(1000); // Throttle
				inputStream = new ByteArrayInputStream(os.toByteArray());
				mineskinResponse = sendToMineSkin(inputStream, isSlim);
				System.out.println("[#" + (tries + 1) + "] " + mineskinResponse);
				tries++;
			}
			inputStream.close();

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

	public static JsonObject sendToMineSkin(InputStream inputStream, boolean isSlim) {
		HttpRequest httpRequest = HttpRequest.post("http://api.mineskin.org/generate/upload?name=&model=" + (isSlim ? "slim" : "steve") + "&visibility=1")
				.userAgent("ProtocolSupportPocketStuff");
		httpRequest.part("file", "mcpe_skin.png", null, inputStream);
		return StuffUtils.JSON_PARSER.parse(httpRequest.body()).getAsJsonObject();
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
