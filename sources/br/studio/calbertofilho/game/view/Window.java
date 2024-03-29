package br.studio.calbertofilho.game.view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Classe respons�vel pela cria��o e manipula��o da janela do jogo
 * 
 * @since 07/07/2021
 * @version 1.0
 * @author Carlos Alberto Morais Moura Filho<br>
 * Fa�a sua doa��o: <a href="https://nubank.com.br/pagar/5wv6g/ZdcePGcCDT">Pix</a>
 */@SuppressWarnings("serial")
public class Window extends JFrame {

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
		game = new Panel();
		setContentPane(game);
	}

}
