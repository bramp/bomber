package net.bramp.bomber.events;

import net.bramp.bomber.Bomb;
import net.bramp.bomber.utils.events.Event;

/**
 * Somebody set up us the bomb
 * 
 * @author bramp
 *
 */
public class BombExplodedEvent extends Event {

	public Bomb bomb;

	@Override
	public void reset() {
		bomb = null;
	}

}
