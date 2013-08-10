package net.bramp.bomber.objects;

import net.bramp.bomber.screens.GameScreen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public final class Powerup extends MapObject {

	public static final byte BOMB =  0;
	public static final byte FLAME = 1;
	public static final byte SPEED = 2;
	
	public static final byte MAX = 3;

	public int type;
	
	public Powerup(GameScreen game, int map_x, int map_y) {
		super(game.map);

		type = MathUtils.random(MAX - 1);

		// Setup textures
		TextureRegion frames[] = game.getTextureRepository().getPowerups();
		setRegion(frames[type]);

		tile_margin_x = (map.getTileWidth()  - getWidth()) / 2;
		tile_margin_y = (map.getTileHeight() - getHeight()) / 2;

		setMapPosition(map_x, map_y);
	}

	@Override
	public void dispose() {}

	public void update (final float dt) {}

}
