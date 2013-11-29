package com.crypt;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Door 
{
	// area for character to enter to open the door
	private Rectangle[] areas = new Rectangle[4];
	
	// internal area of door for map help
	private Rectangle doorBottomArea;
	private Rectangle doorAllArea;
	
	// position of door
	private Vector2 position = new Vector2(0,0);
	
	// key that opens the door
	private int keyValue;
	
	// image of closed door
	TextureRegion image;
	
	// animation of door opening
	Animation animation[];
	TextureRegion currentFrame;
		
	// state flag
	boolean open = false;
	boolean opening = false;
	float stateTime = 0;
	
	/**
	 * @param position - Vector2 screen position
	 * @param image - Texture index of the key
	 * @param keyValue - value of the key e.g. 1, 2, 3 etc. (max of 10 keys per level)
	 * @param bounds - rectangle for character to be in to collect treasure
	 */
	public Door(Vector2 position, TextureRegion image, Animation[] animation2, int keyValue)
	{
		// init values
		this.position.set(position);
		this.image = image;
		this.animation = animation2;
		this.keyValue = keyValue;
		
		// setup areas character must enter carrying key to open door
		areas[0] = new Rectangle(position.x - 2, position.y - Constant.BLOCK_SIZE  - 2, Constant.BLOCK_SIZE * 2 + 4, Constant.BLOCK_SIZE + 4);
		areas[1] = new Rectangle(position.x - Constant.BLOCK_SIZE - 2, position.y - 2, Constant.BLOCK_SIZE + 4, Constant.BLOCK_SIZE * 2 + 4);
		areas[2] = new Rectangle(position.x + Constant.BLOCK_SIZE * 2 - 2, position.y - 2, Constant.BLOCK_SIZE + 4, Constant.BLOCK_SIZE * 2 + 4);
		
		doorBottomArea = new Rectangle(position.x-1,position.y-1,Constant.BLOCK_SIZE * 2 + 2, Constant.BLOCK_SIZE+1);
		doorAllArea = new Rectangle(position.x-1,position.y-1,Constant.BLOCK_SIZE * 2 + 2, Constant.BLOCK_SIZE * 2 +2);
	}
	
	public boolean contains(int x, int y)
	{
		
		if (doorAllArea.contains(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isOpen(int x, int y)
	{
		// check x,y in the door
		if (doorAllArea.contains(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE)) 
		{
			// check if the door is open
			if (open == true) 
			{
				return true;
			}
			// check if the door is opening
			else if (opening == true) 
			{
				// check if the door is over half way open
				if (stateTime < 0.86f)
				{
					return false;
				}
				// over half open, check that in the bottom half
				else if(doorBottomArea.contains(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean pleaseOpenDoor(Rectangle character, Array<Key> keyCollection)
	{
		if (!open) {
			// loop through the areas surrounding the door
			for (int i = 0; i < 3; i++) {
				if (areas[i].contains(character)) {
					// character inside one of the areas now check his has the necessary key
					Iterator<Key> iter = keyCollection.iterator();
					while (iter.hasNext()) {
						Key key = iter.next();
						// check each key
						if (key.getKeyValue() == keyValue) {
							// key match found
							opening = true;
							// remove key from collection
							iter.remove();
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public void draw(SpriteBatch batch, float deltatime)
	{
		if (opening == true)
		{
			stateTime += deltatime;
			// pick correct frame
			currentFrame = animation[0].getKeyFrame(stateTime, false);
			if (animation[0].isAnimationFinished(stateTime))
			{
				opening = false;
				open = true;
			}
			// draw animation
			batch.draw(currentFrame, position.x, position.y);
		}
		// only draw if door closed
		else if (open == false)
		{
			batch.draw(image, position.x, position.y);
		}
	}
}
