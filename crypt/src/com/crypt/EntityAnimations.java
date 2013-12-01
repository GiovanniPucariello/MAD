package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class EntityAnimations {

	// animation class
	private Animation[] animation = new Animation[10];

	// animation arranged as so
	// animation[0] is UP and contains texture array 1,2,3 etc.
	// animation[1] is DOWN and contains texture array 1,2,3 etc.
	// animation[2] is RIGHT and contains texture array 1,2,3 etc.
	// animation[3] is LEFT and contains texture array 1,2,3 etc.
	// animation[4] is STOOD and contains texture array 1,2,3 etc. (If Required)

	public EntityAnimations(String filename, int rows, float interval,int splitWidth, int splitHeight)
	{
		// Get animation images and split into 2 dim array
		TextureRegion images[][] = TextureRegion.split(new Texture(Gdx.files.internal(filename)), splitWidth, splitHeight);
		
		// load animation array
		for(int i =0; i <= rows; i++)
		{
			animation[i] = new Animation(interval, images[i]);
		}
	}
	
	public Animation[] getAnimations()
	{
		return animation;
	}
}
