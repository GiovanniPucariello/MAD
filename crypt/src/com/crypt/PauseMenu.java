package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class PauseMenu 
{
		private TextureRegion parchment, pausedLabel;
		private final int MENU_WIDTH = 400;
	    
		public PauseMenu() 
		{
			// get file form disk 
			parchment = new TextureRegion(new Texture(Gdx.files.internal("data/parchment.png")));
			pausedLabel = new TextureRegion(new Texture(Gdx.files.internal("data/Paused.png")));
		}

		public void draw(SpriteBatch batch, Rectangle viewport) 
		{
			float centerX = viewport.x + (viewport.width/2);		//Center of Display's X axis
			float centerY = viewport.y + (viewport.height/2);		//Center of Display's Y axis
			
			float parchHeight = viewport.height - 100;			//Set Height of Pause Menu Background
			float parchWidth = MENU_WIDTH;						//Set Width of Pause Menu Background
			//float parchWidth = viewport.width - 200;
			
			float labelPausedX = (centerX - (pausedLabel.getRegionWidth()/ 2));		//Set x co-ordinates for Pause Title Label
			float labelPausedY = (centerY - 100 + (parchHeight/2));					//set Y co-ordinates for Pause Title Label

			batch.draw(parchment, centerX -(parchWidth/2), centerY-(parchHeight/2), parchWidth, parchHeight);	//draw background
			batch.draw(pausedLabel, labelPausedX, labelPausedY);												//draw Title
			//System.out.println("PauseMenu Called");
		}
}
