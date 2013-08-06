package net.bramp.bomber;

import com.badlogic.gdx.utils.Pools;

import net.bramp.bomber.events.MapMoveEvent;
import net.bramp.bomber.utils.events.EventBus;

/**
 * Simple physics component for walking on the map
 * Holds the current location
 * 
 * TODO Rename to MapMovementComponent
 * 
 * @author bramp
 *
 */
public class MapMovementComponent {

	/**
	 * Which direction am I facing
	 */
	int direction = Direction.DOWN;

	/**
	 * Distance moved in one frame (in pixels)
	 */
	private static final int DISTANCE = 4;
	
	/**
	 * Am I walking right now?
	 */
	boolean walking = false;

	/**
	 * If we are trying to move, then move us
	 * Checks bounds, so we can't walk through walls
	 */
	public void update(MapObject player) {
		final Map map = player.map;
		final int map_x = player.map_x;
		final int map_y = player.map_y;
		final float x = player.getX();
		final float y = player.getY();

		float dX = 0.0f, dY = 0.0f;

		switch (direction) {
			case Direction.UP:
				if (!map.isFree(map_x, map_y + 1)) {
					float above = map.getScreenY(map_y) + player.tile_margin_y;
					dY = Math.min(DISTANCE, above - y);
				} else {
					dY = DISTANCE;
				}
				break;

			case Direction.DOWN:
				if (!map.isFree(map_x, map_y - 1)) {
					float below = map.getScreenY(map_y) + player.tile_margin_y;
					dY = -Math.min(DISTANCE, y - below);
				} else {
					dY = -DISTANCE;
				}
				break;

			case Direction.LEFT:
				if (!map.isFree(map_x - 1, map_y)) {
					float left = map.getScreenX(map_x) - player.tile_margin_x;
					dX = -Math.min(DISTANCE, x - left);
				} else {
					dX = -DISTANCE;
				}
				break;

			case Direction.RIGHT:
				if (!map.isFree(map_x + 1, map_y)) {
					float right = map.getScreenX(map_x + 1) - player.getWidth() + player.tile_margin_x;
					dX = Math.min(DISTANCE, right - x);
					//System.out.printf("right %f %f %f\n", getX(), right, dX);
				} else {
					dX = DISTANCE;
				}
				break;
			default:
				throw new IllegalArgumentException("Invalid moving direction " + direction);
		}

		if (dX != 0.0f || dY != 0.0f) {
			player.translate(dX, dY);

			// Update map square - TODO use player's origin
			int new_x = map.getMapX(player.getX() + player.getWidth() / 2);
			int new_y = map.getMapY(player.getY());

			if (new_x != map_x || new_y != map_y) {
				player.map_x = new_x; 
				player.map_y = new_y;

				// Send Event to tell others we've moved
				MapMoveEvent event = Pools.obtain(MapMoveEvent.class);
				event.object = player;

				EventBus.getDefault().post(event);
			}
		}
	}
}
