package net.bramp.bomber;

import net.bramp.bomber.screens.GameScreen;

import com.badlogic.gdx.ApplicationListener;

/**
 * 
 * @author bramp
 *
 */
public class BomberGame implements ApplicationListener {

	ApplicationListener screen = new GameScreen();

	public BomberGame() {
	}
	
	@Override
	public void create() {
		screen.create();
	}

	@Override
	public void resize(int width, int height) {
		screen.resize(width, height);
	}

	@Override
	public void render() {
		screen.render();
	}

	@Override
	public void pause() {
		screen.pause();
	}

	@Override
	public void resume() {
		screen.resume();
	}

	@Override
	public void dispose() {
		screen.dispose();
	}
}
