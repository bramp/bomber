package net.bramp.bomber;

import net.bramp.bomber.events.BombExplodedEvent;
import net.bramp.bomber.screens.GameScreen;
import net.bramp.bomber.utils.events.EventBus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pools;

public final class Bomb extends MapObject implements AnimationInterface {

	final GameScreen game;

	final Player owner;
	final int flame_length;
	
	final AnimationComponent animation;

	public Bomb(GameScreen game, Player owner, int map_x, int map_y) {
		super(game.getMap());

		animation = new AnimationComponent(1.0f);

		this.game = game;
		this.owner = owner;

		this.flame_length = owner.flame_length;

		// Setup textures
		TextureRegion frames[] = game.getTextureRepository().getBomb();
		animation.setFrames(this, frames);

		tile_margin_x = (map.getTileWidth()  - getWidth()) / 2;
		tile_margin_y = (map.getTileHeight() - getHeight()) / 2;

		setMapPosition(map_x, map_y);
	}

	public void update (final float dt) {
		animation.update(this, dt);
	}

	@Override
	public void animationEnded() {
		BombExplodedEvent event = Pools.obtain(BombExplodedEvent.class);
		event.bomb = this;

		EventBus.getDefault().post(event);

		game.removeSprite(this);
	}

	@Override
	public void animationFrameEnded(int frame) {
		// TODO Auto-generated method stub	
	}
	
	public Player getOwner() {
		return owner;
	}

}
