package net.bramp.bomber.events;

import net.bramp.bomber.utils.events.Event;

/**
 * A Brick wall is exploding
 * 
 * @author bramp
 *
 */
public class WallExplodeEvent extends Event {

	public static final int START = 0; // The wall just got hit by a flame
	public static final int END   = 1; // The wall is finished and should fall
	
	public int type = -1;
	
	public int map_x;
	public int map_y;

	@Override
	public void reset() {
		type = -1;
		map_x = map_y = -1;
	}
}
