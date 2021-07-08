package br.studio.calbertofilho.game.world;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;

import br.studio.calbertofilho.game.world.objects.Tile;

public class TileMap {

	private int x, y, tileSize, mapWidth, mapHeight, tile, tilePadding, numTilesAcross;
	private int[][] map;
	private BufferedReader reader;
	private String line, delimiters;
	private String[] tokens;
	private BufferedImage tileSet, subImage;
	private Tile[][] tiles;

	public TileMap(String path, int tileSize) {
		this.tileSize = tileSize;
		tilePadding = 1;
		try {
			reader = new BufferedReader(new FileReader(path));
			mapWidth = Integer.parseInt(reader.readLine());
			mapHeight = Integer.parseInt(reader.readLine());
			map = new int[mapHeight][mapWidth];
			delimiters = "\\s+"; // more than one space
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

	public void loadTileSet(String path) {
		try {
			this.tileSet = ImageIO.read(new File(path));
			numTilesAcross = (tileSet.getWidth() + tilePadding) / (tileSize + tilePadding);
			tiles = new Tile[2][numTilesAcross];
			for (int column = 0; column < numTilesAcross; column++) {
				subImage = tileSet.getSubimage(column * tileSize + column, 0, tileSize, tileSize);
				tiles[0][column] = new Tile(subImage, false); // walkable tiles
				subImage = tileSet.getSubimage(column * tileSize + column, tileSize + 1, tileSize, tileSize);
				tiles[1][column] = new Tile(subImage, true); // blocked tiles
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

	public boolean isBlocked(int row, int column) {
		tile = map[row][column];
		int r = tile / tiles[0].length;
		int c = tile % tiles[0].length;
		return tiles[r][c].isBlocked();
	}

////////////////////////////////////////////////////////////////////////////////
	public void update() {}

	public void render(Graphics2D graphics) {
		for (int row = 0; row < mapHeight; row++)
			for (int column = 0; column < mapWidth; column++) {
				tile = map[row][column];
				int r = tile / tiles[0].length;
				int c = tile % tiles[0].length;
				graphics.drawImage(
						tiles[r][c].getImage(),
						x + column * tileSize,
						y + row * tileSize,
						null
					);
			}
	}
////////////////////////////////////////////////////////////////////////////////

}
