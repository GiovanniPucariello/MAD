package com.crypt;

import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
	public SpawnSiteReg spawnSiteReg;
	
	// current level
	private int currentlevel = 1;
	
	private boolean charHit = false;
	
	private float timer = 0f;
	
	// unprojected screen touch position
	private Vector3 touchPoint = new Vector3();
	private Vector2 direction = new Vector2();
	
	public WorldController() 
	{
		// load game options
		Constant.loadGameOptions();
		// Create resources
		assets = new Assets();
		// Instantiate LevelMap
		levelMap = new LevelMap(this);
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
		monsterRegister = new MonsterRegister(this, levelMap, assets.getMummyAnim(), assets.getBatAnim(), assets.getSnakeAnim(), assets.getGhostAnim());
		bulletreg = new BulletRegister(this, levelMap, monsterRegister, assets.getBulletAnim());
		// Instantiate DoorRegister
		doorSites = new DoorRegister(levelMap, assets.getDoorClosed(), assets.getOpeningDoor());
		levelMap.setDoorRegister(doorSites);
		// Instantiate spawnSites
		spawnSiteReg = new SpawnSiteReg(levelMap, assets.getSpawnSiteClouds(), monsterRegister);
		// Setup input detection to this class
		Gdx.input.setInputProcessor(this);
		currentlevel = Constant.STARTING_LEVEL;
		levelMap.setLevel(currentlevel);
	}
	
	public void init() 
	{
		// initial objects for this level
		character.init();
		treasureSites.init();
		keyRegister.init();
		doorSites.init();
		spawnSiteReg.init();
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
			
			spawnSiteReg.update(viewPort, deltaTime, character.getCharacterBounds());
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
					spawnSiteReg.init();
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
					savelevelsAttained();
					timer = 0;
					levelMap.setLevel(++currentlevel);
					character.init();
					treasureSites.init();
					keyRegister.init();
					doorSites.init();	
					monsterRegister.init();
					bulletreg.init();
					spawnSiteReg.init();
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
		if (Constant.CHAR_CONTROL == Constant.TILT) {
			if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)) {
				float adjustedMovement = (Gdx.input.getAccelerometerY());
				//Gdx.app.debug("Crypt", "AccelerometerY : " + adjustedMovement);
				if (adjustedMovement < -0.5f) {
					character.moveLeft();
				} else if (adjustedMovement > 0.5f) {
					character.moveRight();
				} else {
					character.stopHoziontialMove();
				}

				adjustedMovement = Gdx.input.getAccelerometerX();
				if (adjustedMovement < -0.3f) {
					character.moveUp();
				} else if (adjustedMovement > 2.3f) {
					character.moveDown();
				} else {
					character.stopVerticalMove();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param x bottom left of character position
	 * @param y bottom left of character position
	 */
	public void addbullet(Fire fire)
	{
		// Call bulletRegister class from here and add bullet
		Vector2 bulletStart = getCharacterPosition();
		Vector2 bulletDirection = new Vector2(0,0);

		if (fire == Fire.right)
		{
				bulletDirection.x = 1;
				bulletreg.add(bulletDirection,bulletStart);
		} else if (fire == Fire.left)
		{
				bulletDirection.x = -1;
				bulletreg.add(bulletDirection,bulletStart);

		} else if (fire == Fire.down)
		{
			bulletDirection.y = -1;
			bulletreg.add(bulletDirection,bulletStart);
		} else if (fire == Fire.up)
		{
			bulletDirection.y = 1;
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
	
	public Animation[] getGhostAnim()
	{
		return assets.getGhostAnim();
	}
	
	public Rectangle getCharacterBounds()
	{
		return character.getCharacterBounds();
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
		case Keys.D:
			addbullet(Fire.right);
			break;
		case Keys.A:
			addbullet(Fire.left);
			break;
		case Keys.S:
			addbullet(Fire.down);
			break;
		case Keys.W:
			addbullet(Fire.up);
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
		// check if the touch is on the joystick pad or a fire touch
		touchPoint = renderer.unprojected(screenX, screenY);
		if (Constant.CHAR_CONTROL == Constant.JOYSTICK && renderer.touchPad.contains(touchPoint.x, touchPoint.y)) {
			touchpadMovement(touchPoint);
			
		} else {
			if (renderer.fireButtonUp.contains(touchPoint.x, touchPoint.y)) addbullet(Fire.up);
			if (renderer.fireButtonDown.contains(touchPoint.x, touchPoint.y)) addbullet(Fire.down);
			if (renderer.fireButtonLeft.contains(touchPoint.x, touchPoint.y)) addbullet(Fire.left);
			if (renderer.fireButtonRight.contains(touchPoint.x, touchPoint.y)) addbullet(Fire.right);
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (Constant.CHAR_CONTROL == Constant.JOYSTICK) {
			// check if the touch was on the joystick pad
			touchPoint = renderer.unprojected(screenX, screenY);
			if (renderer.touchPadLimit.contains(touchPoint.x, touchPoint.y)){
				// stop all movement
				character.stopHoziontialMove();
				character.stopHoziontialMove();
				character.stopVerticalMove();
				character.stopVerticalMove();
				
				// reset the touchpad position and last drag position
				renderer.touchPadCentre = new Vector2(0,0);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (Constant.CHAR_CONTROL == Constant.JOYSTICK) {
			// check if the touch is on the joystick pad
			touchPoint = renderer.unprojected(screenX, screenY);
			
			if (renderer.touchPad.contains(touchPoint.x, touchPoint.y)) {
				touchpadMovement(touchPoint);
			} else if (renderer.touchPadLimit.contains(touchPoint.x, touchPoint.y)) {
				// finger has dragged slightly outside the joystick but within the limit
				// angle of drag
				float adj = touchPoint.x - renderer.touchPad.x;
				float opp = touchPoint.y - renderer.touchPad.y;
				
				// calculate existing angle and correct it				
				double dAngle = Math.toDegrees(Math.atan2(opp, adj));
				if(dAngle < 0) dAngle += 360;
				
				// flip the angle to the opposite direction 
				if (dAngle - 180 < 0) dAngle += 180; else dAngle -= 180;
				
				// calculate the new centre position for the pad
				renderer.touchPadCentre.x += adj + (renderer.touchPad.radius * Math.cos(Math.toRadians(dAngle)));
				renderer.touchPadCentre.y += opp + (renderer.touchPad.radius * Math.sin(Math.toRadians(dAngle)));
				
				touchpadMovement(touchPoint);
			}
			return true;
		}
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
	
	private void touchpadMovement(Vector3 touchPoint)
	{
		direction = renderer.joystickMovement(touchPoint.x, touchPoint.y);
		
		// horizontal movement
		if (direction.x < -renderer.touchPad.radius / 7) {
			character.moveRight();
		} else if (direction.x > renderer.touchPad.radius / 7) {
			character.moveLeft();
		} else {
			character.stopHoziontialMove();
		}

		// vertical movement
		if (direction.y < -renderer.touchPad.radius / 7) {
			character.moveUp();
		} else if (direction.y > renderer.touchPad.radius / 7) {
			character.moveDown();
		} else {
			character.stopVerticalMove();
		}	
	}
	
	private void savelevelsAttained()
	{
		boolean[] levels = new boolean[20];
		FileHandle file = Gdx.files.local("LevelCompleted.csv");
		if (Gdx.files.local("LevelCompleted.csv").exists()) {
			String line = file.readString();
			StringTokenizer token = new StringTokenizer(line, ",");
			
			int i = 0;
			while(token.hasMoreTokens() && i < 50)
			{
				levels[i] = Boolean.valueOf(token.nextToken());
				i++;
			}
									
			if(currentlevel < i && levels[currentlevel+1] == false) {
				updateLevelsCompleted();
			}
		}
		else {
			updateLevelsCompleted();
		}
	}
	
	private void updateLevelsCompleted()
	{
		Gdx.files.local("LevelCompleted.csv").delete();		 
		FileHandle file = Gdx.files.local("LevelCompleted.csv");
		String line = "";
		for(int i = 0; i < currentlevel+2; i++) {
			line += Boolean.toString(true) + ",";
		}
		for(int i = (currentlevel + 2); i < levelMap.getNumberLevels()+1; i++) {
			line += Boolean.toString(false) + ",";
		}
		file.writeString(line, false);
	}
}
