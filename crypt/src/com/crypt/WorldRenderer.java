package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
	public Rectangle viewPort = new Rectangle(0,0,0,0);
	private Rectangle backgroundPort = new Rectangle(0,0,0,0);
	
	// time lapse
	float deltatime;
	
	// Joystick image
	private TextureRegion joystick;
	private float joystickSize = 192;
	
	// Joystick Area
	public Circle touchPad = new Circle();
	public Circle touchPadLimit = new Circle();
	private Vector2 joystickMovement = new Vector2();
	public Vector2 touchPadCentre = new Vector2(0,0);
	
	// firebuttons
	private TextureRegion fireButtons;
	private TextureRegion fireButtonsSolid;
	public float fireButtonSize = 384;
	private float firebuttonX = 0;
	private float firebuttonY = 0;
	public Rectangle fireButtonUp = new Rectangle(0,0,0,0);
	public Rectangle fireButtonLeft = new Rectangle(0,0,0,0);
	public Rectangle fireButtonRight = new Rectangle(0,0,0,0);
	public Rectangle fireButtonDown = new Rectangle(0,0,0,0);
	
	//Pause Button Image
		private TextureRegion pauseButton;
		public TextureRegion resumeButton;
		public TextureRegion resumeUp;
		public TextureRegion resumeDown;
		public TextureRegion quitButton;
		public TextureRegion quitUp;
		public TextureRegion quitDown;
		
		
		
		private TextureRegion endButton;
		private float buttonWidth = 200;
		private float buttonHeight = 64;
		
		//Pause Area
		public Rectangle pauseButtonArea = new Rectangle();
		public Rectangle resumeButtonArea = new Rectangle();
		public Rectangle quitButtonArea = new Rectangle();
		public Rectangle endButtonArea = new Rectangle();
		
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
		
		// setup Joystick ready for possible use
		joystick = new TextureRegion(new Texture(Gdx.files.internal("data/TouchPad.png")));
		
		fireButtons = new TextureRegion(new Texture(Gdx.files.internal("data/FireButtons.png")));
		fireButtonsSolid = new TextureRegion(new Texture(Gdx.files.internal("data/FireButtonsSolid.png")));
		
		//setup Pause Button Texture
		pauseButton = new TextureRegion(new Texture(Gdx.files.internal("data/PauseButton.png")));
		resumeButton = new TextureRegion(new Texture(Gdx.files.internal("data/ResumeUp.png")));
		resumeUp = new TextureRegion(new Texture(Gdx.files.internal("data/ResumeUp.png")));
		resumeDown = new TextureRegion(new Texture(Gdx.files.internal("data/ResumeDown.png")));
		
		quitButton = new TextureRegion(new Texture(Gdx.files.internal("data/QuitUp.png")));
		quitUp = new TextureRegion(new Texture(Gdx.files.internal("data/QuitUp.png")));
		quitDown = new TextureRegion(new Texture(Gdx.files.internal("data/QuitDown.png")));
		
		endButton = new  TextureRegion(new Texture(Gdx.files.internal("data/quitButton.png")));
		
	}
	
	public void render()
	{
		// Clear Screen
		switch (Constant.PASSAGEWAY_COLOUR)
		{
			case 0:
			{
				Gdx.gl.glClearColor(0, 0, 0, 1);
				break;
			}
			case 1:
			{
				Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
				break;
			}
			case 2:
			{
				Gdx.gl.glClearColor(1, 0.78f, 0, 1);
				break;
			}
			case 3:
			{
				Gdx.gl.glClearColor(1, 1, 0, 1);
				break;
			}
			case 4:
			{
				Gdx.gl.glClearColor(0, 1, 1, 1);
				break;
			}
		}
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				
		// tell the camera to update its matrices.
	    camera.update();
		
	    // begin batch
		batch.begin();
			
			// update screen position based on the character
			updateViewport();
			// Update camera position view position 
			camera.position.set(viewPort.x + (viewPort.width / 2), viewPort.y + (viewPort.height / 2), 0);
			camera.update();
			
			// tell the SpriteBatch to render in the
		    // coordinate system specified by the camera.
		    batch.setProjectionMatrix(camera.combined);
			
			// disabling blending with the background give a speed advantage	
			//batch.disableBlending();
			
			// draw background
			world.background.draw(batch, backgroundPort);
			
			// enable blending for other objects in the world
			//batch.enableBlending();
			
			// ****** render all other objects here and below ******
			deltatime = Gdx.graphics.getDeltaTime();
			world.character.draw(batch);
			world.treasureSites.draw(batch);
			world.keyRegister.draw(batch);
			world.doorSites.draw(batch, deltatime);
			world.monsterRegister.draw(batch);
			world.bulletreg.draw(batch);
			world.spawnSiteReg.draw(batch, deltatime);
			
			// draw pause button
			batch.draw(pauseButton, pauseButtonArea.x, pauseButtonArea.y, pauseButtonArea.width, pauseButtonArea.height);
			
			// draw fire buttons
			batch.draw(fireButtons, firebuttonX, firebuttonY, fireButtonSize, fireButtonSize);
			
			//******* render touch pad last if turned on *************
			if (Constant.CHAR_CONTROL == Constant.JOYSTICK)
			{
				batch.draw(joystick, touchPad.x-touchPad.radius, touchPad.y-touchPad.radius, joystickSize, joystickSize);
			}
			
			world.statusBar.draw(batch, viewPort);		//draws information bar and relevant info in game.
			if(world.paused == true)
			{
				Color c = batch.getColor();
				batch.setColor(c.r, c.g, c.b, 1);
				
				if(world.levelWon == true)
				{
					world.summaryMenu.draw(batch, viewPort, world.levelWon);
				}
				else if (world.end == true){
					world.summaryMenu.draw(batch, viewPort, world.levelWon);
					if(world.summaryMenu.fireButtons == true) batch.draw(fireButtonsSolid, firebuttonX, firebuttonY, fireButtonSize, fireButtonSize);
				}
				else {
					//render Pause Menu and options.
					
					world.pauseMenu.draw(batch, viewPort);
					batch.draw(resumeButton, resumeButtonArea.x, resumeButtonArea.y, resumeButtonArea.width, resumeButtonArea.height);
					batch.draw(quitButton, quitButtonArea.x, quitButtonArea.y, quitButtonArea.width, quitButtonArea.height);
				}
				
				batch.setColor(c.r, c.g, c.b, 0.5f);
			}
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
		
		// Adjust the joystick size
		joystickSize = ((float)Constant.NUM_ROWS / 12) * 192;
		touchPad = new Circle(joystickSize / 2, viewPort.y + joystickSize / 2, joystickSize/2);
		touchPadLimit = new Circle(joystickSize / 2, viewPort.y + joystickSize / 2, joystickSize/2+joystickSize/6);
		
		float scale = ((float)Constant.NUM_ROWS / 12);
		
		pauseButtonArea = new Rectangle(viewPort.width - (pauseButton.getRegionWidth()/2*scale), viewPort.y - (pauseButton.getRegionHeight()/2*scale),pauseButton.getRegionWidth()*scale, pauseButton.getRegionHeight()*scale);
		
		resumeButtonArea = new Rectangle(0, 0, resumeButton.getRegionWidth() * scale, resumeButton.getRegionHeight() * scale);
		quitButtonArea = new Rectangle(0, 0, quitButton.getRegionWidth() * scale, quitButton.getRegionHeight() * scale);
		
		endButtonArea = new Rectangle((viewPort.width/2) - (buttonWidth/2), viewPort.y + buttonHeight + buttonHeight,buttonWidth, buttonHeight);
	

		// Adjust the fire buttons image
		fireButtonSize = scale * 384;
		// Adjust the fire buttons size
		fireButtonUp.width = scale * 185;
		fireButtonUp.height = scale * 120;
		fireButtonLeft.width = scale * 185;
		fireButtonLeft.height = scale * 120;
		fireButtonRight.width = scale * 185;
		fireButtonRight.height = scale * 120;
		fireButtonDown.width = scale * 185;
		fireButtonDown.height = scale * 120;
		
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
		
		// update the position of the touchpad
		touchPad.y = viewPort.y + joystickSize / 2 + touchPadCentre.y;
		touchPad.x = viewPort.x + joystickSize / 2 + touchPadCentre.x;
		touchPadLimit.x = touchPad.x;
		touchPadLimit.y = touchPad.y;
		
		float scale = viewPort.height / Constant.SCREEN_HEIGHT;
		
		// update the position of the pauseButton
		pauseButtonArea.y = viewPort.y + viewPort.height - ((pauseButtonArea.height * 1.25f) *scale);
		pauseButtonArea.x = viewPort.x + viewPort.width - ((pauseButtonArea.width * 1.25f) *scale);
		
		resumeButtonArea.y = viewPort.y + (viewPort.height /2) - (resumeButtonArea.height/2 * scale);
		resumeButtonArea.x = viewPort.x + (viewPort.width /2) - ((resumeButtonArea.width + 10) * scale);
		
		quitButtonArea.y = resumeButtonArea.y; 
		quitButtonArea.x = viewPort.x + (viewPort.width /2) + (10 * scale);
		
		
		endButtonArea.y = viewPort.y + buttonHeight;
		endButtonArea.x = viewPort.x + (viewPort.width/2) - buttonWidth/2;
		
		// update the fire button image position
		firebuttonX = viewPort.x + viewPort.width - fireButtonSize; 
		firebuttonY = viewPort.y;
		
		scale = ((float)Constant.NUM_ROWS / 12);
		// update fire button positions
		fireButtonUp.x = firebuttonX + (scale * 99);
		fireButtonUp.y = firebuttonY + (scale * 261);
		fireButtonLeft.x = firebuttonX; 
		fireButtonLeft.y = firebuttonY + (scale * 135);
		fireButtonRight.x = firebuttonX + (scale * 192);;
		fireButtonRight.y = firebuttonY + (scale * 135);
		fireButtonDown.x = firebuttonX + (scale * 99);
		fireButtonDown.y = firebuttonY + (scale * 7);
		
		return viewPort;
	}
	
	public Vector3 unprojected(float x, float y)
	{
		Vector3 touchPoint = new Vector3(x, y, 0);
		camera.unproject(touchPoint);
		return touchPoint;
	}
	
	public Vector2 joystickMovement(float x, float y)
	{
		joystickMovement.x = touchPad.x - x;
		joystickMovement.y = touchPad.y - y;
		return joystickMovement;		
	}
}
