package net.bramp.bomber.events;

import net.bramp.bomber.objects.Player;
import net.bramp.bomber.objects.Powerup;
import net.bramp.bomber.utils.events.Event;

/**
 * 
 * @author bramp
 *
 */
public class PowerupEvent extends Event {

	public static final int DROPPED  = 0;
	public static final int PICKEDUP = 1;

	public int type = -1;
	public Powerup powerup;
	public Player player;

	@Override
	public void reset() {
		type = -1;
		powerup = null;
		player = null;
	}
}
