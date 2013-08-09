package net.bramp.bomber.screens;

import net.bramp.bomber.Direction;
import net.bramp.bomber.objects.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntArray;
import com.google.common.base.Preconditions;

public class GameScreenInputProcessor implements InputProcessor {

	public final GameScreen game;
	public final Player[] players;

	{
		assertSomeStuff();
	}
	
	/**
	 * We make some assumptions, lets check they are correct
	 */
	@SuppressWarnings("all")
	private static void assertSomeStuff() {
		Preconditions.checkArgument((Keys.UP - Keys.UP)    == Direction.UP);
		Preconditions.checkArgument((Keys.DOWN - Keys.UP)  == Direction.DOWN);
		Preconditions.checkArgument((Keys.LEFT - Keys.UP)  == Direction.LEFT);
		Preconditions.checkArgument((Keys.RIGHT - Keys.UP) == Direction.RIGHT);
	}

	public GameScreenInputProcessor(GameScreen game) {
		this.game = game;
		this.players = game.getPlayers();
	}

	// Holds which keys are currently down, and remembers which one was last
	// this enables us to ensure we are always moving in the right direction after
	// a complex sequence of key presses (left down, right down, right up - we should move left)
	IntArray directionStack = new IntArray(true, 4);

	private void startMove(Player player, int keycode) {
		directionStack.add(keycode);
		int direction = keycode - Keys.UP;
		player.move(direction);
	}

	private void stopMove(Player player, int keycode) {
		directionStack.removeValue(keycode);
		if (directionStack.size == 0) {
			player.move(Direction.STOP);
		} else {
			int lastKeycode = directionStack.peek();
			player.move(lastKeycode - Keys.UP);
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		Player player = players[0];
		if (keycode >= Keys.UP && keycode <= Keys.RIGHT) {
			startMove(player, keycode);
			return true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		Player player = players[0];
		if (keycode >= Keys.UP && keycode <= Keys.RIGHT) {
			stopMove(player, keycode);
			return true;
		}
		if (keycode == Keys.SPACE) {
			player.dropBomb();
		}
		
		if (keycode == Keys.ESCAPE)
			Gdx.app.exit();
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}
}