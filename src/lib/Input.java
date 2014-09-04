package lib;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public abstract class Input {
	
	public interface InputEventListener {
		
		public abstract void mousePressed(int mouseX, int mouseY, int mouseKey);
		public abstract void mouseReleased(int mouseX, int mouseY, int mouseKey);
		public abstract void mouseClicked(int mouseX, int mouseY, int mouseKey);
		
		public abstract void keyPressed(int key);
		public abstract void keyReleased(int key);
		public abstract void keyTyped(int key);
		
	}
	
	private static class ListenerKeyboard implements KeyListener {
		
		@Override
		public void keyTyped(KeyEvent e) {
			System.out.println("[Input] " + e.getKeyChar() + " typed.");
			for (InputEventListener listener : listeners) {
				listener.keyTyped(e.getKeyCode());
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			keysDown[e.getKeyCode()] = true;
			for (InputEventListener listener : listeners) {
				listener.keyPressed(e.getKeyCode());
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			keysDown[e.getKeyCode()] = false;
			for (InputEventListener listener : listeners) {
				listener.keyReleased(e.getKeyCode());
			}
		}

	}
	
	private static class ListenerMouse implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("[Input] Mouse button " + e.getButton() + " clicked (" + e.getX() + ", " + e.getY() + ").");
			for (InputEventListener listener : listeners) {
				listener.mouseClicked(e.getX(), e.getY(), e.getButton());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseDown[e.getButton()] = true;
			for (InputEventListener listener : listeners) {
				listener.mousePressed(e.getX(), e.getY(), e.getButton());
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseDown[e.getButton()] = false;
			for (InputEventListener listener : listeners) {
				listener.mouseReleased(e.getX(), e.getY(), e.getButton());
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			mouseOver = true;
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mouseOver = false;
		}

	}
	
	private static boolean[] keysDown;
	private static boolean[] mouseDown;
	private static boolean mouseOver;
	private static ListenerKeyboard keyListener;
	private static ListenerMouse mouseListener;
	private static ArrayList<InputEventListener> listeners;
	
	public static void initialise() {
		keysDown = new boolean[256];
		mouseDown = new boolean[8];
		keyListener = new ListenerKeyboard();
		mouseListener = new ListenerMouse();
		Window.setKeyboardListener(keyListener);
		Window.setMouseListener(mouseListener);
		listeners = new ArrayList<InputEventListener>();
	}
	
	public static void addListener(InputEventListener listener) {
		listeners.add(listener);
	}

	public static boolean isKeyDown(int key) {
		return keysDown[key];
	}
	
	public static boolean isMouseDown(int key) {
		return mouseDown[key];
	}
	
	public static boolean isMouseOver() {
		return mouseOver;
	}

}
