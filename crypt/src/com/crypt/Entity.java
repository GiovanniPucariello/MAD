package com.crypt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity 
{
	// velocity x & y should only be set to -1, 0 , 1
	protected Vector2 velocity = new Vector2(0,0);
	
	// size of entity for collision checking and movement
	protected Rectangle bounds = new Rectangle(0,0,0,0);
	
	// the frame to be rendered
	protected TextureRegion currentFrame;
	
	// flag to hit
	protected boolean hit;
	
	// flag on screen
	protected boolean onScreen;
	
	// reference to level map
	protected LevelMap levelMap;
	
	// a constant that should be set when the entity is created
	protected float CHAR_SPEED;
	
	// temporary storage for the current frames movement
	// also used to communicate back from level map if the entity
	// is unable to move in the requested direction
	protected Vector2 movement = new Vector2(0,0);
	
	// statetime used to calculate the correct image to display
	private float stateTime;
	
	// time entity has been off screen
	protected float offScreenTimer;
	
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
		this.bounds = new Rectangle(0,0, this.animation[0].getKeyFrame(0f).getRegionWidth(),this.animation[0].getKeyFrame(0f).getRegionHeight());
		// initialise boundary of object
		bounds.x = position.x;
		bounds.y = position.y;
		// set width and height to first animation frame
		bounds.width = animation[0].getKeyFrame(0f).getRegionWidth()-3;
		bounds.height = animation[0].getKeyFrame(0f).getRegionWidth()-3;
		
		System.out.println("X: "+bounds.x+" Y: "+bounds.y);
	}
	
	public void update(float deltaTime, Rectangle Viewport)
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
		// check if on screen
		if (!Viewport.overlaps(bounds))
		{
			onScreen = false;
			offScreenTimer += deltaTime;
		}
		else
		{
			onScreen = true;
			offScreenTimer = 0f;
		}
	}
	
	protected void changeDirection() {
		// override this method with logic for a change of direction.
		// complete vector to movement to see in which direction it was moving
		// and no longer travel.  levelMap.canIMove can be use to try a new direction.
		
	}
	
	protected boolean removeFromGame()
	{
		if (offScreenTimer > 5000) return true;
		return false;
	}
	

	void draw(SpriteBatch batch)
	{
		if (onScreen || !onScreen) 
		{
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
			currentFrame = animation[imageSet].getKeyFrame(stateTime, true);
			// draw image
			batch.draw(currentFrame, bounds.x, bounds.y);
		}
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
