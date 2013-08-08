package net.bramp.bomber.events;

import net.bramp.bomber.utils.events.Event;

/**
 * A Brick wall is exploding
 * 
 * @author bramp
 *
 */
public class WallExplodeEvent extends Event {

	public int map_x;
	public int map_y;

	@Override
	public void reset() {
		map_x = map_y = -1;
	}
}
