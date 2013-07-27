package net.bramp.bomber.screens;

import net.bramp.bomber.Map;
import net.bramp.bomber.Player;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameScreen implements ApplicationListener {

	private Map map = new Map(15, 13);

	private TextureAtlas atlas;
	private final TextureRegion[] map_textures = new TextureRegion[16];
	private final Player[] players = new Player[4];

	private int number_of_players = 0;

	private SpriteBatch batch;
	private OrthographicCamera camera;

	GameScreenInputProcessor inputProcessor;

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

		map_textures[Map.BLANK] = atlas.findRegion("BackgroundTile");
		map_textures[Map.WALL]  = atlas.findRegion("SolidBlock");
		map_textures[Map.BRICK] = atlas.findRegion("ExplodableBlock");

		map_textures[Map.POWERUP_BOMB] = atlas.findRegion("BombPowerup");
		map_textures[Map.POWERUP_FLAME] = atlas.findRegion("FlamePowerup");
		map_textures[Map.POWERUP_SPEED] = atlas.findRegion("SpeedPowerup");

		players[number_of_players++] = new Player(this, map.getPlayerStart(0));
		players[number_of_players++] = new Player(this, map.getPlayerStart(1));
		players[number_of_players++] = new Player(this, map.getPlayerStart(2));
		players[number_of_players++] = new Player(this, map.getPlayerStart(3));

		/*
		sprite = new Sprite(region);
		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
		*/

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
			map.render(batch, map_textures);

			switch (number_of_players) { // No breaks so it fall throughs
				case 4: players[3].draw(batch);
				case 3: players[2].draw(batch);
				case 2: players[1].draw(batch);
				case 1: players[0].draw(batch);
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

}
