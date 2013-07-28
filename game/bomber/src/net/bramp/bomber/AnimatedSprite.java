package net.bramp.bomber;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.base.Preconditions;

public abstract class AnimatedSprite extends Sprite {

	/**
	 * Time between image frames (in seconds)
	 */
	private float frame_interval = 0.01f;

	private TextureRegion[] frames;

	/**
	 * Which animation frame I'm on
	 */
	int animation_frame = 0;

	/**
	 * How far into the animation frame we are (in seconds)
	 */
	float animation_time = 0.0f;

	public AnimatedSprite() {}

	/**
	 * Called when each single frame finishes
	 * @param frame
	 */
	protected void animationFrameEnded(int frame) {}
	
	/**
	 * Called when the sequence of frames ends
	 */
	protected void animationEnded() {}

	/**
	 * Sets the frames to display
	 * resets animation_time and animation_frame
	 * @param frames
	 */
	protected void setFrames(TextureRegion[] frames) {
		Preconditions.checkNotNull(frames);
		Preconditions.checkArgument(frames.length > 0);

		this.frames = frames;
		this.animation_frame = 0;
		this.animation_time = 0.0f;
		setRegion(frames[0]);
	}

	protected void updateAnimationFrame(final float dt) {
		animation_time += dt;
		if (animation_time > frame_interval) {
			animation_time -= frame_interval;

			animationFrameEnded(animation_frame);

			animation_frame++;
			if (animation_frame >= frames.length) {
				animation_frame = 0; // Loop
				animationEnded();
			}

			setRegion( frames[animation_frame] );
		}
	}

	public void update (final float dt) {
		updateAnimationFrame(dt);
	}

}
