package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Character
{
	// Characters speed
	private static int CHAR_SPEED = 200;
	
	// Transport speed
	private static int TRANS_SPEED = 1000;
	
	// Characters velocity 0, 1 or -1
	private Vector2 velocity;
	private Vector2 transVelocity = new Vector2(0,0);
	
	// Characters position x,y
	private Vector2 position;
	
	// amount of movement to to be tested with map
	private Vector2 movement;
	
	// Object references
	private WorldController world;
	private LevelMap levelMap;
	
	// boundary of character
	private Rectangle bounds;
	private Rectangle transbounds = new Rectangle(0,0,0,0);
	
	// players state
	private boolean hit = false;
	private boolean transporting = false;

	// frame to render
	private TextureRegion currentFrame;
	
	// State Time
	float stateTime = 0f;
	
	// sounds
	private Sound transportsound;
	
	// animation class
	private Animation[] animation = new Animation[5];
	
	// animation indexes
	private int up = 0;
	private int down = 1;
	private int right = 2; 
	private int left = 3;
	private int stood = 4;
	private int imageSet = 0;
	
	Character(WorldController world, LevelMap levelMap, Animation[] animation)
	{
		this.world = world;
		this.levelMap = levelMap;
		
		this.animation = animation;
		
		// Transport sound
		transportsound = Gdx.audio.newSound(Gdx.files.internal("data/Trans3.mp3"));
		
		// initialise vector and bounds
		movement = new Vector2(0,0);
		velocity = new Vector2(0,0);
		position = new Vector2(0,0);
		
		// initialise stateTime
		stateTime = 0f;
		
		// It been necessary to reduce the size by 3 pixels to prevent it moving pass opening in a single frame update
		// if further works required to this then see LevelMap - canIMove method (needs to check for openings between start and moved to position.
		bounds = new Rectangle(0,0, this.animation[0].getKeyFrame(0f).getRegionWidth()-3,this.animation[0].getKeyFrame(0f).getRegionHeight()-3);
	}
	
	public void init()
	{
		// get start position from map
		position.set(levelMap.getCharStartPoint());
		// set bounds position to position
		bounds.x = position.x;
		bounds.y = position.y;
	}
	
	Vector2 getCharacterPosition()
	{
		return position;
	}
	
	void moveRight()
	{
		velocity.x = 1;
	}
	
	void moveLeft()
	{
		velocity.x = - 1;
	}
	
	void moveUp()
	{
		velocity.y = 1;
	}
	
	void moveDown()
	{
		velocity.y = -1;
	}
	
	void stopVerticalMove()
	{
		velocity.y = 0;
	}
	
	void stopHoziontialMove()
	{
		velocity.x =0;
	}
	
	void update(float deltaTime)
	{
		// update statetime
		stateTime += deltaTime;
		if (!transporting) 
		{
			// calculate the movement by multiplying the velocity vector by time passed to get the movement
			movement.set(velocity.tmp().mul(deltaTime * CHAR_SPEED));
			// check if character should be transported.
			transbounds.set(bounds);
			if (levelMap.transportMe(transbounds, movement) == true) 
			{
				System.out.println("character position "+bounds.x+","+bounds.y);
				
				System.out.println("transport position "+transbounds.x+","+transbounds.y);
				// start transport
				transporting = true;
				if (transbounds.y > bounds.y)
				{
					transVelocity.y = 1;
				}
				else
				{
					transVelocity.y = -1;
				}
			}
			// check and validate movement
			levelMap.canIMove(bounds, movement);
		}
		else
		{
			// calculate the movement by multiplying the velocity vector by time passed to get the movement
			movement.set(transVelocity.tmp().mul(deltaTime * TRANS_SPEED));
			
			// update transport position
			bounds.x += movement.x;
			bounds.y += movement.y;

			// check direction of travel & check if arrived at the destination
			if (transVelocity.y > 0 && bounds.y > transbounds.y)
			{
				bounds.x = transbounds.x;
				bounds.y = transbounds.y;
				transporting = false;

				transportsound.play();
			}
			else if (transVelocity.y < 0 && bounds.y < transbounds.y)
			{
				bounds.x = transbounds.x;
				bounds.y = transbounds.y;
				transporting = false;

				transportsound.play();
			}
		}
		// update character position to bounds position
		position.x = bounds.x;
		position.y = bounds.y;
	}
	
	void draw(SpriteBatch batch)
	{
		if (!transporting) 
		{
			// Set image set to reflex movement
			if (movement.y > 0) {
				imageSet = up;
			} else if (movement.y < 0) {
				imageSet = down;
			} else if (movement.x > 1) {
				imageSet = right;
			} else if (movement.x < -1) {
				imageSet = left;
			} else
				imageSet = stood;
			// pick correct frame
			currentFrame = animation[imageSet].getKeyFrame(stateTime, true);
			// draw image
			batch.draw(currentFrame, position.x, position.y);
		}
	}
}
