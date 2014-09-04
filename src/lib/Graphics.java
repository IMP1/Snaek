package lib;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.Stack;

public abstract class Graphics {

	private static BufferStrategy strategy;
	private static Graphics2D graphics;
	private static int width;
	private static int height;
	private static Stack<AffineTransform> transformations;
	
	public static void initialise() {
		strategy = Window.canvas.getBufferStrategy();
		width = Window.canvas.getWidth();
		height = Window.canvas.getHeight();
		graphics = (Graphics2D)strategy.getDrawGraphics();
		transformations = new Stack<AffineTransform>();
	}
	
	public static void clear() {
		strategy.show();
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, width, height);
	}
	
	public static void draw(Image img, double x, double y) {
		img.draw(graphics, x, y);
	}

	public static void drawq(Image img, Rectangle quad, double x, double y) {
		img.drawq(graphics, quad, x, y);
	}
	
	public static void setColour(int r, int g, int b) {
		graphics.setColor(new Color(r, g, b));
	}
	
	public static void setColour(int r, int g, int b, int a) {
		graphics.setColor(new Color(r, g, b, a));
	}
	
	public static void rectangle(boolean fill, double x, double y, double width, double height) {
		if (fill) {
			graphics.fillRect((int)x, (int)y, (int)width, (int)height);
		} else {
			graphics.drawRect((int)x, (int)y, (int)width, (int)height);
		}
	}
	
	public static void roundedRectangle(boolean fill, double x, double y, double width, double height, double radius) {
		graphics.drawRoundRect((int)x, (int)y, (int)width, (int)height, (int)radius, (int)radius);
	}
	
	public static void arc(boolean fill, double x, double y, double radius, double startAngle, double endAngle) {
		// Angles negated so clockwise is positive.
		int angleBegin = -(int)Math.toDegrees(startAngle);
		int angleSize = -(int)Math.toDegrees(endAngle) - angleBegin;
		int width = (int)radius * 2;
		int height = width;
		int xPos = (int)x - width / 2;
		int yPos = (int)y - height / 2;
		if (fill) {
			graphics.fillArc(xPos, yPos, width, height, angleBegin, angleSize);
		} else {
			graphics.drawArc(xPos, yPos, width, height, angleBegin, angleSize);
		}
	}
	
	public static void circle(boolean fill, double x, double y, double radius) {
		arc(fill, x, y, radius, 0, Math.PI * 2);
	}
	
	public static void line(double... points) {
		int[] xPoints = new int[points.length / 2];
		int[] yPoints = new int[points.length / 2];
		for (int i = 0; i < points.length; i ++) {
			if (i % 2 == 0) {
				xPoints[i/2] = (int)points[i];
			} else {
				yPoints[i/2] = (int)points[i];
			}
		}
		graphics.drawPolyline(xPoints, yPoints, xPoints.length);
	}
	
	public static void polygon(boolean fill, double... points) {
		int[] xPoints = new int[points.length / 2];
		int[] yPoints = new int[points.length / 2];
		for (int i = 0; i < points.length; i ++) {
			if (i % 2 == 0) {
				xPoints[i/2] = (int)points[i];
			} else {
				yPoints[i/2] = (int)points[i];
			}
		}
		if (fill) {
			graphics.fillPolygon(xPoints, yPoints, xPoints.length);
		} else {
			graphics.drawPolygon(xPoints, yPoints, xPoints.length);
		}
	}
	
	public static void print(String text, double x, double y) {
		// Add font size so text origin is top-left, not bottom-left;
		graphics.drawString(text, (int)x, (int)y + graphics.getFont().getSize());
	}
	
	public static void printCentred(String text, double x, double y, double width) {
		double w = graphics.getFont().getStringBounds(text, graphics.getFontRenderContext()).getWidth();
		x += (width - w) / 2;
		print(text, x, y);
	}
	
	public static void translate(double x, double y) {
		graphics.translate(x, y);
	}
	
	public static void scale(double sx, double sy) {
		graphics.scale(sx, sy);
	}
	
	public static void rotate(double angle) {
		graphics.rotate(angle);
	}

	public static void push() {
		transformations.push(graphics.getTransform());
	}
	
	public static void pop() {
		graphics.setTransform(transformations.pop());
	}
	
}
