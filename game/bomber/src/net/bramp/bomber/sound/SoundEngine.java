package net.bramp.bomber.sound;

import net.bramp.bomber.events.FlameEvent;
import net.bramp.bomber.utils.events.Event;
import net.bramp.bomber.utils.events.EventBus;
import net.bramp.bomber.utils.events.EventListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

/**
 * Deals with all the sound
 * 
 * @author bramp
 *
 */
public class SoundEngine implements Disposable, EventListener {

	private static String TAG = "SoundEngine";

	public SoundEngine() {
		EventBus.getDefault().register(this, FlameEvent.class);
	}

	@Override
	public void dispose() {
		EventBus.getDefault().unregister(this);		
	}

	@Override
	public void onEvent(Event e) {
		if (e instanceof FlameEvent) {
			FlameEvent fe = (FlameEvent)e;
			if (fe.type == FlameEvent.START) {
				Gdx.app.log(TAG, "Boom");
			} else if (fe.type == FlameEvent.END) {
				Gdx.app.log(TAG, "Fin");
			}
			// TODO Play sound
		}
	}

	public void pause() {
		// TODO
	}

	public void resume() {
		// TODO
	}
}
