package net.bramp.bomber;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public interface SpriteInterface extends Disposable {
	public void update (final float dt);
	public void draw(SpriteBatch batch);
}
