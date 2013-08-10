package net.bramp.bomber.screens;

import net.bramp.bomber.Config;
import net.bramp.bomber.Map;
import net.bramp.bomber.SpriteInterface;
import net.bramp.bomber.TextureRepository;
import net.bramp.bomber.events.BombEvent;
import net.bramp.bomber.events.FlameEvent;
import net.bramp.bomber.events.WallExplodeEvent;
import net.bramp.bomber.objects.Bomb;
import net.bramp.bomber.objects.Flame;
import net.bramp.bomber.objects.Player;
import net.bramp.bomber.objects.Powerup;
import net.bramp.bomber.sound.SoundEngine;
import net.bramp.bomber.utils.events.Event;
import net.bramp.bomber.utils.events.EventBus;
import net.bramp.bomber.utils.events.EventListener;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.SnapshotArray;

public class GameScreen implements ApplicationListener, Disposable, EventListener {

	private static final boolean DEBUG = Config.DEBUG;

	/**
	 * Simple value of what time is now (in seconds from game start)
	 */
	private float now = 0.0f;
	
	private TextureAtlas atlas;
	private TextureRepository textureRepo;

	public Map map;
	public final Player[] players = new Player[4];

	public int number_of_players = 0;

	private SpriteBatch batch;
	private OrthographicCamera camera;

	SoundEngine sound;
	GameScreenInputProcessor inputProcessor;

	BitmapFont debugFont;

	final SnapshotArray<SpriteInterface> sprites = new SnapshotArray<SpriteInterface>(false, 16, SpriteInterface.class);
	final Array<Flame> flames = new Array<Flame>();
	
	@Override
	public void create() {

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		atlas = new TextureAtlas(
			Gdx.files.internal("data/bomber.txt"),
			Gdx.files.internal("data")
		);

		// Load all the textures
		textureRepo = new TextureRepository(atlas);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);

		batch = new SpriteBatch();

		map = new Map(this, 15, 13);

		players[number_of_players++] = new Player(this, map.getPlayerStart(0));
		players[number_of_players++] = new Player(this, map.getPlayerStart(1));
		players[number_of_players++] = new Player(this, map.getPlayerStart(2));
		players[number_of_players++] = new Player(this, map.getPlayerStart(3));

		debugFont = new BitmapFont();

		inputProcessor = new GameScreenInputProcessor(this);
		Gdx.input.setInputProcessor(inputProcessor);

		sound = new SoundEngine();

		registerForEvents();
	}
	
	@SuppressWarnings("unchecked")
	private void registerForEvents() {
		EventBus.getDefault().register(this, 
				BombEvent.class, FlameEvent.class, WallExplodeEvent.class);		
	}

	@Override
	public void dispose() {
		EventBus.getDefault().unregister(this);
		batch.dispose();
		atlas.dispose();
	}

	public void update(float dt) {
		now += dt;

		switch (number_of_players) { // No breaks so it fall through
			case 4: players[3].update(dt);
			case 3: players[2].update(dt);
			case 2: players[1].update(dt);
			case 1: players[0].update(dt);
		}

		SpriteInterface[] items = sprites.begin();
		for (int i = 0, len = sprites.size; i < len; i++) {
			items[i].update(dt);
		}
		sprites.end();
	}

	@Override
	public void render() {

		// Update the state of everything before render
		final float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		try {
			map.render(batch);

			SpriteInterface[] items = sprites.items;
			for (int i = 0, len = sprites.size; i < len; i++) {
				items[i].draw(batch);
			}

			switch (number_of_players) { // No breaks so it fall through
				case 4: players[3].draw(batch);
				case 3: players[2].draw(batch);
				case 2: players[1].draw(batch);
				case 1: players[0].draw(batch);
			}

			if (DEBUG) {
				debugFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
			}

		} finally {
			batch.end();
		}
	}

	public float now() {
		return now;
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public Player[] getPlayers() {
		return players;
	}

	public TextureRepository getTextureRepository() {
		return textureRepo;
	}

	public BitmapFont getDebugFont() {
		return debugFont;
	}

	public void addSprite(SpriteInterface sprite) {
		sprites.add(sprite);
	}

	public void removeSprite(SpriteInterface sprite) {
		sprites.removeValue(sprite, true);
		
		// TODO Try pooling the sprite
	}
	
	/**
	 * Drops a bomb
	 * returns false if it's not allowed
	 * @param player
	 * @param map_x
	 * @param map_y
	 */
	public boolean dropBomb(Player player, int map_x, int map_y) {
		// TODO check tile doesn't already have a bomb
		
		addSprite( new Bomb(this, player, map_x, map_y) );
		return true;
	}

	public void onEvent(BombEvent event) {
		Bomb bomb = event.bomb;

		// Add flames
		if (event.type == BombEvent.EXPLODED) {
			// Broadcast flame event
			FlameEvent flameEvent = Pools.obtain(FlameEvent.class);
			flameEvent.flame = new Flame(this, bomb);
			flameEvent.type = FlameEvent.START;

			EventBus.getDefault().post(flameEvent);

		} else if (event.type == BombEvent.FAILED) {

		}

		// Remove bomb
		removeSprite(bomb);
	}

	public void onEvent(FlameEvent event) {
		if (event.type == FlameEvent.START) {
			map.setFire(event.flame, true);
			addSprite(event.flame);

		} else if (event.type == FlameEvent.END) {
			map.setFire(event.flame, false);
			flames.removeValue(event.flame, true);

			// We might have turned off a flame we shoudn't have, so lets readd them all
			for (int i = 0, len = flames.size; i < len; i++) {
				map.setFire(flames.items[i], true);
			}

			removeSprite(event.flame);
		}
	}
	
	public static final float CHANCE_OF_DROP = 0.5f;

	public void onEvent(WallExplodeEvent event) {
		map.destroyWall(event.map_x, event.map_y);

		float r = MathUtils.random();
		if (r < CHANCE_OF_DROP) {
			Powerup powerup = new Powerup(this, event.map_x, event.map_y);
			addSprite(powerup);
			map.addPowerup(powerup);
		}
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof BombEvent) {
			onEvent((BombEvent)event);
		} else if (event instanceof FlameEvent) {
			onEvent((FlameEvent) event);
		} else if (event instanceof WallExplodeEvent) {
			onEvent((WallExplodeEvent) event);
		}
	}
}
