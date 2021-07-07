package br.studio.calbertofilho.game.view;

import java.awt.image.BufferStrategy;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import br.studio.calbertofilho.game.Panel;

/**
 * Classe responsável pela criação e manipulação da janela do jogo
 * 
 * @since 01/07/2021
 * @version 1.0
 * @author Carlos Alberto Morais Moura Filho<br>
 * Faça sua doação: <a href="https://nubank.com.br/pagar/5wv6g/ZdcePGcCDT">Pix</a>
 */@SuppressWarnings("serial")
public class Window extends JFrame {

	private BufferStrategy buffer;
	private Panel game;

	public Window() {
		setTitle("NinjaPlatformer v1.0");
		setIconImage(new ImageIcon("").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(getContentPane().getPreferredSize());
		setMaximumSize(getContentPane().getPreferredSize());
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void addNotify() {
		super.addNotify();
		createBufferStrategy(4);
		buffer = getBufferStrategy();
		game = new Panel(buffer, 1280, 720);
		setContentPane(game);
	}

}
