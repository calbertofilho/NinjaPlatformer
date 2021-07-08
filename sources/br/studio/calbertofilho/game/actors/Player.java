package br.studio.calbertofilho.game.actors;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import br.studio.calbertofilho.game.view.Panel;
import br.studio.calbertofilho.game.world.TileMap;

public class Player {

//	private String persona = "kirby";
	private String persona = "ninja";
	private double x, y, dX, dY;
	private int width, height, currentColumn, currentRow, leftTile, rightTile, topTile, bottomTile;
	private boolean left, right, jumping, descending, topLeft, topRight, bottomLeft, bottomRight, facingLeft;
	private double moveSpeed, maxSpeed, maxDescendingSpeed, stopSpeed, jumpStart, gravity, toX, toY, tmpX, tmpY;
	private TileMap tileMap;
	private Animation animation;
	private BufferedImage subImage;
	private BufferedImage[] idleSprites, jumpingSprites, descendingSprites, walkingSprites;

	public Player(TileMap tileMap) {
		this.tileMap = tileMap;
		moveSpeed = 0.6;
		maxSpeed = 4.2;
		maxDescendingSpeed = 12;
		stopSpeed = 0.3;
		jumpStart = -11.0;
		gravity = 0.64;
		try {
			if (persona == "kirby") {
				width = 22;
				height = 22;
				idleSprites = new BufferedImage[1];
				jumpingSprites = new BufferedImage[1];
				descendingSprites = new BufferedImage[1];
				idleSprites[0] = ImageIO.read(new File("resources\\assets\\images\\player\\kirby\\idle.gif"));
				jumpingSprites[0] = ImageIO.read(new File("resources\\assets\\images\\player\\kirby\\jump.gif"));
				descendingSprites[0] = ImageIO.read(new File("resources\\assets\\images\\player\\kirby\\fall.gif"));
				subImage = ImageIO.read(new File("resources\\assets\\images\\player\\kirby\\walk.gif"));
				walkingSprites = new BufferedImage[(int) (subImage.getWidth() / width)];
				for (int i = 0; i < walkingSprites.length; i++) {
					walkingSprites[i] = subImage.getSubimage(i * width + i, 0, width, height);
				}
			} else if (persona == "ninja") {
				width = 30;
				height = 30;
				jumpingSprites = new BufferedImage[1];
				descendingSprites = new BufferedImage[1];
				jumpingSprites[0] = ImageIO.read(new File("resources\\assets\\images\\player\\ninja\\jump.png"));
				descendingSprites[0] = ImageIO.read(new File("resources\\assets\\images\\player\\ninja\\fall.png"));
				subImage = ImageIO.read(new File("resources\\assets\\images\\player\\ninja\\idle.png"));
				idleSprites = new BufferedImage[(int) (subImage.getWidth() / width)];
				for (int i = 0; i < idleSprites.length; i++) {
					idleSprites[i] = subImage.getSubimage(i * width + i, 0, width, height);
				}
				subImage = ImageIO.read(new File("resources\\assets\\images\\player\\ninja\\run.png"));
				walkingSprites = new BufferedImage[(int) (subImage.getWidth() / width)];
				for (int i = 0; i < walkingSprites.length; i++) {
					walkingSprites[i] = subImage.getSubimage(i * width + i, 0, width, height);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		animation = new Animation();
		facingLeft = false;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setJumping(boolean jumping) {
		if (!descending)
			this.jumping = jumping;
	}

	private void calculateCorners(double x, double y) {
		leftTile = tileMap.getColumnTile((int) (x - width / 2));
		rightTile = tileMap.getColumnTile((int) (x + width / 2) - 1);
		topTile = tileMap.getRowTile((int) (y - height / 2));
		bottomTile = tileMap.getRowTile((int) (y + height / 2) - 1);
		topLeft = tileMap.isBlocked(topTile, leftTile);
		topRight = tileMap.isBlocked(topTile, rightTile);
		bottomLeft = tileMap.isBlocked(bottomTile, leftTile);
		bottomRight = tileMap.isBlocked(bottomTile, rightTile);
	}

////////////////////////////////////////////////////////////////////////////////
	public void update() {
		// determine the next position in screen
		if (left) {
			dX -= moveSpeed;
			if (dX < -maxSpeed)
				dX = -maxSpeed;
		} else if (right) {
			dX += moveSpeed;
			if (dX > maxSpeed)
				dX = maxSpeed;
		} else {
			if (dX > 0) {
				dX -= stopSpeed;
				if (dX < 0)
					dX = 0;
			} else if (dX < 0) {
				dX += stopSpeed;
				if (dX > 0)
					dX = 0;
			}
		}
		if (jumping) {
			dY = jumpStart;
			descending = true;
			jumping = false;
		}
		if (descending) {
			dY += gravity;
			if (dY > maxDescendingSpeed)
				dY = maxDescendingSpeed;
		} else {
			dY = 0;
		}
		// check collisions
		currentColumn = tileMap.getColumnTile((int) x);
		currentRow = tileMap.getRowTile((int) y);
		toX = x + dX;
		toY = y + dY;
		tmpX = x;
		tmpY = y;
		calculateCorners(x, toY);
		if (dY < 0) {
			if (topLeft || topRight) {
				dY = 0;
				tmpY = currentRow * tileMap.getTileSize() + height / 2;
			} else
				tmpY += dY;
		}
		if (dY > 0) {
			if (bottomLeft || bottomRight) {
				dY = 0;
				descending = false;
				tmpY = (currentRow + 1) * tileMap.getTileSize() - height / 2;
			} else
				tmpY += dY;
		}
		calculateCorners(toX, y);
		if (dX < 0) {
			if (topLeft || bottomLeft) {
				dX = 0;
				tmpX = currentColumn * tileMap.getTileSize() + width / 2;
			} else
				tmpX += dX;
		}
		if (dX > 0) {
			if (topRight || bottomRight) {
				dX = 0;
				tmpX = (currentColumn + 1) * tileMap.getTileSize() - width / 2;
			} else
				tmpX += dX;
		}
		if (!descending) {
			calculateCorners(x, y + 1);
			if (!bottomLeft && !bottomRight)
				descending = true;
		}
		x = tmpX;
		y = tmpY;
		// moving camera
		tileMap.setX((int) (Panel.WIDTH / 2 - x));
		tileMap.setY((int) (Panel.HEIGHT / 2 - y));
		// sprite animation
		if (left || right) {
			animation.setFrames(walkingSprites);
			animation.setDelay(100);
		} else {
			animation.setFrames(idleSprites);
			animation.setDelay(-1);
		}
		if (dY < 0) {
			animation.setFrames(jumpingSprites);
			animation.setDelay(-1);
		}
		if (dY > 0) {
			animation.setFrames(descendingSprites);
			animation.setDelay(-1);
		}
		animation.update();
		if (dX < 0)
			facingLeft = true;
		if (dX > 0)
			facingLeft = false;
	}

	public void render(Graphics2D graphics) {
		if (persona == "kirby")
			if (facingLeft)
				graphics.drawImage(
						animation.getImage(),
						(int) (tileMap.getX() + x - width / 2),
						(int) (tileMap.getY() + y - height / 2),
						null
					);
			else
				graphics.drawImage(
						animation.getImage(),
						(int) (tileMap.getX() + x - width / 2 + width),
						(int) (tileMap.getY() + y - height / 2),
						-width,
						height,
						null
					);
		else if (persona == "ninja")
			if (facingLeft)
				graphics.drawImage(
						animation.getImage(),
						(int) (tileMap.getX() + x - width / 2 + width),
						(int) (tileMap.getY() + y - height / 2),
						-width,
						height,
						null
					);
			else
				graphics.drawImage(
						animation.getImage(),
						(int) (tileMap.getX() + x - width / 2),
						(int) (tileMap.getY() + y - height / 2),
						null
					);
	}
////////////////////////////////////////////////////////////////////////////////

}
