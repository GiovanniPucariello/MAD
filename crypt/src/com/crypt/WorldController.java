package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

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
	public KeyRegister keyRegister;
	public BulletRegister bulletreg;
	public DoorRegister doorSites;
	private WorldRenderer renderer;
	public MonsterRegister monsterRegister;
	
	// current level
	private int currentlevel = 1;
	
	private boolean charHit = false;
	
	private float timer = 0f;
	
	public WorldController() 
	{
		// Create resources
		assets = new Assets();
		// Instantiate LevelMap
		levelMap = new LevelMap();
		// Instantiate background 
		background = new Background(levelMap);
		// Instantiate character
		character = new Character(this, levelMap, assets.getCharAnim(), assets.getCharTeleport());
		// instantiate KeyRegister
		keyRegister = new KeyRegister(levelMap, assets.getKeyImages());
		// instantiate TreasureRegister
		treasureSites = new TreasureRegister(levelMap, assets.getTreasureImages());
		// make a bullet Register
		// Instantiate MonsterRegister
		monsterRegister = new MonsterRegister(this, levelMap, assets.getMummyAnim());
		bulletreg = new BulletRegister(this, levelMap, monsterRegister, assets.getBulletAnim());
		// Instantiate DoorRegister
		doorSites = new DoorRegister(levelMap, assets.getDoorClosed(), assets.getOpeningDoor());
		levelMap.setDoorRegister(doorSites);
		// Setup input detection to this class
		Gdx.input.setInputProcessor(this);	
	}
	
	public void init() 
	{
		// initial objects for this level
		character.init();
		treasureSites.init();
		keyRegister.init();
		doorSites.init();
		charHit = false;
	}
	
	public void update (float deltaTime) 
	{
		if (character.isLevelfinished() == false && charHit == false) 
		{
			// check for Accelerometer
			handleAccelerometer();
			
			// call all world objects to update themselves
			character.update(deltaTime);
			
			monsterRegister.update(deltaTime, viewPort);
			
			bulletreg.update(deltaTime, viewPort);
			
			// update the screen area to be rendered
			viewPort = renderer.updateViewport();
			
			// update all other objects below here
			
			treasureSites.collectTresasure(character.getCharacterBounds());
			
			Array<Key> collected  = keyRegister.collectKey(character.getCharacterBounds());
			character.addkeys(collected);
			
			doorSites.pleaseOpenDoor(character.getCharacterBounds(), character.getKeys());
		}
		else
		{
			// check if character caught
			if (charHit == true)
			{
				// update character to die
				character.update(deltaTime);
				if (character.isDead())
				{
					// **************************** need to decrement lives here
					// reset character
					charHit = false;
					character.init();
					// reset monsters and bullets
					monsterRegister.init();
					bulletreg.init();
					
				}
			}
			// character finished the level
			else
			{
				// three second delay
				if (timer < 3)
				{
					timer += Gdx.graphics.getDeltaTime();
				}
				else
				{
					// change level
					timer = 0;
					levelMap.setLevel(currentlevel++);
					character.init();
					treasureSites.init();
					keyRegister.init();
					doorSites.init();		
				}				
			}
		}
	}
	
	public void characterCaught()
	{
		charHit = true;
		character.isHit();
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
		int centerx = Gdx.graphics.getWidth() /2;
		int centery = Gdx.graphics.getHeight() /2;
		x = x-centerx;
		y = y-centery;
		// Call bulletRegister class from here and add bullet
		Vector2 bulletStart = getCharacterPosition();
		Vector2 bulletDirection = new Vector2(0,0);

		if (x> Math.abs(y))
		{
				bulletDirection.x = 1;
				bulletreg.add(bulletDirection,bulletStart);
		} 
		if ((-x)> Math.abs(y))
		{
				bulletDirection.x = -1;
				bulletreg.add(bulletDirection,bulletStart);

		}
		if (y> Math.abs(x))
		{
			bulletDirection.y = -1;
			bulletreg.add(bulletDirection,bulletStart);
		}
		if ((-y)> Math.abs(x))
		{
			bulletDirection.y = 1;
			bulletreg.add(bulletDirection,bulletStart);
		}
		if(y==x)
		{
			bulletDirection.x = 1;
			bulletDirection.y = 0;
			bulletreg.add(bulletDirection,bulletStart);
			bulletDirection.x = -1;
			bulletDirection.y = 0;
			bulletreg.add(bulletDirection,bulletStart);
			bulletDirection.x = 0;
			bulletDirection.y = 1;
			bulletreg.add(bulletDirection,bulletStart);
			bulletDirection.x = 0;
			bulletDirection.y = -1;
			bulletreg.add(bulletDirection,bulletStart);
		}

	}
	
	public Vector2 getCharacterPosition()
	{
		// get characters position
		return character.getCharacterPosition();
	}
	
	public Rectangle getCharacterCollisionBounds()
	{
		return character.getCollisionBounds();
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
