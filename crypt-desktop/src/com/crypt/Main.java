package com.crypt;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Crypt Scroll Demo";
		cfg.useGL20 = true;
		cfg.width = 1000;
		cfg.height = 600;
		
		new LwjglApplication(new Crypt(), cfg);
	}
}
