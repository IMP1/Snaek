package lib;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public abstract class Window {
	
	private static JFrame window;
	protected static Canvas canvas;

	public static void initialise(int width, int height, String title) {
		Dimension size = new Dimension(width, height);
		
		canvas = new Canvas();
		canvas.setPreferredSize(size);
		
		window = new JFrame();
		window.setResizable(false);
		window.add(canvas);
		window.pack();
		window.setTitle(title);
		window.setLocationRelativeTo(null);
		
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		window.setVisible(true);
		
		canvas.createBufferStrategy(2);
	}
	
	protected static void setKeyboardListener(KeyListener listener) {
		window.addKeyListener(listener);
		canvas.addKeyListener(listener);
	}
	
	protected static void setMouseListener(MouseListener listener) {
		window.addMouseListener(listener);
		canvas.addMouseListener(listener);
	}

}
