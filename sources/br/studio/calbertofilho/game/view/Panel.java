package br.studio.calbertofilho.game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import br.studio.calbertofilho.game.actors.Player;
import br.studio.calbertofilho.game.world.TileMap;

/**
 * Classe responsável pela tela de exibição e o controle de fluxo de execução do jogo
 * 
 * @since 07/07/2021
 * @version 1.0
 * @author Carlos Alberto Morais Moura Filho<br>
 * Faça sua doação: <a href="https://nubank.com.br/pagar/5wv6g/ZdcePGcCDT">Pix</a>
 */@SuppressWarnings("serial")
public class Panel extends JPanel implements Runnable, KeyListener {

	public static final int WIDTH = 500, HEIGHT = 400;
	private final int FPS = 60;
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	private Graphics graphs;
	private Graphics2D graphics;
	private int targetTime;
	private long startTime, urdTime, waitTime;
	private TileMap tileMap;
	private Player player;

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
		addKeyListener(this);
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
		tileMap = new TileMap("resources\\assets\\stages\\level1.map", 32);
		tileMap.loadTileSet("resources\\assets\\images\\tiles\\tileset.gif");
		player = new Player(tileMap);
		player.setX(50);
		player.setY(50);
	}

	public double getFPS(double oldTime) {
		double newTime = System.nanoTime();
		double delta = -oldTime;
		double fps = 1 / (delta * 1000);
		oldTime = newTime;
		return fps;
		// usage: getFPS(System.nanoTime());
	}

////////////////////////////////////////////////////////////////////////////////
	public void update() {
		tileMap.update();
		player.update();
	}

	public void render() {
		graphics.setColor(new Color(135, 206, 250)); //light sky
//		graphics.setColor(new Color(79, 155, 217));  //dark sky
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		tileMap.render(graphics);
		player.render(graphics);
	}

	public void draw() {
		graphs = getGraphics();
		graphs.drawImage(image, 0, 0, null);
		graphs.dispose();
	}
////////////////////////////////////////////////////////////////////////////////

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if ((key == KeyEvent.VK_LEFT) || (key == KeyEvent.VK_A))
			player.setLeft(true);
		if ((key == KeyEvent.VK_RIGHT) || (key == KeyEvent.VK_D))
			player.setRight(true);
		if ((key == KeyEvent.VK_UP) || (key == KeyEvent.VK_W))
			player.setJumping(true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if ((key == KeyEvent.VK_LEFT) || (key == KeyEvent.VK_A))
			player.setLeft(false);
		if ((key == KeyEvent.VK_RIGHT) || (key == KeyEvent.VK_D))
			player.setRight(false);
		if ((key == KeyEvent.VK_UP) || (key == KeyEvent.VK_W))
			player.setJumping(false);
	}

}
