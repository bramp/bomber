package net.bramp.bomber;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.base.Preconditions;

public final class Map {
	public static final byte BLANK = 0x0; // 0b0000
	public static final byte WALL  = 0x1; // 0b0001
	public static final byte BRICK = 0x2; // 0b0010
	public static final byte BLOCK_MASK = WALL | BRICK;

	public static final byte POWERUP_BOMB =  0x4; // 0b0100
	public static final byte POWERUP_FLAME = 0x8; // 0b1000
	public static final byte POWERUP_SPEED = 0xc; // 0b1100
	public static final byte POWERUP_MASK  = POWERUP_BOMB | POWERUP_FLAME | POWERUP_SPEED;

	static final double BRICK_PROB = 0.85;

	static final int block_width = 64;
	static final int block_height = 64;

	//
	final int width, height;
	final byte[][] map;
	
	public Map(int width, int height) {
		Preconditions.checkArgument( width >= 5 && (width - 3) % 2 == 0);
		Preconditions.checkArgument( height >= 5 && (height - 3) % 2 == 0);
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

	public void render(SpriteBatch batch, TextureRegion[] map_textures) {
		// Local copies of the vars
		final int width = this.width; 
		final int height = this.height;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int block = map[x][y] & BLOCK_MASK;
				batch.draw(map_textures[block], x * block_width, y * block_height, block_width, block_height);
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
		return map_x * block_width;
	}

	/**
	 * Gets screen Y coords for this map square
	 * @param map_y
	 * @return
	 */
	public float getScreenY(int map_y) {
		return map_y * block_height;
	}
	
	/**
	 * Is this square free to walk on?
	 * @param map_x
	 * @param map_y
	 * @return
	 */
	public boolean isFree(int map_x, int map_y) {
		return (map[map_x][map_y] & BLOCK_MASK) == BLANK;
	}
}