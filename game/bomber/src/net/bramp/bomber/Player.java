package net.bramp.bomber;

import net.bramp.bomber.screens.GameScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Player extends AnimatedSprite implements Mappable {
	private static final boolean DEBUG = Config.DEBUG;

	public static final int STOP  = -1;
	public static final int UP    = 0;
	public static final int DOWN  = 1;
	public static final int LEFT  = 2;
	public static final int RIGHT = 3;

	/**
	 * Distance moved in one frame (in pixels)
	 */
	private static final int WALKING_DISTANCE = 4;
	
	private static final int WALKING_FRAMES = 8;
	
	final GameScreen game;
	final Map map;

	/**
	 * Frames of the player, WALKING_FRAMES * 4 directions
	 */
	final TextureRegion[][] walking_frames = new TextureRegion[4][WALKING_FRAMES];
	
	/**
	 * Which direction am I facing
	 */
	int walking_direction = DOWN;
	
	/**
	 * Am I walking right now?
	 */
	boolean walking = false;

	/**
	 * Coordinates on the map I am
	 */
	int map_x = 0, map_y = 0;

	int flame_length = 1;
	int allowed_bombs = 1;
	int deployed_bombs = 0;

	final float offset_x, offset_y;

	public Player(GameScreen game, int[] map_coord) {

		this.game = game;
		this.map  = game.getMap();

		// Setup textures
		final TextureAtlas atlas = game.getTextureAtlas();

		for (int i = 0; i < WALKING_FRAMES; i++) {
			walking_frames[UP][i]    = atlas.findRegion("Bman_B", i);
			walking_frames[DOWN][i]  = atlas.findRegion("Bman_F", i);
			walking_frames[LEFT][i]  = atlas.findRegion("Bman_L", i);
			walking_frames[RIGHT][i] = atlas.findRegion("Bman_R", i);
		}

		// Setup sprite size / original image
		setFrames( walking_frames[walking_direction] );
		TextureRegion first = walking_frames[walking_direction][0];

		// Size of the sprite
		final float width  = first.getRegionWidth();
		final float height = first.getRegionHeight();
		setSize(width, height);

		// Offset from the spite's edge to the side of the tile
		offset_x = (width  - map.getTileWidth()) / 2;
		offset_y = (height - map.getTileHeight()) / 2;

		setMapPosition(map_coord[0], map_coord[1]);
	}

	public void setMapPosition(int map_x, int map_y) {
		this.map_x = map_x;
		this.map_y = map_y;
		setPosition(
			map.getScreenX(map_x) - offset_x,
			map.getScreenY(map_y) + offset_y
		);
	}
	
	/**
	 * If we are trying to move, then move us
	 * Checks bounds, so we can't walk through walls
	 */
	private void updatePosition() {
		float x = getX(), y = getY();
		float dX = 0.0f, dY = 0.0f;

		switch (walking_direction) {
			case UP:
				if (!map.isFree(map_x, map_y + 1)) {
					//float above = map.getScreenY(map_y + 1) - getHeight() + offset_y;
					float above = map.getScreenY(map_y) + offset_y;
					dY = Math.min(WALKING_DISTANCE, above - y);
				} else {
					dY = WALKING_DISTANCE;
				}
				break;

			case DOWN:
				if (!map.isFree(map_x, map_y - 1)) {
					float below = map.getScreenY(map_y) + offset_y;
					dY = -Math.min(WALKING_DISTANCE, y - below);
				} else {
					dY = -WALKING_DISTANCE;
				}
				break;

			case LEFT:
				if (!map.isFree(map_x - 1, map_y)) {
					float left = map.getScreenX(map_x) - offset_x;
					dX = -Math.min(WALKING_DISTANCE, x - left);
				} else {
					dX = -WALKING_DISTANCE;
				}
				break;

			case RIGHT:
				if (!map.isFree(map_x + 1, map_y)) {
					float right = map.getScreenX(map_x + 1) - getWidth() + offset_x;
					dX = Math.min(WALKING_DISTANCE, right - x);
				} else {
					dX = WALKING_DISTANCE;
				}
				break;
			default:
				throw new IllegalArgumentException("Invalid moving direction " + walking_direction);
		}

		if (dX != 0.0f || dY != 0.0f) {
			translate(dX, dY);

			// Update square (possibly)
			map_x = map.getMapX(getX() + getWidth() / 2);
			map_y = map.getMapY(getY());
		}
	}

	protected void animationFrameEnded(int frame) {
		updatePosition();
	}

	public void update (final float dt) {
		if (walking) {
			updateAnimationFrame(dt);
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);

		if (DEBUG) {
			final BitmapFont font = game.getDebugFont();
			font.setColor(Color.BLACK);
			font.draw(batch, map_x + "," + map_y, getX(), getY() + getHeight() / 2);
		}
	}

	
	/**
	 * Start/stop moving in direction
	 * 
	 * @param direction
	 */
	public void move(int direction) {
		if (direction == STOP) {
			walking = false;
		} else {
			walking = true;
			if (direction != walking_direction) {
				walking_direction = direction;
				setFrames(walking_frames[direction]);
			}
		}
	}

	public void dropBomb() {
		if (deployed_bombs >= allowed_bombs)
			return;

		game.dropBomb(this, map_x, map_y);
		deployed_bombs++;
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
