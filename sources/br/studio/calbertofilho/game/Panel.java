package br.studio.calbertofilho.game;

import java.awt.Dimension;
import java.awt.image.BufferStrategy;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel implements Runnable {

	private int width, height;
	private Thread thread;
	private boolean running;
	private BufferStrategy buffer;

	public Panel(BufferStrategy buffer, int width, int height) {
		this.width = width;
		this.height = height;
		this.buffer = buffer;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		setDoubleBuffered(true);
		running = false;
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
	public void run() {}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
