package lib;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image {
	
	private BufferedImage image;

	public Image(String path) {
		try {
			image = ImageIO.read(Filesystem.getURL(path));
			System.out.println("[Image] " + path + " loaded.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void draw(Graphics2D g, double x, double y) {
		g.drawImage(image, (int)x, (int)y, null);
	}
	
	protected void drawq(Graphics2D g, Rectangle quad, double x, double y) {
		g.drawImage(image, (int)x, (int)y, (int)x + quad.width, (int)y + quad.height, quad.x, quad.y, quad.x + quad.width, quad.y + quad.height, null);
	}

}
