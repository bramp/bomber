package net.bramp.bomber;

import net.bramp.bomber.screens.GameScreen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Bomb extends AnimatedSprite implements Mappable, SpriteInterface {

	final GameScreen game;
	final Map map;

	/**
	 * Coordinates on the map I am
	 */
	int map_x, map_y;

	final Player owner;
	final int flame_length;

	public Bomb(GameScreen game, Player owner, int map_x, int map_y) {
		super(1.0f);

		this.game = game;
		this.map  = game.getMap();
		this.owner = owner;

		this.flame_length = owner.getFlameLength();

		// Setup textures
		TextureRegion frames[] = game.getTextureRepository().getBomb();

		// Setup sprite size / original image
		TextureRegion first = frames[0];
		final float width  = first.getRegionWidth();
		final float height = first.getRegionHeight();
		setSize(width, height);
		setFrames(frames);

		setMapPosition(map_x, map_y);
	}

	public void setMapPosition(int map_x, int map_y) {
		this.map_x = map_x;
		this.map_y = map_y;

		float offset_x = (map.getTileWidth() - getWidth()) / 2;
		float offset_y = (map.getTileHeight() - getHeight()) / 2;

		setPosition(
			map.getScreenX(map_x) + offset_x,
			map.getScreenY(map_y) + offset_y
		);
	}

	public void update (final float dt) {
		super.update(dt);
	}
	
	@Override
	protected void animationEnded() {
		// We need to go boom
		game.bombExploded(this);
	}

	@Override
	public int getMapX() {
		return map_x;
	}

	@Override
	public int getMapY() {
		return map_y;
	}

	public Player getOwner() {
		return owner;
	}
}
