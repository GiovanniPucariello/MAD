package com.crypt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Crypt implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Background background;
	private Character character;
	public static float screenLength;
	
	@Override
	public void create() {		
		Texture.setEnforcePotImages(false);
		
		// get the physical size of the screen
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		// assuming that the height will be the set dimension of the constant
		// calculate the proportion width of the screen
		float proportion = (Constant.SCREEN_HEIGHT /height);
		screenLength = width * proportion;
		
		// set to the camera view of the game
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenLength, Constant.SCREEN_HEIGHT);
		
		// Setup a spritebatch for rendering 
		batch = new SpriteBatch();
		
		// Instantiate background 
		background = new Background(screenLength, Constant.SCREEN_HEIGHT);
		
		// Instantiate character 
		character = new Character(background);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		// tell the camera to update its matrices.
	    camera.update();
	      
	    // tell the SpriteBatch to render in the
	    // coordinate system specified by the camera.
	    batch.setProjectionMatrix(camera.combined);
	    handleInput();
	    // begin batch
		batch.begin();
			//Update character movement
			character.move();
			// draw background
			background.draw(batch);
			// System.out.println("Frame per second :"+ Gdx.graphics.getFramesPerSecond());
		batch.end();
	}

	private void handleInput() {
		if (Gdx.input.isTouched())
		{
			if (Gdx.input.getX() > Gdx.graphics.getWidth()/2)
			{
				character.moveRight();
				
			} else
			{
				character.moveLeft();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
