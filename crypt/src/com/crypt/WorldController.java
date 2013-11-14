package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class WorldController implements InputProcessor
{
	// world objects
	public Background background;
	private LevelMap levelMap;
	private Character character;
	
	public WorldController() 
	{
		// Instantiate LevelMap
		levelMap = new LevelMap();
		// Instantiate background 
		background = new Background(levelMap);
		// Instantiate character
		character = new Character(this, levelMap);
		// Setup input detection to this class
		Gdx.input.setInputProcessor(this);	
	}
	
	private void init() 
	{
		
	}
	
	public void update (float deltaTime) 
	{
		// call all world objects to update themselves
		character.update(deltaTime);
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
			break;
		case Keys.D:
			levelMap.setLevel(1);
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
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
}
