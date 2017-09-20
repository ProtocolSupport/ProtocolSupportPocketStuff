package protocolsupportpocketstuff.api.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import org.apache.commons.lang3.Validate;

public class SkinUtils {

	//TODO: Why here? :S
	public static String skinPropertyName = "textures";
	
	
	public static BufferedImage fromData(byte[] data) {
		System.out.println(data.length);
		Validate.isTrue((data.length == 8192) || (data.length == 16384), "Skin data must be either 8192 or 16384 bytes long!");
		int width = 64, height = (data.length == 16384) ? 64 : 32;
		ByteArrayInputStream stream = new ByteArrayInputStream(data);
		BufferedImage skin = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = new Color(stream.read(), stream.read(), stream.read(), stream.read());
				skin.setRGB(x, y, color.getRGB());;
			}
		}
		return skin;
	}
	
}
