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

	public int type;
	public Powerup powerup;
	public Player player;

	@Override
	public void reset() {
		powerup = null;
		player = null;
	}
}
