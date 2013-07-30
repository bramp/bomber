package net.bramp.bomber;

import net.bramp.bomber.screens.GameScreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.base.Preconditions;

public final class Map {
	private static final boolean DEBUG = Config.DEBUG;

	// If things are odd, they 
	public static final byte BLANK = 0x0; // 0b00 0000
	public static final byte WALL  = 0x1; // 0b00 0001
	public static final byte BRICK = 0x3; // 0b00 0011

	public static final byte BLOCK_MASK = WALL | BRICK;

	public static final byte POWERUP_BOMB =  0x4; // 0b00 0100
	public static final byte POWERUP_FLAME = 0x8; // 0b00 1000
	public static final byte POWERUP_SPEED = 0xc; // 0b00 1100
	public static final byte POWERUP_MASK  = POWERUP_BOMB | POWERUP_FLAME | POWERUP_SPEED;
	
	public static final byte ON_FIRE   = 0x10; // 0b01 0000
	public static final byte FIRE_MASK = 0x30;

	/**
	 * Probability of a square having a brick wall (at round start)
	 */
	static final float BRICK_PROB = 0.85f;

	static final float tile_width  = 64f;
	static final float tile_height = 64f;

	private final GameScreen game;
	private final TextureRegion[] tile_textures;
	private final TextureRegion[] fire_textures;
	
	//
	final int width, height;
	final byte[][] map;
	
	public Map(GameScreen game, int width, int height) {
		Preconditions.checkArgument( width >= 5 && (width - 3) % 2 == 0);
		Preconditions.checkArgument( height >= 5 && (height - 3) % 2 == 0);

		this.game = game;

		final TextureRepository repo = game.getTextureRepository();
		tile_textures  = repo.getMapTile();
		fire_textures = repo.getFlame();

		this.width = width;
		this.height = height;
		this.map = new byte[width][height];

		setupWalls();
	}

	/**
	 * Setups the map grid to have a standard layout
	 */
	private void setupWalls() {
		// Top and Bottom walls
		for (int x = 0; x < width; x++) {
			map[x][0] = map[x][height - 1] = WALL;
		}
		// Left and Right walls
		for (int y = 0; y < height; y++) {
			map[0][y] = map[width - 1][y] = WALL;
		}
		// Every other x/y
		for (int x = 2; x < width; x+=2) {
			for (int y = 2; y < height; y+=2) {
				map[x][y] = WALL;
			}
		}

		// Now setup bricks
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				if ((x & 1) == 0 && (y & 1) == 0) {
					map[x][y] = WALL;
				} else {
					if (Math.random() < BRICK_PROB)
						map[x][y] = BRICK;
				}
			}
		}
		
		// Ensure there is space for people to stand
		map[1][1] = BLANK; map[2][1] = BLANK; map[1][2] = BLANK;
		map[width - 2][1] = BLANK; map[width - 3][1] = BLANK; map[width - 2][2] = BLANK;

		map[1][height - 2] = BLANK; map[2][height - 2] = BLANK; map[1][height - 3] = BLANK;
		map[width - 2][height - 2] = BLANK; map[width - 3][height - 2] = BLANK; map[width - 2][height - 3] = BLANK;
	}

	public void render(SpriteBatch batch) {
		
		final BitmapFont font;
		if (DEBUG) {
			font = game.getDebugFont();
			font.setColor(Color.WHITE);
		}

		// Local copies of the vars
		final int width = this.width; 
		final int height = this.height;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int block = map[x][y] & BLOCK_MASK;
				int fire  = map[x][y] & FIRE_MASK;
				float screenX = x * tile_width;
				float screenY = y * tile_height;

				batch.draw(tile_textures[block], screenX, screenY, tile_width, tile_height);
				
				if (fire > 0)
					batch.draw(fire_textures[fire], screenX, screenY, tile_width, tile_height);

				if (DEBUG)
					font.draw(batch, x + "," + y, screenX, screenY + tile_height);
			}
		}
	}
	
	/**
	 * Returns {x,y} of where this player should start
	 * @param player
	 * @return
	 */
	public int[] getPlayerStart(int player) {
		switch (player) {
			case 0: return new int[]{1,1};
			case 1: return new int[]{width - 2, height - 2};
			case 2: return new int[]{width - 2, 1};
			case 3: return new int[]{1, height - 2};
		}
		throw new IllegalArgumentException("Invalid player number " + player);
	}

	/**
	 * Gets screen X coords for this map square
	 * @param map_x
	 * @return
	 */
	public float getScreenX(int map_x) {
		return map_x * tile_width;
	}

	/**
	 * Gets screen Y coords for this map square
	 * @param map_y
	 * @return
	 */
	public float getScreenY(int map_y) {
		return map_y * tile_height;
	}
	
	public int getMapX(float screen_x) {
		return (int) (screen_x / tile_width);
	}

	public int getMapY(float screen_y) {
		return (int) (screen_y / tile_height);
	}

	public int getTile(int map_x, int map_y) {
		return map[map_x][map_y];
	}
	
	public float getTileWidth() {
		return tile_width;
	}
	
	public float getTileHeight() {
		return tile_height;
	}

	/**
	 * Set the squares in the bounds on fire
	 * @param origin_x
	 * @param origin_y
	 * @param min_x
	 * @param max_x
	 * @param min_y
	 * @param max_y
	 */
	public void setOnFire(int origin_x, int origin_y, int min_x, int max_x, int min_y, int max_y, boolean onFire) {
		if (onFire) {
			for (int x = min_x; x < max_x; x++)
				map[x][origin_y] |= ON_FIRE;

			for (int y = min_y; y < max_y; y++)
				map[origin_x][y] |= ON_FIRE;
		} else {
			for (int x = min_x; x < max_x; x++)
				map[x][origin_y] &= ~FIRE_MASK;

			for (int y = min_y; y < max_y; y++)
				map[origin_x][y] &= ~FIRE_MASK;			
		}
	}

	public boolean isOnFire(int map_x, int map_y) {
		return (map[map_x][map_y] & ON_FIRE) != ON_FIRE;
	}
	
	/**
	 * Is this square free to walk on?
	 * @param map_x
	 * @param map_y
	 * @return
	 */
	public boolean isFree(int map_x, int map_y) {
		//System.out.printf("%d %d %d\n", map_x, map_y, map[map_x][map_y]);
		return (map[map_x][map_y] & WALL) != WALL;
	}
}