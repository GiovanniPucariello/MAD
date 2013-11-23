package com.crypt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity 
{
	// velocity x & y should only be set to -1, 0 , 1
	protected Vector2 velocity;
	
	// size of entity for collision checking and movement
	protected Rectangle bounds;
	
	// the frame to be rendered
	protected TextureRegion currentFrame;
	
	// flag to hit
	protected boolean hit;
	
	// reference to level map
	protected LevelMap levelMap;
	
	// a constant that should be set when the entity is created
	protected float CHAR_SPEED;
	
	// temporary storage for the current frames movement
	// also used to communicate back from level map if the entity
	// is unable to move in the requested direction
	protected Vector2 movement;
	
	// statetime used to calculate the correct image to display
	private float stateTime;
	
	// animation collection indexed as shown below
	private Animation[] animation = new Animation[4];
	
	// animation indexes
	private int up = 0;
	private int down = 1;
	private int right = 2; 
	private int left = 3;

	// used with currentframe in draw method
	private int imageSet = 0;
	
	public Entity(Vector2 position, Animation[] animation)
	{
		this.animation = animation;
		
		// initialise boundary of object
		bounds.x = position.x;
		bounds.y = position.y;
		// set width and height to first animation frame
		bounds.width = animation[0].getKeyFrame(0f).getRegionWidth() - 3;
		bounds.height = animation[0].getKeyFrame(0f).getRegionWidth() - 3;
	}
	
	public void update(float deltaTime)
	{
		// update statetime
		stateTime += deltaTime;
		
		// calculate the movement by multiplying the velocity vector by time passed to get the movement
		movement.set(velocity.tmp().mul(deltaTime * CHAR_SPEED));
		
		// check and validate movement
		if (levelMap.canIMove(bounds, movement) == false)
		{
			// check returned movement to see if it did not move vertically or horizontally
			if (movement.x == 0 && movement.y == 0)
			{
				changeDirection();
			}
		}	
	}
	
	private void changeDirection() {
		// override this method with logic for a change of direction.
		// complete vector to movement to see in which direction it was moving
		// and no longer travel.  levelMap.canIMove can be use to try a new direction.
		
	}

	void draw(SpriteBatch batch)
	{
		imageSet = up;
		// Set image set to reflect movement
		if (movement.y > 0)
		{
			imageSet = up;
		}
		else if (movement.y < 0)
		{
			imageSet = down;
		}
		else if (movement.x > 1)
		{
			imageSet = right;
		}
		else if (movement.x < -1)
		{
			imageSet = left;
		}
				
		// pick correct frame
		currentFrame = animation[imageSet].getKeyFrame(stateTime, true);
		
		// draw image
		batch.draw(currentFrame, bounds.x, bounds.y);
	}
	
	public boolean collision(Rectangle item)
	{
		if (bounds.overlaps(item)) return true;
		return false;
	}
	
	public void hit()
	{
		
	}
	
	
}
