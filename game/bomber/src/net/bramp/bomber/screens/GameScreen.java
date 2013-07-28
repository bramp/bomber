package net.bramp.bomber.screens;

import net.bramp.bomber.Map;
import net.bramp.bomber.Player;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameScreen implements ApplicationListener {

	private static final boolean DEBUG = true;
	
	private TextureAtlas atlas;
	private Map map;
	private final Player[] players = new Player[4];

	private int number_of_players = 0;

	private SpriteBatch batch;
	private OrthographicCamera camera;

	GameScreenInputProcessor inputProcessor;

	BitmapFont debugFont;
	
	@Override
	public void create() {

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		atlas = new TextureAtlas(
			Gdx.files.internal("data/bomber.txt"),
			Gdx.files.internal("data")
		);
		
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
		switch (number_of_players) { // No breaks so it fall through
			case 4: players[3].update(dt);
			case 3: players[2].update(dt);
			case 2: players[1].update(dt);
			case 1: players[0].update(dt);
		}
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

	public TextureAtlas getTextureAtlas() {
		return atlas;
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

}
