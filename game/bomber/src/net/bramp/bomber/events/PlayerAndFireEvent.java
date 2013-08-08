package net.bramp.bomber.events;

import net.bramp.bomber.Player;
import net.bramp.bomber.utils.events.Event;

/**
 * A player collided with fire
 * 
 * @author bramp
 *
 */
public class PlayerAndFireEvent extends Event {

	public Player player;

	@Override
	public void reset() {
		player = null;
	}
}
