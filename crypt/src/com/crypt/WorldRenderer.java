package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController world;
	private LevelMap levelMap;
	
	// current Screen size
	private float screenWidth;
	private float screenHeight;
	
	// Screen viewPort
	private Rectangle viewPort = new Rectangle(0,0,0,0);
	private Rectangle backgroundPort = new Rectangle(0,0,0,0);
		
	public WorldRenderer (WorldController world) 
	{
		// Store reference to World class and LevelMap
		this.world = world;
		this.levelMap = world.getLevelMap();
		
		// set to the camera view of the game
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
						
		// Setup a spritebatch for rendering 
		batch = new SpriteBatch();
	}
	
	private void init()
	{
		
	}
	
	public void render()
	{
		// Clear Screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				
		// tell the camera to update its matrices.
	    camera.update();
		
	    // begin batch
		batch.begin();
			
			/*// update screen position based on the character
			updateViewport();*/
			// Update camera position view position 
			camera.position.set(viewPort.x + (viewPort.width / 2), viewPort.y + (viewPort.height / 2), 0);
			camera.update();
			
			// tell the SpriteBatch to render in the
		    // coordinate system specified by the camera.
		    batch.setProjectionMatrix(camera.combined);
			
			// disabling blending with the background give a speed advantage	
			batch.disableBlending();
			
			// draw background
			world.background.draw(batch, backgroundPort);
			
			// enable blending for other objects in the world
			batch.enableBlending();
			
			// ****** render all other objects here and below ******
			world.character.draw(batch);
			
		batch.end();
	}
	
	public void resize(int width, int height)
	{						
		// assuming that the height will be the set dimension of the constant
		// calculate the proportion width of the screen
		float proportion = (Constant.SCREEN_HEIGHT / height);
		screenWidth = width * proportion;
		screenHeight = Constant.SCREEN_HEIGHT;
		//stage.setViewport(screenWidth, screenHeight, false);
		camera.setToOrtho(false, screenWidth, screenHeight);
		updateViewport();
	}
	
	@Override public void dispose()
	{
		batch.dispose();
	}
	
	public float getscreenWidth()
	{
		return screenWidth;
	}
	
	public float getscreenHeight()
	{
		return screenHeight;
	}
	
	// Calculates the ViewPort size and position based upon the characters position
	public Rectangle updateViewport()
	{
		// calculate bottom corner of viewport based upon the characters position
		// Characters position - half the screen size + half a block because the characters position
		// is the both left hand corner of the character 
		float renderXPos = world.getCharacterPosition().x - (screenWidth / 2) + (Constant.BLOCK_SIZE / 2);
		//System.out.println("renderXPos :" + renderXPos);
		float renderYPos = world.getCharacterPosition().y - (Constant.SCREEN_HEIGHT / 2) + (Constant.BLOCK_SIZE /2);
		
		// Check if render origin is off screen 
		if (renderYPos < 0) 
		{
			// reset it to the edge of the screen
			renderYPos = 0;
		} else if (renderYPos + screenHeight > levelMap.getMapHeightPixels())
		{
			// reset the view to the far edge of the map
			renderYPos = levelMap.getMapHeightPixels() - screenHeight;
		}
		
		// Check if the map is smaller than the width of the viewport
		if (screenWidth > levelMap.getMapLengthPixels())
		{
			// Store new ViewPort
			viewPort.x = -(screenWidth - levelMap.getMapLengthPixels()) / 2;
			viewPort.y = renderYPos;
			viewPort.width = screenWidth;
			viewPort.height = screenHeight;
			
			// Update backgroundPort 
			backgroundPort.y = viewPort.y;
			backgroundPort.height = screenHeight;
			
			// centre backgroundPort within Viewport
			backgroundPort.x = viewPort.x + ((screenWidth - levelMap.getMapLengthPixels())/2);
			backgroundPort.width = levelMap.getMapLengthPixels();
		} 
		else
		{
			// Check if render origin is off screen 
			if (renderXPos < 0)
			{
				// reset it to the edge of the screen
				renderXPos = 0;
			} else if (renderXPos + screenWidth > levelMap.getMapLengthPixels()) // check if it is beyond the map length
			{
				// reset the view to the far edge of the map
				renderXPos = levelMap.getMapLengthPixels() - screenWidth;
			}
			
			// Store new ViewPort
			viewPort.x = renderXPos;
			viewPort.y = renderYPos;
			viewPort.width = screenWidth;
			viewPort.height = screenHeight;
			
			// Update backgroundPort 
			backgroundPort.y = viewPort.y;
			backgroundPort.height = screenHeight;
			backgroundPort.x = viewPort.x;
			backgroundPort.width = screenWidth;			
		}
		
		return viewPort;
	}
}
