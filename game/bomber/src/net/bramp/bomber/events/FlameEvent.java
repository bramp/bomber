package net.bramp.bomber.events;

import net.bramp.bomber.Flame;
import net.bramp.bomber.utils.events.Event;

/**
 * Somebody set up us the bomb
 * 
 * @author bramp
 *
 */
public class FlameEvent extends Event {

	public static final int FLAME_START = 0;
	public static final int FLAME_END = 1;
	
	public Flame flame;
	public int type;

	@Override
	public void reset() {
		flame = null;
	}

}
