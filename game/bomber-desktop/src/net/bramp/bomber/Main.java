package net.bramp.bomber;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Main {
	public static void main(String[] args) {
		/*
        Settings settings = new Settings();
        settings.maxWidth = 512;
        settings.maxHeight = 512;
        TexturePacker2.process(settings,  "../images", "../game-android/assets", "bomber");
		*/

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Bomber";
		cfg.useGL20 = false;
		cfg.width = 960;
		cfg.height = 832;

		new LwjglApplication(new BomberGame(), cfg);
	}
}
