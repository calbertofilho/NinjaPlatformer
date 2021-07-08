package br.studio.calbertofilho.game.world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.FileReader;

public class TileMap {

	private int x, y, tileSize, mapWidth, mapHeight, tile;
	private int[][] map;
	private BufferedReader reader;
	private String line, delimiters;
	private String[] tokens;

	public TileMap(String path, int tileSize) {
		this.tileSize = tileSize;
		try {
			reader = new BufferedReader(new FileReader(path));
			mapWidth = Integer.parseInt(reader.readLine());
			mapHeight = Integer.parseInt(reader.readLine());
			map = new int[mapHeight][mapWidth];
			delimiters = " ";
			for (int row = 0; row < mapHeight; row++) {
				line = reader.readLine();
				tokens = line.split(delimiters);
				for (int column = 0; column < mapWidth; column++)
					map[row][column] = Integer.parseInt(tokens[column]);
			}
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getColumnTile(int x) {
		return x / tileSize;
	}

	public int getRowTile(int y) {
		return y / tileSize;
	}

	public int getTile(int row, int column) {
		return map[row][column];
	}

	public int getTileSize() {
		return tileSize;
	}

////////////////////////////////////////////////////////////////////////////////
	public void update() {}

	public void render(Graphics2D graphics) {
		for (int row = 0; row < mapHeight; row++)
			for (int column = 0; column < mapWidth; column++) {
				tile = map[row][column];
				if (tile == 0)
					graphics.setColor(Color.BLACK);
				if (tile == 1)
					graphics.setColor(Color.WHITE);
				graphics.fillRect(x + column * tileSize, y + row * tileSize, tileSize, tileSize);
			}
	}
////////////////////////////////////////////////////////////////////////////////

}
