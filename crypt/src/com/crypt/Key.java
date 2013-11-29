package com.crypt;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Key 
{
	// area for character to enter to get treasure
	private Rectangle bounds = new Rectangle(0,0,0,0);
	
	// position of treasure to draw
	private Vector2 position = new Vector2(0,0);
	
	// key number
	int keyValue;
	
	// image of treasure to draw
	TextureRegion[] image;
	int imageIndex;
	
	// collected flag
	boolean collected = false;
	
	/**
	 * @param position - Vector2 screen position
	 * @param image - Texture index of the key
	 * @param keyValue - value of the key e.g. 1, 2, 3 etc. (max of 10 keys per level)
	 * @param bounds - rectangle for character to be in to collect treasure
	 */
	public Key(Vector2 position, TextureRegion[] image, int imageIndex, int keyValue, Rectangle bounds)
	{
		// init values
		this.position.set(position);
		this.image = image;
		this.keyValue = keyValue;
		this.bounds.set(bounds);
		this.imageIndex = imageIndex;
	}
	
	public boolean collected()
	{
		return collected;
	}
	
	public Key collectKey(Rectangle character)
	{
		if (collected == false)
		{
			// check if the character has entered the boundary required to collect the treasure
			if (bounds.contains(character)) 
			{
				collected = true;
				return this;
			}
		}
		return null;
	}
	
	public void draw(SpriteBatch batch)
	{
		// only draw if not collected
		if (collected == false)
		{
			batch.draw(image[imageIndex], position.x, position.y);
		}
	}
	
	public int getKeyValue()
	{
		return keyValue;
	}
}
