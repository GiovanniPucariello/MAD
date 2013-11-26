package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WorldController implements InputProcessor
{
	// Screen viewPort
	private Rectangle viewPort = new Rectangle(0,0,0,0);
	
	// world objects
	public Background background;
	private LevelMap levelMap;
	private Assets assets;
	public Character character;
	public TreasureRegister treasureSites;
	private WorldRenderer renderer;
	
	public WorldController() 
	{
		// Create resources
		assets = new Assets();
		// Instantiate LevelMap
		levelMap = new LevelMap();
		// Instantiate background 
		background = new Background(levelMap);
		// Instantiate character
		character = new Character(this, levelMap, assets.getCharAnim());
		// instantiate TreasureRegister
		treasureSites = new TreasureRegister(levelMap, assets.getTreasureImages());
		// Setup input detection to this class
		Gdx.input.setInputProcessor(this);	
	}
	
	public void init() 
	{
		// initial objects for this level
		character.init();
		treasureSites.init();
	}
	
	public void update (float deltaTime) 
	{
		// check for Accelerometer
		handleAccelerometer();
		
		// call all world objects to update themselves
		character.update(deltaTime);
		
		// update the screen area to be rendered
		viewPort = renderer.updateViewport();
		
		// update all other objects below here
		treasureSites.collectTresasure(character.getCharacterBounds());
	}

	private void handleAccelerometer() {
		if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer))
		{
			float adjustedMovement = (Gdx.input.getAccelerometerY());
			//Gdx.app.debug("Crypt", "AccelerometerY : " + adjustedMovement);
			if (adjustedMovement < -1.0f)
			{
				character.moveLeft();
			}
			else if (adjustedMovement > 0.2f)
			{
				character.moveRight();
			}
			else 
			{
				character.stopHoziontialMove();
			}
			
			adjustedMovement = Gdx.input.getAccelerometerX();
			if (adjustedMovement < 0.1f)
			{
				character.moveUp();
			}
			else if (adjustedMovement > 3.0f)
			{
				character.moveDown();
			} 
			else 
			{
				character.stopVerticalMove();
			}
		}
	}
	
	/**
	 * 
	 * @param x bottom left of character position
	 * @param y bottom left of character position
	 */
	public void addbullet(float x , float y)
	{
		// Call bulletRegister class from here and add bullet
		if (x > Gdx.graphics.getWidth() /2)
		{
				System.out.println("firing right");
		} 
		else
		{
				System.out.println("firing left");
		}
	}
	
	public Vector2 getCharacterPosition()
	{
		// get characters position
		return character.getCharacterPosition();
	}
	
	public LevelMap getLevelMap()
	{
		return levelMap;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode)
		{
		case Keys.RIGHT:
			character.moveRight();
			break;
		case Keys.LEFT:
			character.moveLeft();
			break;
		case Keys.UP:
			character.moveUp();
			break;
		case Keys.DOWN:
			character.moveDown();
			break;
		case Keys.C:
			levelMap.setLevel(0);
			character.init();
			break;
		case Keys.D:
			levelMap.setLevel(1);
			character.init();
			break;	
		case Keys.S:
			levelMap.setLevel(2);
			character.init();
			break;
		case Keys.X:
			addbullet(Gdx.graphics.getWidth() /2 +100, 0);
			break;
		case Keys.Z:
			addbullet(Gdx.graphics.getWidth() /2 -100, 0);
			break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode)
		{
		case Keys.RIGHT:
			character.stopHoziontialMove();
			break;
		case Keys.LEFT:
			character.stopHoziontialMove();
			break;
		case Keys.UP:
			character.stopVerticalMove();
			break;
		case Keys.DOWN:
			character.stopVerticalMove();
			break;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{
		addbullet(screenX, screenY);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public void setRender(WorldRenderer renderer) {
		this.renderer = renderer;
		
	}
}
