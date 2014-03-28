package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class PauseMenu 
{
		private TextureRegion parchment;
		private final int MENU_WIDTH = 500;
	    
		public PauseMenu() 
		{
			// get file form disk 
			parchment = new TextureRegion(new Texture(Gdx.files.internal("data/parchment.png")));
		}

		public void draw(SpriteBatch batch, Rectangle viewport) 
		{
			float scale = ((float)Constant.NUM_ROWS / 12);
			
			float centerX = viewport.x + (viewport.width/2);		//Center of Display's X axis
			float centerY = viewport.y + (viewport.height/2);		//Center of Display's Y axis
			
			float parchHeight = 375 * scale;						//Set Height of Pause Menu Background
			float parchWidth = MENU_WIDTH * scale;					//Set Width of Pause Menu Background

			batch.draw(parchment, centerX -(parchWidth/2), centerY-(parchHeight/2), parchWidth, parchHeight);	//draw background
		}
}
