package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets 
{
	// asset objects
	private EntityAnimations character = new EntityAnimations("data/ManSprites.png", 4, 0.075f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	private EntityAnimations mummy = new EntityAnimations("data/MummySprite.png",3,0.1f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	private TextureRegion[] treasure = new TextureRegion[10];
	
	public Animation[] getCharAnim()
	{
		return character.getAnimations();
	}
	
	public Animation[] getMummyAnim()
	{
		return mummy.getAnimations();
	}
	
	public TextureRegion[] getTreasureImages()
	{
		// Get animation images and split into 2 dim array
		TextureRegion images[][] = TextureRegion.split(new Texture(Gdx.files.internal("data/TreasureSprites.png")), Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
		
		// load texture array
		for(int i =0; i <4 ; i++)
		{
			treasure[i] = images[0][i];
		}
		return treasure;
	}

}
