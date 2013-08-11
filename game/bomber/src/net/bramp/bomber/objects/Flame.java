package net.bramp.bomber.objects;

import net.bramp.bomber.AnimationInterface;
import net.bramp.bomber.Map;
import net.bramp.bomber.SpriteInterface;
import net.bramp.bomber.components.AnimationComponent;
import net.bramp.bomber.events.FlameEvent;
import net.bramp.bomber.events.WallExplodeEvent;
import net.bramp.bomber.screens.GameScreen;
import net.bramp.bomber.utils.events.Event;
import net.bramp.bomber.utils.events.EventBus;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public final class Flame extends MapObject implements SpriteInterface, AnimationInterface {

	public static final String TAG = "Flame";
	
	/**
	 * Coordinates of the center of the flame
	 */
	public int min_x, min_y, max_x, max_y;

	final int flame_length;

	boolean isOnFire = true;

	final AnimationComponent animation;

	/**
	 * Queue of events for the walls which will be destroyed at the end of this flame
	 */
	Array<Event> queuedEvents = new Array<Event>(false, 4);

	/**
	 * Creates a new flame (starting at map_x,map_y)
	 * @param game
	 * @param map_x
	 * @param map_y
	 */
	public Flame(GameScreen game, Bomb bomb) {
		super(game.map);

		this.flame_length = bomb.flame_length;

		this.animation = new AnimationComponent(this, 0.5f);
		this.animation.setListener(this);

		// Setup textures
		TextureRegion frames[] = game.getTextureRepository().getFlame();
		animation.setFrames(frames);

		tile_margin_x = (map.getTileWidth()  - getWidth()) / 2;
		tile_margin_y = (map.getTileHeight() - getHeight()) / 2;

		setMapPosition(bomb.map_x, bomb.map_y);
		calculateBounds();
	}


	@Override
	public void dispose() {
		queuedEvents.clear();
		
	}
	
	protected void postAndQueueWallExplodeEvent(int map_x, int map_y) {
		if (!map.isOnFire(map_x, map_y)) {
			WallExplodeEvent event;
	
			// Post this event now
			event = Pools.obtain(WallExplodeEvent.class);
			event.type = WallExplodeEvent.START;
			event.map_x = map_x;
			event.map_y = map_y;
	
			EventBus.getDefault().post(event);
	
			// Queue this event for later
			event = Pools.obtain(WallExplodeEvent.class);
			event.type = WallExplodeEvent.END;
			event.map_x = map_x;
			event.map_y = map_y;
	
			queuedEvents.add(event);
		}
	}
	
	/**
	 * Calculates the farthest left/right/up/down the flame goes
	 */
	protected void calculateBounds() {

		min_x = max_x = map_x;
		min_y = max_y = map_y;

		// Left
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x - i, map_y) & Map.BLOCK_MASK;
			if (t != Map.BLANK) {
				if (t == Map.BRICK) {
					min_x--;
					postAndQueueWallExplodeEvent(min_x, map_y);
				}
				break;
			}
			min_x--;
		}

		// Right
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x + i, map_y) & Map.BLOCK_MASK ;
			if (t != Map.BLANK) {
				if (t == Map.BRICK) {
					max_x++;
					postAndQueueWallExplodeEvent(max_x, map_y);
				}
				break;
			}
			max_x++;
		}

		// Up
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x, map_y + i) & Map.BLOCK_MASK;
			if (t != Map.BLANK) {
				if (t == Map.BRICK) {
					max_y++;
					postAndQueueWallExplodeEvent(map_x, max_y);
				}
				break;
			}
			max_y++;
		}

		// Down
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x, map_y - i) & Map.BLOCK_MASK;
			if (t != Map.BLANK) {
				if (t == Map.BRICK) {
					min_y--;
					postAndQueueWallExplodeEvent(map_x, min_y);
				}
				break;
			}
			min_y--;
		}
	}

	public void update (final float dt) {
		animation.update(dt);
	}

	@Override
	public void animationEnded() {

		FlameEvent event = Pools.obtain(FlameEvent.class);
		event.flame = this;
		event.type = FlameEvent.END;

		EventBus.getDefault().postAll(queuedEvents);
		EventBus.getDefault().post(event);
	}

	@Override
	public void animationFrameEnded(int frame) {}
	
	public void draw(SpriteBatch batch) {
		float screen_x, screen_y;
		
		final float width = getWidth();
		final float height = getWidth();

		final float tile_width  = map.getTileWidth();
		final float tile_height = map.getTileHeight();

		screen_x = map.getScreenX(min_x) + tile_margin_x;
		screen_y = map.getScreenY(map_y) + tile_margin_y;
		for (int x = min_x; x <= max_x; x++) {
			batch.draw(this, screen_x, screen_y, width, height);
			screen_x += tile_width;
		}

		screen_x = map.getScreenX(map_x) + tile_margin_x;
		screen_y = map.getScreenY(min_y) + tile_margin_y;
		for (int y = min_y; y <= max_y; y++) {
			if (y != map_y)
				batch.draw(this, screen_x, screen_y, width, height);
			
			screen_y += tile_height;
		}
	}
}
