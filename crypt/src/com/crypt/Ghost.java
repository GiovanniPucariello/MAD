package com.crypt;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Ghost extends Entity
{	
	Random randomGenerator = new Random();
	private WorldController worldController;
	
	//Controls guarding behaviour
	private boolean guarding = false;
	private float guardTimer = 0f;
	private float guardInterval = 4f;
	//Calls changeMode to determine if the ghost should turn transparent/start guarding or do nothing
	private float changeModeTimer = 0f;
	private float changeModeInterval = 2f;
	//To make sure that the ghost doesn't become solid straight away
	private float transparentTimer = 0f;
	private float transparentMin = 2f;
	
	// animation indexes
	private int up = 0;
	private int down = 1;
	private int right = 2; 
	private int left = 3;
	private int fadeoutUp = 4;
	private int fadeoutDown = 5;
	private int fadeoutright = 6; 
	private int fadeoutleft = 7;
	private int transparentUp = 8;
	private int transparentDown = 9;
	private int transparentRight = 10; 
	private int transparentLeft = 11;
	private int fadeinUp = 12;
	private int fadeinDown = 13;
	private int fadeinRight = 14; 
	private int fadeinLeft = 15;
	private int guard = 17;
	private int dying = 5; // no animation currently available
	
	// used with currentframe in draw method
	private int imageSet = 0;
	
	public Ghost(WorldController worldController, Vector2 position, Animation[] animation, LevelMap levelMap, int spawnSiteID, boolean active)
	{	
		super(position, animation, new Vector2(8,2), new Vector2(-8,-2), spawnSiteID, active);
		
		currentFrame = animation[up].getKeyFrame(0, true);
		
		this.levelMap = levelMap;
		this.worldController = worldController;
		
		CHAR_SPEED = 60 * Constant.GAME_SPEED;
		
		//Set velocity of mummy at spawn, if 0,0: do it again.
		velocity.x = randomGenerator.nextInt(2)-1;
		velocity.y = randomGenerator.nextInt(2)-1;		
		
		while (velocity.x == 0.0 && velocity.y == 0.0) 
		{
			velocity.x = randomGenerator.nextInt(3)-1;
			velocity.y = randomGenerator.nextInt(3)-1;
		}
	}
	
	@Override
	public void update(float deltaTime, Rectangle Viewport, Array<Entity> monsters)
	{
		if (active) {
			// update statetime
			this.deltaTime += deltaTime;
			stateTime += deltaTime;
			
			//every 2 seconds, this timer calls changeMode
			changeModeTimer += deltaTime;
			if (changeModeTimer > changeModeInterval) {
				changeModeTimer = 0;
				changeMode(monsters);
			}			
			
			// if not hit
			if (!hit) {
				// calculate the movement by multiplying the velocity vector by time passed to get the movement
				movement.set(velocity.tmp().mul(deltaTime * CHAR_SPEED));

				updateCollisionBounds();
				
				//keeps him guarding and stops the changeMode method to be called
				if (guarding)
				{
					velocity.x = 0;
					velocity.y = 0;
					guardTimer += deltaTime;
					changeModeTimer = 0f;
				}
				
				//stops guarding
				if (guardTimer > guardInterval)
				{
					System.out.println("Stops guarding.");
					
					guarding = false;
					guardTimer = 0f;
					velocity.x = randomGenerator.nextInt(3)-1;
					velocity.y = randomGenerator.nextInt(3)-1;
				}
				
				//Makes sure the ghost doesn't go solid the second he becomes transparent
				if(transparent)
				{
					transparentTimer += deltaTime;

					if(transparentTimer > transparentMin)
					{
						if (!collided(monsters) && inPassageWay())
						{
							System.out.println("Solid");
		
							transparent = false;
							guarding = false;
							transparentTimer = 0f;
						}
					}	
				}
				
				// check and validate movement
				if (levelMap.canIMove(bounds, movement, animation, transparent) == false) {
					// check returned movement to see if it did not move vertically or horizontally
					if (movement.x == 0 && movement.y == 0 && !guarding) {
						changeDirection();
					}
				}
				
				// check if collided with another creature
				if (moveCollided(monsters)) {
					if (transparent) System.out.println("changing direction");
					if (movement.x != 0) {
						// inverse direction
						velocity.x *= -1;
						// undo movement
						bounds.x += velocity.x * deltaTime * CHAR_SPEED;
						movement.x = 0;
					}
					if (movement.y != 0) {
						// inverse direction
						velocity.y *= -1;
						// undo movement
						bounds.y += velocity.y * deltaTime * CHAR_SPEED;
						movement.y = 0;
					}
				}
				// check if on screen
				checkOffScreen(deltaTime, Viewport);
			}
		}
	}
	
	void changeMode(Array<Entity> monsters) 
	{	
		System.out.println("changeMode()");
		
		if(!transparent)
		{
			if (randomGenerator.nextInt(4) == 1)
			{
				System.out.println("Transparent");
				
				transparent = true;
			}
			else if(randomGenerator.nextInt(7) == 1)
			{
				if(!collided(monsters) && inPassageWay())
				{
					System.out.println("Guarding.");
					
					guarding = true;
				}
			}
		}
		changeDirection();
	}
	
	@Override
	public void changeDirection() 	
	{	
		Vector2 characterPosition = new Vector2(worldController.getCharacterPosition());
		
		if (transparent) 
		{
			if (bounds.x < characterPosition.x)
			{
				//System.out.println("Moving right");
				velocity.x = 1;
			}
			else if (bounds.x > characterPosition.x)
			{
				//System.out.println("Moving left");
				velocity.x = -1;
			}
			if (bounds.y < characterPosition.y)
			{
				//System.out.println("Moving up");
				velocity.y = 1;
			}
			else if (bounds.y > characterPosition.y)
			{
				//System.out.println("Moving down");
				velocity.y = -1;
			}
		}
		else if (!transparent)
		{
			if(randomGenerator.nextInt(5) < 2)
			{
				if (bounds.x < characterPosition.x)
				{
					velocity.x = 1;
				}
				else if (bounds.x > characterPosition.x)
				{
					velocity.x = -1;
				}
				if (bounds.y < characterPosition.y)
				{
					velocity.y = 1;
				}
				else if (bounds.y > characterPosition.y)
				{
					velocity.y = -1;
				}
			}	
			else
			{
				velocity.x = randomGenerator.nextInt(3)-1;
				velocity.y = randomGenerator.nextInt(3)-1;
			}
		}
		while (velocity.x == 0.0 && velocity.y == 0.0) 
		{
			velocity.x = randomGenerator.nextInt(3)-1;
			velocity.y = randomGenerator.nextInt(3)-1;
		}
	}
	
	private boolean inPassageWay()
	{
		if(checkBlock(bounds.x, bounds.y)) return false;
		
		if(checkBlock(bounds.x+bounds.width, bounds.y)) return false;
		
		if(checkBlock(bounds.x+bounds.width, bounds.y+bounds.height)) return false;
		
		if(checkBlock(bounds.x, bounds.y+bounds.height)) return false;
		
		return true;
	}
	
	private boolean checkBlock(float x, float y)
	{
		int xblock = (int) (x / Constant.BLOCK_SIZE);
		int yblock = (int) (y / Constant.BLOCK_SIZE);
		
		return levelMap.Cell(xblock, yblock) != Constant.BLOCKVALUES.PASSAGE.getValue();
	}
	
	public boolean moveCollided(Array<Entity> monsters)
	{
		if (transparent)
		{
			if (monsters != null)
			{
				for (int index = 0; index < monsters.size; index++) 
				{
			        Entity creature = monsters.get(index);
					if (creature != this && (creature instanceof Ghost))
					{
						if (bounds.overlaps(creature.bounds))
						{
							return true;
						}
					}
				}
			}
		}
		else
		{
			return collided(monsters);	
		}
		return false;
	}
	
	//Makes sure that the ghost won't collide with other creatures while transparent.
	@Override
	public boolean collided(Array<Entity> monsters)
	{
		// check if collection contains items to check
		if (monsters != null)
		{
			for (int index = 0; index < monsters.size; index++) 
			{
		        Entity creature = monsters.get(index);
				if (creature != this)
				{
					if (bounds.overlaps(creature.bounds))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	//Makes sure that the ghost won't collide with bullets while transparent, but still will collide with character.
	@Override
	public boolean collision(Rectangle item)
	{
		if (item == worldController.getCharacterCollisionBounds() && collisionBounds.overlaps(item))
		{ 
			return true;
		}
		else if(item != worldController.getCharacterCollisionBounds() && collisionBounds.overlaps(item) && !transparent)
		{
			return true;
		}
		return false;
	}
	
	public void randomlyChangeDirection()
	{
		
	}
	
	@Override
	void draw(SpriteBatch batch)
	{
		if (active) {
			if (onScreen) {
				if (!hit) {
					if (!transparent && !guarding) {
						imageSet = up;
						// Set image set to reflect movement
						if (movement.y > 0) {
							imageSet = up;
						} else if (movement.y < 0) {
							imageSet = down;
						} else if (movement.x > 0) {
							imageSet = right;
						} else if (movement.x < 0) {
							imageSet = left;
						}
						// pick correct frame
						if (movement.x != 0 || movement.y != 0)
							currentFrame = animation[imageSet].getKeyFrame(
									stateTime, true);
						// draw image
						batch.draw(currentFrame, bounds.x, bounds.y);
					} 
					else if (transparent) {
						imageSet = transparentUp;
						// Set image set to reflect movement
						if (movement.y > 0) {
							imageSet = transparentUp;
						} else if (movement.y < 0) {
							imageSet = transparentDown;
						} else if (movement.x > 0) {
							imageSet = transparentRight;
						} else if (movement.x < 0) {
							imageSet = transparentLeft;
						}
						// pick correct frame
						if (movement.x != 0 || movement.y != 0)
							currentFrame = animation[imageSet].getKeyFrame(
									stateTime, true);
						// draw image
						batch.draw(currentFrame, bounds.x, bounds.y);
					}
					else if (guarding) {
						imageSet = guard;
						// Set image set to reflect movement
						if (movement.x == 0 && movement.y == 0) {
							imageSet = guard;
						}
						// pick correct frame
						currentFrame = animation[imageSet].getKeyFrame(
									stateTime, true);
						// draw image
						batch.draw(currentFrame, bounds.x, bounds.y);
					}
				} else {
					// pick correct frame
					currentFrame = animation[dying].getKeyFrame(stateTime,
							false);
					if (animation[dying].isAnimationFinished(stateTime)) {
						dead = true;
					}
					// draw animation
					batch.draw(currentFrame, bounds.x, bounds.y);
				}
			}
		}
	}
}
