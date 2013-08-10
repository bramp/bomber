package net.bramp.bomber;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface AnimationInterface {

	/**
	 * Called when each single frame finishes
	 * @param frame
	 */
	void animationFrameEnded(int frame);

	/**
	 * Called when the sequence of frames ends
	 */
	void animationEnded();

	/**
	 * Sets the current frame
	 */
	void setRegion(TextureRegion region);

}
