package net.bramp.bomber.components;

import net.bramp.bomber.AnimationInterface;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.base.Preconditions;

public class AnimationComponent {

	/**
	 * Time between image frames (in seconds)
	 */
	private float frame_interval;

	protected TextureRegion[] frames;

	/**
	 * Which animation frame I'm on
	 */
	protected int animation_frame = 0;

	/**
	 * How far into the animation frame we are (in seconds)
	 */
	protected float animation_time = 0.0f;

	final Sprite sprite;
	private AnimationInterface callback = null;
	
	/**
	 * 
	 * @param frame_interval Time between image frames (in seconds)
	 */
	public AnimationComponent(final Sprite sprite, float frame_interval) {
		this.sprite = sprite;
		this.frame_interval = frame_interval;
	}
	
	public void setListener(AnimationInterface callback) {
		this.callback = callback;
	}

	/**
	 * Sets the frames to display
	 * resets animation_time and animation_frame
	 * @param frames
	 */
	public void setFrames(TextureRegion[] frames) {
		Preconditions.checkNotNull(frames);
		Preconditions.checkArgument(frames.length > 0);

		this.frames = frames;
		this.animation_frame = 0;
		this.animation_time = 0.0f;

		TextureRegion first = frames[0];
		final float width  = first.getRegionWidth();
		final float height = first.getRegionHeight();

		sprite.setSize(width, height);
		sprite.setRegion(first);
	}

	public void update(final float dt) {
		animation_time += dt;
		if (animation_time > frame_interval) {
			animation_time -= frame_interval;

			if (callback != null)
				callback.animationFrameEnded(animation_frame);

			animation_frame++;
			if (animation_frame >= frames.length) {
				// Loop the animation
				animation_frame = 0;

				if (callback != null)
					callback.animationEnded();
			}

			sprite.setRegion( frames[animation_frame] );
		}
	}
}
