package run;

import java.awt.event.KeyEvent;

import lib.*;
import cls.Map;
import cls.Snake;

public class Game implements Input.InputEventListener {
	
	public static final double ROOT_2 = Math.sqrt(2);
	
	public static final int WIDTH = 960;
	public static final int HEIGHT = WIDTH / 16 * 9;
	public static final double MAX_DT = 1.0 / 15;

	private Map map;
	private Snake[] snakes;
	private boolean gameOver;
	private boolean paused;

	/**
	 * Create the simple game - this also starts the game loop
	 */
	public Game() {
		Filesystem.addLocation("src/gfx", false);
		
		Window.initialise(WIDTH, HEIGHT, "Title");
		Input.initialise();
		Input.addListener(this);
		Graphics.initialise();
		setup();
		gameLoop();
	}
	
	public void setup() {
		map = new Map(24, 16);
		snakes = new Snake[] {
			new Snake(4, 4, 0),
			new Snake(10, 4, 2),
		};
		gameOver = false;
		paused = false;
	}
	
	/**
	 * The game loop handles the basic rendering and tracking of time. Each
	 * loop it calls off to the game logic to perform the movement and 
	 * collision checking.
	 */
	public void gameLoop() {
		long lastTick = System.nanoTime();
		while (true) {
			try { Thread.sleep(4); } catch (Exception e) {}; // pause a bit so that we don't choke the system
			double deltaTime = (double)(System.nanoTime() - lastTick) / 1_000_000_000.0;
			lastTick = System.nanoTime();
			// Update multiple times rather than with a dangerously large delta-time
			while (deltaTime > MAX_DT) {
				System.out.println("[Game] dt > " + MAX_DT + " (" + deltaTime + ").");
				update(MAX_DT);
				deltaTime -= MAX_DT;
			}
			if (deltaTime > 0)
				update(deltaTime);
			
			Graphics.clear();
			draw();
		}
	}
	
	public void update(double dt) {
		if (paused) return;
		if (gameOver) return;
		map.update(dt, snakes);
		for (Snake s : snakes) { s.update(dt, map, snakes); }
		for (Snake s : snakes) { if (s.hasCrashed()) gameOver = true; }
	}
	
	public void draw() {
		map.draw();
		for (Snake s : snakes) { s.draw(); }
		if (paused) {
			drawDebug();
		}
	}

	private void drawDebug() {
		Graphics.setColour(0, 0, 0, 128);
		Graphics.rectangle(true, 0, 0, WIDTH, HEIGHT);
		Graphics.setColour(255, 255, 255);
		Graphics.printCentred("Paused", 0, 0, WIDTH);
	}
	
	public static void main(String[] args) {
		new Game();
	}

	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseKey) {
		
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseKey) {
		
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseKey) {
		
	}

	@Override
	public void keyPressed(int key) {
		if (gameOver) {
			setup();
			return;
		}
		if (key == KeyEvent.VK_P) {
			paused = !paused;
		}
		// key == KeyEvent.VK_UP || key == KeyEvent.VK_NUMPAD8 || 
		// key == KeyEvent.VK_DOWN || key == KeyEvent.VK_NUMPAD2 || 
		// key == KeyEvent.VK_LEFT || key == KeyEvent.VK_NUMPAD4 || 
		// key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_NUMPAD6 || 
		if (key == KeyEvent.VK_W) {
			snakes[0].turnTo(3);
		} else if (key == KeyEvent.VK_S) {
			snakes[0].turnTo(1);
		} else if (key == KeyEvent.VK_A) {
			snakes[0].turnTo(2);
		} else if (key == KeyEvent.VK_D) {
			snakes[0].turnTo(0);
		} else if (key == KeyEvent.VK_SPACE) {
			snakes[0].shoot(map);
		}
		if (key == KeyEvent.VK_UP) {
			snakes[1].turnTo(3);
		} else if (key == KeyEvent.VK_DOWN) {
			snakes[1].turnTo(1);
		} else if (key == KeyEvent.VK_LEFT) {
			snakes[1].turnTo(2);
		} else if (key == KeyEvent.VK_RIGHT) {
			snakes[1].turnTo(0);
		} else if (key == KeyEvent.VK_CONTROL) {
			snakes[1].shoot(map);
		}
	}

	@Override
	public void keyReleased(int key) {
		
	}

	@Override
	public void keyTyped(int key) {
		
	}

}
