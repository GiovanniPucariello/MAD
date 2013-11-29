package com.crypt;

import com.badlogic.gdx.Gdx; 
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CharacterAnimations2 
{		
	// TextureRegion Rows and Columns
	private static final int FRAMES_COL = 2;
	private static final int FRAMES_ROW = 2;
	
	private Animation animation;
	private TextureRegion[] frames;
	private TextureRegion currentFrame;
	private float stateTime;
	
	public CharacterAnimations2(String filename, int rows, float interval,int splitWidth, int splitHeight) 
	{
		TextureRegion[][] temp = TextureRegion.split(new Texture(Gdx.files.internal(filename)), splitWidth, splitHeight);
		frames = new TextureRegion[FRAMES_COL * FRAMES_ROW];
		
		int index = 0;
		
		for(int i=0; i<FRAMES_ROW; i++)
		{
			for(int j=0; j<FRAMES_COL; j++)
			{
				frames[index++] = temp[i][j];
			}
		}
	
		animation = new Animation(0.1f, frames);
		stateTime = 0f;
	}
	
	public TextureRegion getAnimations()
	{
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animation.getKeyFrame(stateTime, true);
		
		return currentFrame;
	}
}
