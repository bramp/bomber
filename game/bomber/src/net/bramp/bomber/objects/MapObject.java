package net.bramp.bomber.objects;

import net.bramp.bomber.Map;
import net.bramp.bomber.SpriteInterface;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * A sprite that has a coordinate on the map
 * 
 * @author bramp
 *
 */
public abstract class MapObject extends Sprite implements SpriteInterface {

	/**
	 * The map we are on
	 */
	public final Map map;

	/**
	 * The space between the sprite, and the tile edges (used for collision detection)
	 */
	public float tile_margin_x = 0, tile_margin_y = 0;

	/**
	 * Coordinates on the map I am
	 */
	public int map_x = 0, map_y = 0;

	protected MapObject(Map map) {
		this.map = map;
	}

	/**
	 * Moves the Object to the center of the x/y tile, and updates the screen x/y coords
	 * @param map_x
	 * @param map_y
	 */
	public void setMapPosition(int map_x, int map_y) {
		this.map_x = map_x;
		this.map_y = map_y;
		setPosition(
			map.getScreenX(map_x) + tile_margin_x,
			map.getScreenY(map_y) + tile_margin_y
		);
	}
}
