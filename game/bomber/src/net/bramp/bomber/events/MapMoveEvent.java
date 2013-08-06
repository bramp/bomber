package net.bramp.bomber.events;

import net.bramp.bomber.MapObject;
import net.bramp.bomber.utils.events.Event;

/**
 * A MapObject moved on the map
 * 
 * @author bramp
 *
 */
public class MapMoveEvent extends Event {
	public MapObject object;

	@Override
	public void reset() {
		object = null;
	}
	
	
}
