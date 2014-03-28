package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Character
{
	// Characters speed
	private static int CHAR_SPEED = (int) (175 * Constant.GAME_SPEED);
	
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
	
	// size for collision checking 
	protected Rectangle collisionBounds = new Rectangle(0,0,0,0);
	
	// players state
	private boolean hit = false;
	private boolean isDead = false;
	private boolean livesLeft = true;
	private boolean transporting = false;
	private boolean appearing = false;
	private boolean appeared = false;
	private float appearingTime = 0f;
	private float stoodtimer = 0;
	static int lives = 3;
	static int deaths = 0;
	
	// frame to render
	private TextureRegion currentFrame;
	
	// State Time
	float stateTime = 0f;
	
	// sounds
	private Sound transportsound;
	
	// animation class
	private Animation[] animation = new Animation[5];
	private Animation[] teleport = new Animation[1];
	
	// Sounds
	private Sound DyingSound;
	
	// animation indexes
	private int up = 0;
	private int down = 1;
	private int right = 2; 
	private int left = 3;
	private int stood = 4;
	private int imageSet = 0;
	
	// state information
	private Array<Key> collectedKeys = new Array<Key>();
	
	Character(WorldController world, LevelMap levelMap, Animation[] animation, Animation[] teleport)
	{
		this.world = world;
		this.levelMap = levelMap;
		
		this.animation = animation;
		this.teleport = teleport;
		
		// Transport sound
		transportsound = Gdx.audio.newSound(Gdx.files.internal("data/Trans3.mp3"));
		
		// initialise vector and bounds
		movement = new Vector2(0,0);
		velocity = new Vector2(0,0);
		position = new Vector2(0,0);
		
		// initialise stateTime
		stateTime = 0f;
		
		bounds = new Rectangle(0,0, this.animation[0].getKeyFrame(0f).getRegionWidth()-1,this.animation[0].getKeyFrame(0f).getRegionHeight()-1);
		imageSet = stood;
		currentFrame = animation[imageSet].getKeyFrame(stateTime, true);
		deaths = 0;

		DyingSound = Gdx.audio.newSound(Gdx.files.internal("data/Dying.mp3"));
	}
	
	public void init()
	{
		// get start position from map
		position.set(levelMap.getCharStartPoint());
		// set bounds position to position
		bounds.x = position.x;
		bounds.y = position.y;
		
		// reset hit and dead state
		hit = false;
		isDead = false;
		lives = 3;
		
		// reset image
		imageSet = stood;
		currentFrame = animation[imageSet].getKeyFrame(stateTime, true);
		
		// reset movement
		movement.set(0,0);
		velocity.set(0,0);
	}
	
	public Array<Key> getKeys()
	{
		return collectedKeys;
	}
	
	public void isHit()
	{
		hit = true;
		// change to image set to fade out
		imageSet += 5;
		// reset clock for fading images
		stateTime = 0;
		// play dying sound
		DyingSound.play();
	}
	
	public boolean getTransporting()
	{
		return transporting;
	}
	
	public boolean isDead()
	{
		return isDead;
	}
	
	public Vector2 getCharacterPosition()
	{
		return position;
	}
	
	public Rectangle getCharacterBounds()
	{
		return bounds;
	}
	
	public Rectangle getCollisionBounds()
	{
		return collisionBounds;
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
		if (!hit)
		{
			if (!transporting) 
			{
				// calculate the movement by multiplying the velocity vector by time passed to get the movement
				movement.set(velocity.tmp().mul(deltaTime * CHAR_SPEED));
				// check if character should be transported.
				transbounds.set(bounds);
				if (levelMap.transportMe(transbounds, movement) == true) 
				{
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
				else
				{
					// check and validate movement
					levelMap.canIMove(bounds, movement, animation, false);
				}
			}
			else
			{
				if (appearing == false) 
				{
					// calculate the movement by multiplying the velocity vector by time passed to get the movement
					movement.set(transVelocity.tmp().mul(deltaTime * TRANS_SPEED));
					
					// update transport position
					bounds.x += movement.x;
					bounds.y += movement.y;
		
					// not appeared yet
	
					// check direction of travel & check if arrived at the destination
					if (transVelocity.y > 0 && bounds.y > transbounds.y) 
					{
						// arrived / start appearing
						appearing = true;
						bounds.x = transbounds.x;
						bounds.y = transbounds.y;
						transportsound.play();
					} 
					else if (transVelocity.y < 0 && bounds.y < transbounds.y) 
					{
						// arrived / start appearing
						appearing = true;
						bounds.x = transbounds.x;
						bounds.y = transbounds.y;
						transportsound.play();
					}
				}
				else
				{
					if (appeared)
					{
						// transport over reset state
						transporting = false;
						appearing = false;
						appeared = false;
						appearingTime = 0;
					}
				}
			}
		}
		// update character position to bounds position
		position.x = bounds.x;
		position.y = bounds.y;
		
		// update collision bounds
		// adjust collision size
		collisionBounds.x = bounds.x + 13;
		collisionBounds.y = bounds.y + 4;
		// subtraction includes additions to x & y 
		collisionBounds.width = bounds.width - 26;
		collisionBounds.height =  bounds.height -8;
	}
	
	void draw(SpriteBatch batch)
	{
		if (hit)
		{
			// dying
			currentFrame = animation[imageSet].getKeyFrame(stateTime, false);
			if (animation[imageSet].isAnimationFinished(stateTime))
			{
				if (stateTime > 3f) isDead = true;
			}
			// draw animation
			batch.draw(currentFrame, position.x, position.y);
		}
		else
		{
			if (!transporting) 
			{
				// Set image set to reflex movement
				if (movement.y > 0) 
				{
					imageSet = up;
					stoodtimer = 0;
				}
				else if (movement.y < 0) 
				{
					imageSet = down;
					stoodtimer = 0;
				} 
				else if (movement.x > 1) 
				{
					imageSet = right;
					stoodtimer = 0;
				}
				else if (movement.x < -1) 
				{
					imageSet = left;
					stoodtimer = 0;
				}
				else
				{
					// increment delay timer				
					stoodtimer += Gdx.graphics.getDeltaTime();
					
					// check if stood for over 1.5 seconds
					if (stoodtimer > 1.5 && world.paused == false)
					{
						// reset timer and select a stood position
						stoodtimer = 0;
						currentFrame = animation[stood].getKeyFrame(stateTime, true);
					}
				}
				
				// pick correct frame
				if (movement.x !=0 || movement.y != 0) currentFrame = animation[imageSet].getKeyFrame(stateTime, true);
				// draw image
				batch.draw(currentFrame, position.x, position.y);
			}
			else if (appearing == true)
			{
				// update animation time state
				appearingTime += Gdx.graphics.getDeltaTime();
				// pick correct frame
				currentFrame = teleport[0].getKeyFrame(appearingTime, false);
				if (teleport[0].isAnimationFinished(appearingTime))
				{
					appeared = true;

				}
				// draw animation
				batch.draw(currentFrame, position.x, position.y);
			}
		}	
	}
	
	public void addkeys(Array<Key> keys)
	{
		collectedKeys.addAll(keys);
	}
	
	public boolean isLevelfinished()
	{
		int numXBlock = 0;
		int numYBlock = 0;
		
		// convert to block references
		int xblock = (int) bounds.x / Constant.BLOCK_SIZE;
		int yblock = (int) bounds.y / Constant.BLOCK_SIZE;
		
		// check if the block is a NEXT_LEVEL block
		if (levelMap.cellProperties(xblock, yblock) == Constant.BLOCKVALUES.NEXT_LEVEL.getValue())
		{
			// check if the block adjacent are also
			if (levelMap.cellProperties(xblock+1, yblock) == Constant.BLOCKVALUES.NEXT_LEVEL.getValue()) numXBlock = 2; else numXBlock =1;
			if (levelMap.cellProperties(xblock, yblock+1) == Constant.BLOCKVALUES.NEXT_LEVEL.getValue()) numYBlock = 2; else numYBlock =1;
			
			// check that the character is within the areas of the blocks
			Rectangle testArea = new Rectangle(bounds.x - 2, bounds.y - 2, (Constant.BLOCK_SIZE * numYBlock) + 4, (Constant.BLOCK_SIZE * numXBlock +4));
			if (testArea.contains(bounds))
			{
				return true;
			}
			
		}
		return false;
	}
	
	public boolean livesleft() {
		deaths++;
		if(deaths >= lives)
		{
			livesLeft = false;
		}
		//System.out.println("Deaths: " + deaths);
		//System.out.println("Points: " + score);
		return livesLeft;
	}
}
