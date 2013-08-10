package net.bramp.bomber.objects;

import net.bramp.bomber.AnimationInterface;
import net.bramp.bomber.components.AnimationComponent;
import net.bramp.bomber.events.BombEvent;
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

		animation = new AnimationComponent(this, 0.1f);
		animation.setListener(this);

		this.owner = owner;
		this.flame_length = owner.flame_length; // We snap shot the flame (the moment it's dropped)

		// Setup textures
		TextureRegion frames[] = game.getTextureRepository().getBomb();
		animation.setFrames(frames);

		tile_margin_x = (map.getTileWidth()  - getWidth()) / 2;
		tile_margin_y = (map.getTileHeight() - getHeight()) / 2;

		setMapPosition(map_x, map_y);
	}

	@Override
	public void dispose() {}

	public void update (final float dt) {
		animation.update(dt);
	}

	@Override
	public void animationEnded() {
		BombEvent event = Pools.obtain(BombEvent.class);
		event.type = this.dud ? BombEvent.FAILED : BombEvent.EXPLODED;
		event.bomb = this;

		EventBus.getDefault().post(event);
	}

	@Override
	public void animationFrameEnded(int frame) {
		//nothing
	}
}
