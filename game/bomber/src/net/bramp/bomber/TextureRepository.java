package net.bramp.bomber;

import net.bramp.bomber.objects.Powerup;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureRepository {

	final TextureAtlas atlas;

	/**
	 * Frames of the player, 4 directions
	 */
	final TextureRegion[][] player_walking = new TextureRegion[4][];
	final TextureRegion[] bomb;
	final TextureRegion[] flame;
	final TextureRegion[] map_tile;
	final TextureRegion[] powerups;

	public TextureRepository(TextureAtlas atlas) {
		this.atlas = atlas;

		bomb  = findRegions("Bomb");
		flame = findRegions("Flame");

		player_walking[Direction.UP]    = findRegions("Bman_B");
		player_walking[Direction.DOWN]  = findRegions("Bman_F");
		player_walking[Direction.LEFT]  = findRegions("Bman_L");
		player_walking[Direction.RIGHT] = findRegions("Bman_R");

		map_tile = new TextureAtlas.AtlasRegion[4];
		map_tile[Map.BLANK] = findRegion("BackgroundTile");
		map_tile[Map.WALL]  = findRegion("SolidBlock");
		map_tile[Map.BRICK] = findRegion("ExplodableBlock");

		powerups  = new TextureRegion[3];
		powerups[Powerup.BOMB]  = findRegion("BombPowerup");
		powerups[Powerup.FLAME] = findRegion("FlamePowerup");
		powerups[Powerup.SPEED] = findRegion("SpeedPowerup");
	}

	private TextureRegion[] findRegions(String name) {
		return atlas.findRegions(name).toArray(TextureRegion.class);
	}
	
	private TextureRegion findRegion(String name) {
		return atlas.findRegion(name);
	}

	public TextureRegion[][] getPlayerWalking() {
		return player_walking;
	}

	public TextureRegion[] getBomb() {
		return bomb;
	}

	public TextureRegion[] getFlame() {
		return flame;
	}

	public TextureRegion[] getMapTile() {
		return map_tile;
	}
	
	public TextureRegion[] getPowerups() {
		return powerups;
	}
}
