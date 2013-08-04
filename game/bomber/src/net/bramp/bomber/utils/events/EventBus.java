package net.bramp.bomber.utils.events;

import net.bramp.bomber.utils.ArrayQueue;

import com.badlogic.gdx.utils.IntMap;

/**
 * Simple EventBus that uses some of the libgdx collections.
 * @author bramp
 *
 */
public class EventBus {

	final IntMap<IdentitySet<EventSubscriber>> subscription = new IntMap<IdentitySet<EventSubscriber>>();

	final ArrayQueue<Event> events = new ArrayQueue<Event>();
	boolean processing = false;

	static final EventBus DEFAULT = new EventBus();

	public static EventBus getDefault() {
		return DEFAULT;
	}

	public boolean register(EventSubscriber subscriber, int eventType) {
		IdentitySet<EventSubscriber> subscribers = subscription.get(eventType);
		if (subscribers == null) {
			subscribers = new IdentitySet<EventSubscriber>();
			subscription.put(eventType, subscribers);
		}
		return subscribers.add(subscriber);
	}

	public boolean unregister(EventSubscriber subscriber, int eventType) {
		IdentitySet<EventSubscriber> subscribers = subscription.get(eventType);
		if (subscribers == null) {
			return false;
		}
		return subscribers.remove(subscriber);
	}

	protected void postInternal(final Event event) {
		IdentitySet<EventSubscriber> subscribers = subscription.get(event.getType());
		if (subscribers == null) {
			return;
		}	
		for (EventSubscriber subscriber : subscribers) {
			subscriber.onEvent(event);
		}
	}
	
	/**
	 * Posts a event, and processes them in the order they arrive 
	 * @param event
	 */
	public void post(final Event event) {
		events.push(event);

		// We are currently processing, so defer until later
		if (processing)
			return;

		try {
			processing = true;

			Event e;
			while ((e = events.pop()) != null) {
				postInternal(e);
			}

		} finally {
			processing = false;
		}
	}
}
