package com.crypt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Entity 
{
	protected boolean transparent = false;
	
	// velocity x & y should only be set to -1, 0 , 1
	protected Vector2 velocity = new Vector2(0,0);
	
	// size of entity for movement
	protected Rectangle bounds = new Rectangle(0,0,0,0);
	
	// size of entity for collision checking 
	protected Rectangle collisionBounds = new Rectangle(0,0,0,0);
	protected Vector2 collisionBotLeftAdjust = new Vector2(0,0);
	protected Vector2 collisionTopRightAdjust = new Vector2(0,0);
	
	// the frame to be rendered
	protected TextureRegion currentFrame;
	
	// flag to hit
	protected boolean hit = false;
	// flag dead
	protected boolean dead = false;
	
	// flag on screen
	protected boolean onScreen;
	protected boolean active = false;
	
	// reference to level map
	protected LevelMap levelMap;
	
	// a constant that should be set when the entity is created
	protected float CHAR_SPEED;
	
	// temporary storage for the current frames movement
	// also used to communicate back from level map if the entity
	// is unable to move in the requested direction
	protected Vector2 movement = new Vector2(0,0);
	
	// statetime used to calculate the correct image to display
	protected float stateTime;
	
	protected float deltaTime;
	
	// time entity has been off screen
	protected float offScreenTimer;
	protected float offScreenValue = 5;
	
	// time interval for random changes of direction
	protected float randomInterval = 1f;
	protected float randomIntervalTimer = 0;
	
	// animation collection indexed as shown below
	protected Animation[] animation = new Animation[4];
	
	// animation indexes
	private int up = 0;
	private int down = 1;
	private int right = 2; 
	private int left = 3;
	private int dying = 4;

	// used with currentframe in draw method
	private int imageSet = 0;
	
	// The id of the spawn site that created this creature
	protected int spawnSiteID;
	
	public Entity(Vector2 position, Animation[] animation, Vector2 collisionBotLeftAdjust, Vector2 collisionTopRightAdjust, int spawnSiteID, boolean active)
	{
		this.spawnSiteID = spawnSiteID;
		this.animation = animation;
		this.bounds = new Rectangle(0,0, this.animation[0].getKeyFrame(0f).getRegionWidth(),this.animation[0].getKeyFrame(0f).getRegionHeight());
		this.active = active;
		
		// initialise boundary of object
		bounds.x = position.x;
		bounds.y = position.y;
		// set width and height to first animation frame
		bounds.width = animation[0].getKeyFrame(0f).getRegionWidth()-1;
		bounds.height = animation[0].getKeyFrame(0f).getRegionWidth()-1;
		
		this.collisionBotLeftAdjust = collisionBotLeftAdjust;
		this.collisionTopRightAdjust = collisionTopRightAdjust;
		
		currentFrame = animation[up].getKeyFrame(0, true);
	}
	
	public void update(float deltaTime, Rectangle Viewport, Array<Entity> monsters)
	{
		if (active) {
			// update statetime
			stateTime += deltaTime;
			randomIntervalTimer += deltaTime;
			if (randomIntervalTimer > randomInterval) {
				randomIntervalTimer = 0;
				randomlyChangeDirection();
			}
			// if not hit
			if (!hit) {
				// calculate the movement by multiplying the velocity vector by time passed to get the movement
				movement.set(velocity.tmp().mul(deltaTime * CHAR_SPEED));

				updateCollisionBounds();

				// check and validate movement
				if (levelMap.canIMove(bounds, movement, animation, false) == false) {
					// check returned movement to see if it did not move vertically or horizontally
					if (movement.x == 0 && movement.y == 0) {
						changeDirection();
					}
				}

				// check if collided with another creature
				if (collided(monsters)) {
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
	
	public boolean collided(Array<Entity> monsters)
	{
		// check if collection contains items to check
		if (monsters != null)
		{
			for (int index = 0; index < monsters.size; index++) 
			{
		        Entity creature = monsters.get(index);
				if (creature != this  && creature.transparent == false)
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

	protected void checkOffScreen(float deltaTime, Rectangle Viewport) {
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

	protected void updateCollisionBounds() {
		// update collision bounds
		collisionBounds.set(bounds);
		
		// adjust collision size
		collisionBounds.x += collisionBotLeftAdjust.x;
		collisionBounds.y += collisionBotLeftAdjust.y;
		// subtraction includes additions to x & y
		collisionBounds.width += collisionTopRightAdjust.x + (collisionBotLeftAdjust.x * -1);
		collisionBounds.height +=  collisionTopRightAdjust.y + (collisionBotLeftAdjust.y * -1);
	}
	
	// override this method with logic for a change of direction.
	// complete vector to movement to see in which direction it was moving
	// and no longer travel.  levelMap.canIMove can be use to try a new direction.
	abstract void changeDirection(); 
	
	abstract void randomlyChangeDirection();
	
	protected boolean removeFromGame()
	{
		if (offScreenTimer > offScreenValue) return true;
		return false;
	}

	void draw(SpriteBatch batch)
	{
		if (active) {
			if (onScreen) {
				if (!hit) {
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
	
	public boolean collision(Rectangle item)
	{
		if (collisionBounds.overlaps(item)) return true;
		return false;
	}
	
	public boolean spawnCollision(Rectangle item)
	{
		if (bounds.overlaps(item)) return true;
		return false;
	}
	
	public void setHit(boolean hit)
	{
		if (this.hit == false)
		{
			this.hit = hit;
			stateTime = 0f;
		}
	}
	
	
}
