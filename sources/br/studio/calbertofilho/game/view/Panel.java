package br.studio.calbertofilho.game.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import br.studio.calbertofilho.game.world.TileMap;

/**
 * Classe responsável pela tela de exibição e o controle de fluxo de execução do jogo
 * 
 * @since 07/07/2021
 * @version 1.0
 * @author Carlos Alberto Morais Moura Filho<br>
 * Faça sua doação: <a href="https://nubank.com.br/pagar/5wv6g/ZdcePGcCDT">Pix</a>
 */@SuppressWarnings("serial")
public class Panel extends JPanel implements Runnable {

	private final int WIDTH = 1280, HEIGHT = 720;
	private final int FPS = 60;
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	private Graphics graphs;
	private Graphics2D graphics;
	private int targetTime;
	private long startTime, urdTime, waitTime;
	private TileMap tileMap;

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
		tileMap = new TileMap("resources\\assets\\stages\\test.map", 32);
	}

////////////////////////////////////////////////////////////////////////////////
	public void update() {
		tileMap.update();
	}

	public void render() {
		tileMap.render(graphics);
	}

	public void draw() {
		graphs = getGraphics();
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

	public double getFPS(double oldTime) {
		double newTime = System.nanoTime();
		double delta = -oldTime;
		double fps = 1 / (delta * 1000);
		oldTime = newTime;
		return fps;
		// usage: getFPS(System.nanoTime());
	}

}
