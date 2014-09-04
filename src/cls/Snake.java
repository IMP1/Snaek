package cls;

import java.util.ArrayList;

import lib.Graphics;
import cls.Map.Pellet;

public class Snake {
	
	public static int xOffset(int direction) {
		switch (direction) {
			case 0: return 1;
			case 2: return -1;
			default: return 0;
		}
	}
	
	public static int yOffset(int direction) {
		switch (direction) {
			case 1: return 1;
			case 3: return -1;
			default: return 0;
		}
	}
	
	public static final double TICK_DELAY = 0.1;
	public static final int START_LENGTH = 4;
	
	private class Cell {
		
		private Cell(int x, int y, int r) {
			this.x = x;
			this.y = y;
			this.heading = r;
		}
		
		int heading;
		int x;
		int y;
		
	}

	private ArrayList<Cell> body;
	private double tickTimer;
	private int growth;
	private int newDirection;
	private boolean hasCrashed;
	private int pelletsEaten;
	private ArrayList<Pellet> pelletsEating;
	private int bullets;
	
	public Snake(int x, int y, int r) {
		body = new ArrayList<Cell>();
		body.add(new Cell(x, y, r));
		tickTimer = 0;
		growth = 0;
		hasCrashed = false;
		pelletsEaten = 0;
		pelletsEating = new ArrayList<Pellet>();
		bullets = 1;
		grow(START_LENGTH - 1);
	}
	
	private Cell getHead() {
		return body.get(0);
	}
	
	private Cell getTail() {
		return body.get(body.size()-1);
	}
	
	private int length() {
		return body.size();
	}
	
	public void grow(int n) {
		growth += n;
	}
	public void grow() { grow(1); }
	
	public boolean hasCrashed() {
		return hasCrashed;
	}
	
	public void update(double dt, Map map, Snake[] snakes) {
		if (hasCrashed) return;
		tickTimer += dt;
		while (tickTimer > TICK_DELAY) {
			tickTimer -= TICK_DELAY;
			tick(map, snakes);
		}
	}
	
	private void addTail() {
		body.add(new Cell(getTail().x, getTail().y, getTail().heading));
	}
	
	private void tick(Map map, Snake[] snakes) {
		if (newDirection != getHead().heading) {
			getHead().heading = newDirection;
		}
		int newX = getHead().x + Snake.xOffset(getHead().heading);
		int newY = getHead().y + Snake.yOffset(getHead().heading);
		if (isNotPassable(map, snakes, newX, newY)) {
			hasCrashed = true;
			return;
		}
		boolean hasJustGrown = false;
		if (growth > 0) {
			addTail();
			hasJustGrown = true;
			growth -= 1;
		}
		if (length() > 1) {
			for (int i = length() - (hasJustGrown ? 2 : 1); i > 0; i --) {
				body.get(i).x = body.get(i-1).x;
				body.get(i).y = body.get(i-1).y;
				body.get(i).heading = body.get(i-1).heading;
			}
		}
		getHead().x = newX;
		getHead().y = newY;
		if (map.pelletAt(newX, newY)) {
			eatPellet(map, newX, newY);
		}
		for (int i = pelletsEating.size()-1; i >= 0; i --) {
			if (cellAt(pelletsEating.get(i).x, pelletsEating.get(i).y) == -1) {
				pelletsEating.remove(i);
			}
		}
	}
	
	private void eatPellet(Map map, int x, int y) {
		map.eatPellet(x, y);
		pelletsEating.add(map.new Pellet(x, y));
		pelletsEaten += 1;
		if (pelletsEaten == 5 && bullets < length()) {
			pelletsEaten -= 5;
			bullets += 1;
		}
		grow();
	}
	
	public int cellAt(int x, int y) {
		for (int i = 0; i < body.size(); i ++) {
			if (body.get(i).x == x && body.get(i).y == y) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean hit(int x, int y) {
		int i = cellAt(x, y);
		if (i == -1) return false;
		if (i == 0) return true;
		int size = i + 1;
		while (body.size() > size) {
			body.remove(i + 1);
		}
		return true;
	}
	
	private boolean isNotPassable(Map map, Snake[] snakes, int x, int y) {
		if (!map.passable(x, y)) {
			return true;
		}
		for (Snake s : snakes) {
			if (s.cellAt(x, y) != -1) {
				return true;
			}
		}
		return false;
	}
	
	public void turnTo(int direction) {
		if (Math.abs(getHead().heading - direction) != 2) {
			newDirection = direction;
		}
	}
	
	public void shoot(Map map) {
		if (bullets == 0) return;
		map.addBullet(getHead().x, getHead().y, getHead().heading);
		bullets -= 1;
	}
	
	public void draw() {
		Graphics.setColour(192, 192, 255);
		for (int i = 0; i < body.size(); i ++) {
			Cell c = body.get(i);
			if (i >= bullets) Graphics.setColour(255, 255, 255);
			Graphics.rectangle(true, c.x * Map.TILE_SIZE, c.y * Map.TILE_SIZE, Map.TILE_SIZE, Map.TILE_SIZE);
		}
		for (Pellet p : pelletsEating) {
			Graphics.setColour(128, 255, 128, 128);
			Graphics.rectangle(true, p.x * Map.TILE_SIZE, p.y * Map.TILE_SIZE, Map.TILE_SIZE, Map.TILE_SIZE);
		}
		
		Graphics.setColour(255, 255, 255);
	}
	
	public void drawDebug() {
		Graphics.print("Head @ (" + getHead().x + ", " + getHead().y + ")", 0, 0);
	}

}
