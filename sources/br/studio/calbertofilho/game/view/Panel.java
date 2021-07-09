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
	private Thread thread;
	private boolean running;
	private BufferedImage image;
	private Graphics graphs;
	private Graphics2D graphics;
	private TileMap tileMap;
	private Player player;
	private final int TARGET_FPS = 60;
	private static double averageFPS;
	private long startTime, URDTimeMillis, targetTime, waitTime, totalTime;
	private int frameCount, maxFrameCount;

	public Panel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
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
			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;
			waitTime = (targetTime - URDTimeMillis) > 0 ? (targetTime - URDTimeMillis) : 0;
			try {
				Thread.sleep(waitTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if (frameCount == maxFrameCount) {
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}
		}
	}

	private void init() {
		running = true;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		graphics = (Graphics2D) image.getGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		totalTime = 0;
		frameCount = 0;
		maxFrameCount = TARGET_FPS;
		targetTime = 1000 / TARGET_FPS;
		tileMap = new TileMap("resources\\assets\\stages\\level1.map", 32);
		tileMap.loadTileSet("resources\\assets\\images\\tiles\\tileset.gif");
		player = new Player(tileMap);
		player.setX(50);
		player.setY(50);
	}

////////////////////////////////////////////////////////////////////////////////
	private void update() {
		tileMap.update();
		player.update();
	}

	private void render() {
		graphics.setColor(new Color(79, 155, 217));  //dark sky: new Color(79, 155, 217)       light sky: new Color(135, 206, 250)
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		tileMap.render(graphics);
		graphics.setColor(Color.WHITE);
		player.render(graphics);
		//show fps counter
		String text = String.format("FPS: %.2f", getFPS());
		graphics.drawString(text, (WIDTH - graphics.getFontMetrics().stringWidth(text)) - 5, graphics.getFontMetrics().getHeight());
	}

	private void draw() {
		graphs = this.getGraphics();
		graphs.drawImage(image, 0, 0, null);
		graphs.dispose();
	}
////////////////////////////////////////////////////////////////////////////////

	public static double getFPS() {
		return averageFPS;
	}

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
