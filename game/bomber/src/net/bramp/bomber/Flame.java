package net.bramp.bomber;

import net.bramp.bomber.screens.GameScreen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Flame extends MapObject implements SpriteInterface, AnimationInterface {

	final GameScreen game;

	/**
	 * Coordinates of the center of the flame
	 */
	int min_x, min_y, max_x, max_y;

	final int flame_length;
	
	boolean isOnFire = true;

	final AnimationComponent animation;

	/**
	 * Creates a new flame (starting at map_x,map_y)
	 * @param game
	 * @param map_x
	 * @param map_y
	 */
	public Flame(GameScreen game, int map_x, int map_y, int flame_length) {
		super(game.map);

		this.game = game;
		this.flame_length = flame_length;

		this.animation = new AnimationComponent(0.5f);

		// Setup textures
		TextureRegion frames[] = game.getTextureRepository().getFlame();
		animation.setFrames(this, frames);

		tile_margin_x = (map.getTileWidth()  - getWidth()) / 2;
		tile_margin_y = (map.getTileHeight() - getHeight()) / 2;

		setMapPosition(map_x, map_y);
	}

	/**
	 * Calculates the farest left/right/up/down the flame goes
	 */
	protected void calculateBounds() {

		// Left
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x - i, map_y);
			if (t != Map.BLANK) {
				min_x = map_x - i;
				break;
			}
		}

		// Right
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x + i, map_y);
			if (t != Map.BLANK) {
				max_x = map_x + i;
				break;
			}
		}

		// Up
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x, map_y + 1);
			if (t != Map.BLANK) {
				max_y = map_y + 1;
				break;
			}
		}

		// Down
		for (int i = 0; i < flame_length; i++) {
			int t = map.getTile(map_x, map_y - 1);
			if (t != Map.BLANK) {
				min_y = map_y - 1;
				break;
			}
		}
	}

	public void update (final float dt) {
		animation.update(this, dt);

		// Ensure all squares are on fire (no need if we keep track elsewhere)
		map.setOnFire(map_x, map_y, min_x, max_x, min_y, max_y, isOnFire);
	}

	@Override
	public void animationEnded() {
		isOnFire = false;

		map.setOnFire(map_x, map_y, min_x, max_x, min_y, max_y, isOnFire);
	}

	@Override
	public void animationFrameEnded(int frame) {}
	
	public void draw(SpriteBatch batch) {
		return; // Do nothing, drawing is handled by the map itself

		/*
		float screen_x, screen_y;
		
		final float tile_width = map.getTileWidth();
		final float tile_height = map.getTileHeight();

		screen_x = map.getScreenX(min_x) + offset_x;
		screen_y = map.getScreenY(map_y) + offset_y;
		for (int x = min_x; x < max_x; x++) {
			batch.draw(this, screen_x, screen_y, tile_width, tile_height);
			screen_x += map.getTileWidth();
		}

		screen_x = map.getScreenX(map_x) + offset_x;
		screen_y = map.getScreenY(map_y) + offset_y;
		for (int y = min_y; y < max_y; y++) {
			batch.draw(this, screen_x, screen_y, tile_width, tile_height);
			screen_y += map.getTileHeight();
		}

		
		//spriteBatch.draw(getTexture(), getVertices(), 0, SPRITE_SIZE);

		float[][] items = vertices.items;
		for (int i = 0; i < items.length; i++) {
			items[i].draw(batch);
		}
		*/
	}
}
