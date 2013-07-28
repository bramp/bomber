package net.bramp.bomber;

import net.bramp.bomber.screens.GameScreen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Bomb extends AnimatedSprite implements Mappable {

	final GameScreen game;
	final Map map;

	/**
	 * Coordinates on the map I am
	 */
	int map_x = 0, map_y = 0;

	int flame_length = 1;

	final Player owner;

	public Bomb(GameScreen game, Player owner, int map_x, int map_y) {

		this.game = game;
		this.map  = game.getMap();
		this.owner = owner;

		// Setup textures
		TextureRegion frames[] = game.getTextureRepository().getBomb();

		// Setup sprite size / original image
		TextureRegion first = frames[0];
		final float width  = first.getRegionWidth();
		final float height = first.getRegionHeight();
		setSize(width, height);
		setFrames(frames);
	}


	public void update (final float dt) {
		super.update(dt);
	}
	
	@Override
	protected void animationEnded() {
		// We need to go boom
		System.out.println("Boom");
	}


	@Override
	public int getMapX() {
		return map_x;
	}


	@Override
	public int getMapY() {
		return map_y;
	}
}
