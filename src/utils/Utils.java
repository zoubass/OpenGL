package utils;

import java.io.IOException;
import java.io.InputStream;

import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Utils {

	public Texture loadTexture(String name) {
		InputStream is = getClass().getResourceAsStream("../" + name + ".jpg");
		Texture texture = null;
		if (is == null) {
			System.out.println(String.format("File %s.jpg not found", name));
		} else {
			try {
				texture = TextureIO.newTexture(is, true, "jpg");
			} catch (GLException | IOException e) {
				System.out.println(String.format("Failed to load image with name %s.", name));
			}
		}
		return texture;
	}
}
