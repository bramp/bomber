package net.bramp.bomber.utils.events;

import net.bramp.bomber.utils.ArrayQueue;
import net.bramp.bomber.utils.IdentitySet;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.Pools;

/**
 * Simple EventBus that uses some of the libgdx collections.
 * @author bramp
 *
 */
public class EventBus {

	final IdentityMap<Class<? extends Event>, IdentitySet<EventListener>> subscription = 
			new IdentityMap<Class<? extends Event>, IdentitySet<EventListener>>();

	final ArrayQueue<Event> events = new ArrayQueue<Event>();
	boolean processing = false;

	static final EventBus DEFAULT = new EventBus();

	public static EventBus getDefault() {
		return DEFAULT;
	}

	public void register(EventListener subscriber, Class<? extends Event> ... eventTypes) {
		for(int i = 0, len = eventTypes.length; i < len; i++) {
			register(subscriber, eventTypes[i]);
		}
	}

	public void register(EventListener subscriber, Class<? extends Event> eventType) {
		IdentitySet<EventListener> subscribers = subscription.get(eventType);
		if (subscribers == null) {
			subscribers = new IdentitySet<EventListener>();
			subscription.put(eventType, subscribers);
		}
		subscribers.add(subscriber);
	}

	/**
	 * Brute force unregister
	 * @param subscriber
	 * @return
	 */
	public void unregister(EventListener subscriber) {
		for (IdentitySet<EventListener> subscribers : subscription.values()) {
			subscribers.remove(subscriber);
		}
	}
	
	public void unregister(EventListener subscriber, Class<? extends Event> eventType) {
		IdentitySet<EventListener> subscribers = subscription.get(eventType);
		if (subscribers != null) {
			subscribers.remove(subscriber);
		}
	}

	protected void postOne(final Event event) {
		IdentitySet<EventListener> subscribers = subscription.get(event.getClass());
		if (subscribers == null) {
			return;
		}	
		for (EventListener subscriber : subscribers) {
			subscriber.onEvent(event);
		}
		
		// Now we free this class, so it might be reused
		Pools.free(event);
	}
	
	protected void postInternal() {

		// We are currently processing, so defer until later
		if (processing)
			return;

		try {
			processing = true;

			Event e;
			while ((e = events.pop()) != null) {
				postOne(e);
			}

		} finally {
			processing = false;
		}		
	}
	
	/**
	 * Posts a event, and processes them in the order they arrive 
	 * @param event
	 */
	public void post(final Event event) {
		events.push(event);
		postInternal();
	}
	
	/**
	 * Posts multiple events, and processes them in the order they arrive 
	 * @param event
	 */
	public void postAll(final Array<Event> event) {
		events.push(event);
		postInternal();
	}
}
