package net.bramp.bomber;

import net.bramp.bomber.events.BombExplodedEvent;
import net.bramp.bomber.screens.GameScreen;
import net.bramp.bomber.utils.events.Event;
import net.bramp.bomber.utils.events.EventBus;
import net.bramp.bomber.utils.events.EventSubscriber;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class Player extends MapObject implements AnimationInterface, EventSubscriber {
	private static final boolean DEBUG = Config.DEBUG;

	final GameScreen game;

	final TextureRegion[][] walking_frames;

	/**
	 * Am I alive?
	 */
	boolean alive = true;

	int flame_length = 1;
	int allowed_bombs = 1;
	int deployed_bombs = 0;

	final AnimationComponent animation;
	final MapMovementComponent movement;

	public Player(GameScreen game, int[] map_coord) {
		super(game.getMap());

		this.game = game;
		
		animation = new AnimationComponent(0.01f);
		movement = new MapMovementComponent();

		// Setup textures
		walking_frames = game.getTextureRepository().getPlayerWalking();
		animation.setFrames(this, walking_frames[movement.direction]);

		// Offset from the spite's edge to the side of the tile
		tile_margin_x = (getWidth()  - map.getTileWidth()) / 2;
		tile_margin_y = (getHeight() - map.getTileHeight()) / 2;

		setMapPosition(map_coord[0], map_coord[1]);
		
		EventBus.getDefault().register(this, BombExplodedEvent.class);
	}

	@Override
	public void animationEnded() {
		// Do nothing
	}

	@Override
	public void animationFrameEnded(int frame) {
		movement.update(this);
	}

	public void update (final float dt) {
		// TODO input update

		if (movement.walking) {
			animation.update(this, dt);
		}

		if (map.isOnFire(map_x, map_y)) {
			die();
		}
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);

		if (DEBUG) {
			final BitmapFont font = game.getDebugFont();
			font.setColor(Color.BLACK);
			font.draw(batch, map_x + "," + map_y, getX(), getY() + getHeight() / 2);
		}
	}

	protected void die() {
		alive = false;
		game.killPlayer(this);
	}
	
	/**
	 * Start/stop moving in direction
	 * 
	 * @param direction
	 */
	public void move(int direction) {
		if (direction == Direction.STOP) {
			movement.walking = false;
		} else {
			movement.walking = true;
			if (direction != movement.direction) {
				movement.direction = direction;
				animation.setFrames(this, walking_frames[direction]);
			}
		}
	}

	public void dropBomb() {
		if (deployed_bombs >= allowed_bombs)
			return;

		if (game.dropBomb(this, map_x, map_y)) {
			deployed_bombs++;
		}
	}

	public void onEvent(BombExplodedEvent e) {
		if (e.bomb.owner == this) {
			deployed_bombs--;
			assert(deployed_bombs >= 0);
		}
	}
	
	@Override
	public void onEvent(Event e) {
		if (e instanceof BombExplodedEvent) {
			onEvent((BombExplodedEvent)e);
		}
	}
}
