package br.studio.calbertofilho.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel implements Runnable {

	private static final int WIDTH = 1280, HEIGHT = 720;
	private final int FPS = 60;
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	private Graphics2D graphics;
	private int targetTime;
	private long startTime, urdTime, waitTime;

	public Panel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		setDoubleBuffered(true);
		running = false;
		targetTime = 1000 / FPS;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this, "GameThread");
			thread.start();
		}
	}

	@Override
	public void run() {
		init();
		while (running) {
			startTime = System.nanoTime();
	/////////////////////////
			update();
			render();
			draw();
	/////////////////////////
			urdTime = (System.nanoTime() - startTime) / 1000000;
			if (urdTime > targetTime)
				urdTime = targetTime;
			waitTime = targetTime - urdTime;
			try {
				Thread.sleep(waitTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void init() {
		running = true;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		graphics = (Graphics2D) image.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

////////////////////////////////////////////////////////////////////////////////
	public void update() {}

	public void render() {}

	public void draw() {
		Graphics graphs = getGraphics();
		graphs.drawImage(image, 0, 0, null);
		graphs.dispose();
	}
////////////////////////////////////////////////////////////////////////////////

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

}
