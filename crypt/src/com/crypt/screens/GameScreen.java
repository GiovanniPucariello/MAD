package com.crypt.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.crypt.Crypt;
import com.crypt.WorldController;
import com.crypt.WorldRenderer;

public class GameScreen implements Screen{

	private Crypt game;
	private WorldController world;
	private WorldRenderer worldRenderer;
	
	private boolean pause;
	
	public GameScreen(Crypt game) 
	{
		this.game = game;
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Initialise controller and renderer
		world = new WorldController();
		worldRenderer = new WorldRenderer(world);
		
	}
	
	@Override
	public void render(float delta) {
		// only update the game if not paused
		if (!pause)
		{
			// Update game world by the time that has passed since last rendered frame.
			world.update(Gdx.graphics.getDeltaTime());
			worldRenderer.render();	
		}		
	}

	@Override
	public void resize(int width, int height) 
	{
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() 
	{
		pause = true;
	}

	@Override
	public void resume() 
	{
		pause = false;
	}

	@Override
	public void dispose() {
		worldRenderer.dispose();
	}
}
