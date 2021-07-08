package br.studio.calbertofilho.game.actors;

import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage[] frames;
	private int currentFrame;
	private long startTime, elapsedTime, delay;

	public Animation() {}

	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		if (currentFrame >= frames.length)
			currentFrame = 0;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public BufferedImage getImage() {
		return frames[currentFrame];
	}

////////////////////////////////////////////////////////////////////////////////
	public void update() {
		if (delay == -1)
			return;
		elapsedTime = (System.nanoTime() - startTime) / 1000000;
		if (elapsedTime > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if (currentFrame == frames.length)
			currentFrame = 0;
	}
////////////////////////////////////////////////////////////////////////////////

}
