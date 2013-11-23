package com.crypt;

import com.badlogic.gdx.graphics.g2d.Animation;

public class Assets 
{
	// asset objects
	private EntityAnimations character = new EntityAnimations("data/ManSprites.png", 4, 0.1f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	private EntityAnimations mummy = new EntityAnimations("data/MummySprite.png",3,0.1f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	
	public Animation[] getCharAnim()
	{
		return character.getAnimations();
	}
	
	public Animation[] getMummyAnim()
	{
		return mummy.getAnimations();
	}

}
