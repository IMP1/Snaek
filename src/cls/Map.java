package cls;

import java.util.ArrayList;

import lib.Graphics;

public class Map {
	
	public static final int TILE_SIZE = 32;
	public static final int MAX_PELLETS = 3;
	public static final int PELLET_TIMER = 3;
	
	public class Pellet {
		public final int x, y;
		public Pellet(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private int width, height;
	private ArrayList<Pellet> pellets;
	private ArrayList<Bullet> bullets;
	private double timer;
	
	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		pellets = new ArrayList<Pellet>();
		bullets = new ArrayList<Bullet>();
		timer = 0;
	}
	
	public void update(double dt, Snake[] snakes) {
		timer += dt;
		if (timer > PELLET_TIMER) {
			generatePellet();
			timer -= PELLET_TIMER;
		}
		for (int i = bullets.size()-1; i >= 0; i --) {
			bullets.get(i).update(this, snakes, dt);
			if (bullets.get(i).isFinished()) {
				bullets.remove(i);
			}
		}
	}
	
	public void addBullet(int x, int y, int r) {
		bullets.add(new Bullet(x, y, r));
	}
	
	private void generatePellet() {
		int x;
		int y;
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		} while (pelletAt(x, y) || !passable(x, y));
		pellets.add(new Pellet(x, y));
	}
	
	public boolean pelletAt(int x, int y) {
		for (Pellet p : pellets) {
			if (p.x == x && p.y == y) return true;
		}
		return false;
	}
	
	public void eatPellet(int x, int y) {
		int target = -1;
		for (int i = 0; i < pellets.size(); i ++) {
			if (pellets.get(i).x == x && pellets.get(i).y == y) {
				target = i;
			}
		}
		if (target > -1) {
			pellets.remove(target);
		}
	}
	
	public boolean passable(int x, int y) {
		return (x >= 0 && y >= 0 && x < width && y < height);
	}

	public void draw() {
		for (int j = 0; j < height; j ++) {
			for (int i = 0; i < width; i ++) {
				Graphics.setColour(128, 128, 128);
				Graphics.rectangle(true, i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
				Graphics.setColour(64, 64, 64);
				Graphics.rectangle(false, i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			}
		}
		for (Pellet p : pellets) {
			Graphics.setColour(128, 255, 128);
			Graphics.circle(true, (p.x + 0.5) * TILE_SIZE, (p.y + 0.5) * TILE_SIZE, 4);
		}
		for (Bullet b : bullets) {
			b.draw();
		}
	}
	
}
