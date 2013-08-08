package net.bramp.bomber;

import net.bramp.bomber.events.FlameEvent;
import net.bramp.bomber.events.WallExplodeEvent;
import net.bramp.bomber.screens.GameScreen;
import net.bramp.bomber.utils.events.EventBus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pools;

public final class Flame extends MapObject implements SpriteInterface, AnimationInterface {

	public static final String TAG = "Flame";
	
	/**
	 * Coordinates of the center of the flame
	 */
	int min_x, min_y, max_x, max_y;

	final int flame_length;

	boolean isOnFire = true;

	final AnimationComponent animation;

	/**
	 * Creates a new flame (starting at map_x,map_y)
	 * @param game
	 * @param map_x
	 * @param map_y
	 */
	public Flame(GameScreen game, Bomb bomb) {
		super(game.map);

		this.flame_length = bomb.flame_length;

		this.animation = new AnimationComponent(0.5f);

		// Setup textures
		TextureRegion frames[] = game.getTextureRepository().getFlame();
		animation.setFrames(this, frames);

		tile_margin_x = (map.getTileWidth()  - getWidth()) / 2;
		tile_margin_y = (map.getTileHeight() - getHeight()) / 2;

		setMapPosition(bomb.map_x, bomb.map_y);
		calculateBounds();
	}


	@Override
	public void dispose() {}
	
	protected void postWallExplode(int map_x, int map_y) {
		// Broadcast flame event
		WallExplodeEvent event = Pools.obtain(WallExplodeEvent.class);
		event.map_x = map_x;
		event.map_y = map_y;
		
		EventBus.getDefault().post(event);
	}
	
	/**
	 * Calculates the farest left/right/up/down the flame goes
	 */
	protected void calculateBounds() {
		
		min_x = max_x = map_x;
		min_y = max_y = map_y;

		// Left
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x - i, map_y);
			if (t != Map.BLANK) {
				if (t == Map.BRICK) {
					min_x--;
					postWallExplode(min_x, map_y);
				}
				break;
			}
			min_x--;
		}

		// Right
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x + i, map_y);
			if (t != Map.BLANK) {
				if (t == Map.BRICK) {
					max_x++;
					postWallExplode(max_x, map_y);
				}
				break;
			}
			max_x++;
		}

		// Up
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x, map_y + i);
			if (t != Map.BLANK) {
				if (t == Map.BRICK) {
					max_y++;
					postWallExplode(map_x, max_y);
				}
				break;
			}
			max_y++;
		}

		// Down
		for (int i = 1; i <= flame_length; i++) {
			int t = map.getTile(map_x, map_y - i);
			if (t != Map.BLANK) {
				if (t == Map.BRICK) {
					min_y--;
					postWallExplode(map_x, min_y);
				}
				break;
			}
			min_y--;
		}

		Gdx.app.log(TAG, "Flame X:" + min_x + "-" + max_x + " Y:" + min_y + "-" + max_y );
	}

	public void update (final float dt) {
		animation.update(this, dt);
	}

	@Override
	public void animationEnded() {
		FlameEvent event = Pools.obtain(FlameEvent.class);
		event.flame = this;
		event.type = FlameEvent.FLAME_END;

		EventBus.getDefault().post(event);
	}

	@Override
	public void animationFrameEnded(int frame) {}
	
	public void draw(SpriteBatch batch) {
		float screen_x, screen_y;
		
		final float tile_width  = map.getTileWidth();
		final float tile_height = map.getTileHeight();

		screen_x = map.getScreenX(min_x) + tile_margin_x;
		screen_y = map.getScreenY(map_y) + tile_margin_y;
		for (int x = min_x; x <= max_x; x++) {
			batch.draw(this, screen_x, screen_y, tile_width, tile_height);
			screen_x += map.getTileWidth();
		}

		screen_x = map.getScreenX(map_x) + tile_margin_x;
		screen_y = map.getScreenY(min_y) + tile_margin_y;
		for (int y = min_y; y <= max_y; y++) {
			if (y != map_y)
				batch.draw(this, screen_x, screen_y, tile_width, tile_height);
			
			screen_y += map.getTileHeight();
		}
	}
}
