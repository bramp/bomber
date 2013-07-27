package net.bramp.bomber.screens;

import java.util.ArrayDeque;
import java.util.Deque;

import net.bramp.bomber.Player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.google.common.base.Preconditions;

public class GameScreenInputProcessor implements InputProcessor {

	public final GameScreen game;
	public final Player[] players;

	// We make some assumptions, lets check they are correct
	// TODO remove warnings
	{
		Preconditions.checkArgument((Keys.UP - Keys.UP)    == Player.UP);
		Preconditions.checkArgument((Keys.DOWN - Keys.UP)  == Player.DOWN);
		Preconditions.checkArgument((Keys.LEFT - Keys.UP)  == Player.LEFT);
		Preconditions.checkArgument((Keys.RIGHT - Keys.UP) == Player.RIGHT);
	}

	public GameScreenInputProcessor(GameScreen game) {
		this.game = game;
		this.players = game.getPlayers();
	}

	// Holds which keys are currently down, and remembers which one was last
	// this enables us to ensure we are always moving in the right direction after
	// a complex sequence of key presses (left down, right down, right up - we should move left)
	Deque<Integer> directionStack = new ArrayDeque<Integer>();

	private void startMove(Player player, int keycode) {
		directionStack.add(keycode);
		int direction = keycode - Keys.UP;
		player.move(direction);
	}

	private void stopMove(Player player, int keycode) {
		directionStack.remove(keycode);

		Integer lastKeycode = directionStack.peekLast();
		if (lastKeycode == null) {
			player.move(Player.STOP);
		} else {
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