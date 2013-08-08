package net.bramp.bomber;

import net.bramp.bomber.events.BombExplodedEvent;
import net.bramp.bomber.screens.GameScreen;
import net.bramp.bomber.utils.events.EventBus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pools;

public final class Bomb extends MapObject implements AnimationInterface {

	public final Player owner;
	public final int flame_length;
	
	public final boolean dud = false;
	
	final AnimationComponent animation;

	public Bomb(GameScreen game, Player owner, int map_x, int map_y) {
		super(game.map);

		animation = new AnimationComponent(0.1f);

		this.owner = owner;
		this.flame_length = owner.flame_length; // We snap shot the flame (the moment it's dropped)

		// Setup textures
		TextureRegion frames[] = game.getTextureRepository().getBomb();
		animation.setFrames(this, frames);

		tile_margin_x = (map.getTileWidth()  - getWidth()) / 2;
		tile_margin_y = (map.getTileHeight() - getHeight()) / 2;

		setMapPosition(map_x, map_y);
	}

	@Override
	public void dispose() {}

	public void update (final float dt) {
		animation.update(this, dt);
	}

	@Override
	public void animationEnded() {
		BombExplodedEvent event = Pools.obtain(BombExplodedEvent.class);
		event.bomb = this;

		EventBus.getDefault().post(event);
	}

	@Override
	public void animationFrameEnded(int frame) {
		// TODO Auto-generated method stub	
	}
	
	public Player getOwner() {
		return owner;
	}

}
