package net.bramp.bomber;

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

	public TextureRepository(TextureAtlas atlas) {
		this.atlas = atlas;

		bomb  = findRegions(atlas, "Bomb");
		flame = findRegions(atlas, "Flame");
		
		player_walking[Player.UP]    = findRegions(atlas, "Bman_B");
		player_walking[Player.DOWN]  = findRegions(atlas, "Bman_F");
		player_walking[Player.LEFT]  = findRegions(atlas, "Bman_L");
		player_walking[Player.RIGHT] = findRegions(atlas, "Bman_R");
		
		map_tile = new TextureAtlas.AtlasRegion[16];
		map_tile[Map.BLANK] = findRegion(atlas, "BackgroundTile");
		map_tile[Map.WALL]  = findRegion(atlas, "SolidBlock");
		map_tile[Map.BRICK] = findRegion(atlas, "ExplodableBlock");

		map_tile[Map.POWERUP_BOMB]  = findRegion(atlas, "BombPowerup");
		map_tile[Map.POWERUP_FLAME] = findRegion(atlas, "FlamePowerup");
		map_tile[Map.POWERUP_SPEED] = findRegion(atlas, "SpeedPowerup");

	}
	
	private static TextureRegion[] findRegions(TextureAtlas atlas, String name) {
		return atlas.findRegions(name).toArray(TextureRegion.class);
	}
	
	private static TextureRegion findRegion(TextureAtlas atlas, String name) {
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
}
