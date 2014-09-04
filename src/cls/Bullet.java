package cls;

import lib.Graphics;

public class Bullet {
	
	public static final int SPEED = Map.TILE_SIZE * 16;
	
	private double realX, realY;
	private int heading;
	private boolean finished;

	public Bullet(int x, int y, int direction) {
		this.realX = (x + 0.5 + Snake.xOffset(direction) * 1.5) * Map.TILE_SIZE;
		this.realY = (y + 0.5 + Snake.yOffset(direction) * 1.5) * Map.TILE_SIZE;
		heading = direction;
		finished = false;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void update(Map map, Snake[] snakes, double dt) {
		realX += Snake.xOffset(heading) * SPEED * dt;
		realY += Snake.yOffset(heading) * SPEED * dt;
		if (!map.passable(tileX(), tileY())) {
			finished = true;
			return;
		}
		for (Snake s : snakes) {
			if (s.hit(tileX(), tileY())) {
				finished = true;
				return;
			}
		}
	}
	
	private int tileX() {
		return (int)(realX / Map.TILE_SIZE);
	}
	
	private int tileY() {
		return (int)(realY / Map.TILE_SIZE);
	}
	
	public void draw() {
		Graphics.setColour(255, 255, 255);
		Graphics.circle(true, realX, realY, 2);
	}

}
