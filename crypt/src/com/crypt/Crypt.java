package com.crypt;

import com.badlogic.gdx.Game;

public class Crypt extends Game {
	public static final String VERSION = "1.0";

	@Override
	public void create() 
	{		
		// Start Game Screen
		//setScreen(new GameScreen(this));
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() 
	{
		super.pause();
	}

	@Override
	public void resume() 
	{
		super.resume();
	}
}
