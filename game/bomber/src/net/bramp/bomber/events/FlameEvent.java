package net.bramp.bomber.events;

import net.bramp.bomber.objects.Flame;
import net.bramp.bomber.utils.events.Event;

/**
 * Somebody set up us the bomb
 * 
 * @author bramp
 *
 */
public class FlameEvent extends Event {

	public static final int START = 0;
	public static final int END = 1;
	
	public int type = -1;
	public Flame flame;

	@Override
	public void reset() {
		type = -1;
		flame = null;
	}

}
