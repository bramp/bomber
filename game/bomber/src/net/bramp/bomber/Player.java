package net.bramp.bomber;

import net.bramp.bomber.screens.GameScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public final class Player extends Sprite {
	private static final boolean DEBUG = true;

	public static final int STOP  = -1;
	public static final int UP    = 0;
	public static final int DOWN  = 1;
	public static final int LEFT  = 2;
	public static final int RIGHT = 3;

	/**
	 * Time between image frames (in seconds)
	 * We vary this to speed the player up
	 */
	private float WALKING_INTERVAL = 0.01f;

	/**
	 * Distance moved in one frame (in pixels)
	 */
	private static final int WALKING_DISTANCE = 4;

	/**
	 * Number of walking animation frames
	 */
	private static final int WALKING_FRAMES = 8;
	
	final GameScreen game;
	final Map map;

	static final TextureRegion[][] body = new TextureRegion[4][WALKING_FRAMES];

	/**
	 * Which walking animation frame I'm on
	 */
	int walking_frame = 0;
	
	/**
	 * How far into the animation frame we are (in seconds)
	 */
	float walking_animation = 0.0f;
	
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
	
	final float offset_x, offset_y;

	public Player(GameScreen game, int[] map_coord) {

		this.game = game;
		this.map  = game.getMap();

		// Setup textures
		final TextureAtlas atlas = game.getTextureAtlas();

		for (int i = 0; i < WALKING_FRAMES; i++) {
			body[UP][i]    = atlas.findRegion("Bman_B", i);
			body[DOWN][i]  = atlas.findRegion("Bman_F", i);
			body[LEFT][i]  = atlas.findRegion("Bman_L", i);
			body[RIGHT][i] = atlas.findRegion("Bman_R", i);
		}
		
		// Setup sprite size / original image
		TextureRegion first = body[walking_direction][walking_frame];
		setRegion(first);

		float width  = first.getRegionWidth();
		float height = first.getRegionHeight();

		setSize(width, height);

		offset_x = (getWidth() - map.getTileWidth()) / 2;
		offset_y = (getHeight() - map.getTileHeight()) / 2;

		setMapPosition(map_coord[0], map_coord[1]);
	}
	
	public void setMapPosition(int map_x, int map_y) {
		this.map_x = map_x;
		this.map_y = map_y;
		setPosition(
			map.getScreenX(map_x) + offset_x,
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
					float above = map.getScreenY(map_y + 1) - getHeight() + offset_y;
					dY = Math.min(WALKING_DISTANCE, above - y);
				} else {
					dY = WALKING_DISTANCE;
				}
				break;

			case DOWN:
				if (!map.isFree(map_x, map_y - 1)) {
					float below = map.getScreenY(map_y) - offset_y;
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
			map_y = map.getMapY(getY() + getHeight() / 2);
		}
	}

	public void updateAnimationFrame(final float dt) {

		if (walking) {
			walking_animation += dt;
			if (walking_animation > WALKING_INTERVAL) {
				walking_animation -= WALKING_INTERVAL;
				walking_frame++;
				if (walking_frame >= WALKING_FRAMES)
					walking_frame = 0;

				setRegion( body[walking_direction][walking_frame] );				
				updatePosition();
			}
		}
	}

	public void update (final float dt) {
		updateAnimationFrame(dt);

		//super.update(dt);
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

	public static boolean within(Vector3 v, int width, int height) {
		return v.x >= 0 && v.x < width && v.y >= 0 && v.y < height;
	}

	/**
	 * Start/stop moving in direction
	 * 
	 * @param direction
	 */
	public void move(int direction) {

		if (direction != walking_direction || !walking) {

			if (direction == STOP) {
				walking = false;
			} else {
				walking = true;
				walking_direction = direction;
				walking_frame = 0; // TODO do we need to reset this?

				setRegion(body[direction][walking_frame]);
			}
		}
	}

}
