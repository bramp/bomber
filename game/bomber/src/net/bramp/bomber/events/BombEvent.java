package net.bramp.bomber.events;

import net.bramp.bomber.objects.Bomb;
import net.bramp.bomber.utils.events.Event;

/**
 * Somebody set up us the bomb
 * 
 * @author bramp
 *
 */
public class BombEvent extends Event {

	public static final int DROPPED  = 0;
	public static final int EXPLODED = 1;
	public static final int FAILED   = 2; // because it was a dud

	public int type;
	public Bomb bomb;

	@Override
	public void reset() {
		bomb = null;
	}
}
