package com.crypt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Treasure 
{
	// area for character to enter to get treasure
	private Rectangle bounds = new Rectangle(0,0,0,0);
	
	// position of treasure to draw
	private Vector2 position = new Vector2(0,0);
	
	// points awarded for collecting treasure
	int points;
	
	// image of treasure to draw
	TextureRegion[] image;
	int imageIndex;
	
	// collected flag
	boolean collected = false;
	
	/**
	 * @param position - Vector2 screen position
	 * @param image - Texture of the treasure
	 * @param bounds - rectangle for character to be in to collect treasure
	 */
	public Treasure (Vector2 position, TextureRegion[] image, int imageIndex, int points, Rectangle bounds)
	{
		// init values
		this.position.set(position);
		this.image = image;
		this.points = points;
		this.bounds.set(bounds);
		this.imageIndex = imageIndex;
	}
	
	public boolean collected()
	{
		return collected;
	}
	
	public int collectTreasure(Rectangle character)
	{
		if (collected == false)
		{
			// check if the character has entered the boundary required to collect the treasure
			if (bounds.contains(character)) {
				collected = true;
				return points;
			}
		}
		return 0;
	}
	
	public void draw(SpriteBatch batch)
	{
		// only draw if not collected
		if (collected == false)
		{
			batch.draw(image[imageIndex], position.x, position.y);
		}
	}
}
