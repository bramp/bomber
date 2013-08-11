package net.bramp.bomber.events;

import net.bramp.bomber.objects.MapObject;
import net.bramp.bomber.utils.events.Event;

/**
 * @author bramp
 *
 */
public class MapObjectEvent extends Event {

	public static final int SPAWN = 0;
	public static final int MOVED = 1;
	public static final int DIE   = 2;

	public int type = -1;
	public MapObject object;

	@Override
	public void reset() {
		type = -1;
		object = null;
	}
	
	
}
