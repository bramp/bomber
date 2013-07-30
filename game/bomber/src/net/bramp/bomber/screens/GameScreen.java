package net.bramp.bomber.screens;

import java.util.ArrayList;

import net.bramp.bomber.Bomb;
import net.bramp.bomber.Config;
import net.bramp.bomber.Map;
import net.bramp.bomber.Player;
import net.bramp.bomber.SpriteInterface;
import net.bramp.bomber.TextureRepository;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

public class GameScreen implements ApplicationListener {

	private static final boolean DEBUG = Config.DEBUG;

	/**
	 * Simple value of what time is now (in seconds from game start)
	 */
	private float now = 0.0f;
	
	private TextureAtlas atlas;
	private TextureRepository textureRepo;

	private Map map;
	private final Player[] players = new Player[4];

	private int number_of_players = 0;

	private SpriteBatch batch;
	private OrthographicCamera camera;

	GameScreenInputProcessor inputProcessor;

	BitmapFont debugFont;
	
	final SnapshotArray<SpriteInterface> sprites = new SnapshotArray<SpriteInterface>(false, 16, SpriteInterface.class);

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

	}

	@Override
	public void dispose() {
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

			switch (number_of_players) { // No breaks so it fall throughs
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

	public float getMapX(int map_x) {
		return 0;
	}

	public float getMapY(int map_y) {
		return 0;
	}

	public Map getMap() {
		return map;
	}

	public BitmapFont getDebugFont() {
		return debugFont;
	}

	public void addSprite(SpriteInterface sprite) {
		sprites.add(sprite);
	}

	public void removeSprite(SpriteInterface sprite) {
		sprites.removeValue(sprite, true);
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

	public void bombExploded(Bomb bomb) {
		removeSprite(bomb);
		bomb.getOwner().bombExploded(bomb);

		// Add flames

		// Players check (each update) if they are on a flame
	}

	/**
	 * A player died
	 * @param player
	 */
	public void killPlayer(Player player) {
		
	}
}
