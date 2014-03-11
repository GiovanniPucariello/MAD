package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class SummaryMenu 
{
		private TextureRegion parchment, summaryLabelWon, summaryLabelLost, scoreLabel, timeBonusLabel, livesBonusLabel, summaryLabel;
		private final int MENU_WIDTH = 400;
		float labelTitleX, labelTitleY;
		private BitmapFont scoreFont, timeFont;
	    
		public SummaryMenu() 
		{
			// get file form disk 
			parchment = new TextureRegion(new Texture(Gdx.files.internal("data/parchment.png")));
			summaryLabel = new TextureRegion(new Texture(Gdx.files.internal("data/GameOver.png")));
			summaryLabelWon = new TextureRegion(new Texture(Gdx.files.internal("data/GameWon.png")));
			summaryLabelLost = new TextureRegion(new Texture(Gdx.files.internal("data/GameLost.png")));
			scoreLabel = new TextureRegion(new Texture(Gdx.files.internal("data/Score.png")));
			timeBonusLabel = new TextureRegion(new Texture(Gdx.files.internal("data/Time.png")));
			livesBonusLabel = new TextureRegion(new Texture(Gdx.files.internal("data/LivesBonus.png")));
			
			scoreFont = new BitmapFont();
			timeFont = new BitmapFont();
		}

		public void draw(SpriteBatch batch, Rectangle viewport, Boolean hasWon) 
		{
			float centerX = viewport.x + (viewport.width/2);		//Center of Display's X axis
			float centerY = viewport.y + (viewport.height/2);		//Center of Display's Y axis
			
			float parchHeight = viewport.height - 100;			//Set Height of Pause Menu Background
			float parchWidth = MENU_WIDTH;						//Set Width of Pause Menu Background
			//float parchWidth = viewport.width - 200;
			
			batch.draw(parchment, centerX -(parchWidth/2), centerY-(parchHeight/2), parchWidth, parchHeight);	//draw background
			labelTitleX = (centerX - (summaryLabel.getRegionWidth()/ 2));		//Set x co-ordinates for Title Label
			labelTitleY = (centerY - 100 + (parchHeight/2));					//set Y co-ordinates for Title Label
			batch.draw(summaryLabel, labelTitleX, labelTitleY);												//draw Title
		
			float labelX = (centerX - (parchWidth/2));
			float labelY = labelTitleY - 50;	
			batch.draw(scoreLabel, labelX, labelY);
			drawScore(batch, labelX + scoreLabel.getRegionWidth(), labelY);
			labelY = labelY- scoreLabel.getRegionHeight();
			batch.draw(timeBonusLabel, labelX, labelY);
			drawTime(batch, labelX + timeBonusLabel.getRegionWidth(), labelY);
			labelY = labelY- timeBonusLabel.getRegionHeight();
			batch.draw(livesBonusLabel, labelX + 10, labelY);
			drawLives(batch, labelX + livesBonusLabel.getRegionWidth() + 10, labelY);
			
			labelY = labelY - 100;
			if (hasWon)
			{
				labelTitleX = (centerX - (summaryLabelWon.getRegionWidth()/ 2));		//Set x co-ordinates for Title Label
				batch.draw(summaryLabelWon, labelTitleX, labelY);												//draw Title
			}
			else
			{
				labelTitleX = (centerX - (summaryLabelLost.getRegionWidth()/ 2));		//Set x co-ordinates for Title Label
				batch.draw(summaryLabelLost, labelTitleX, labelY);												//draw Title
			}
			//System.out.println("Summary Called");	
		}
		
		void drawScore(SpriteBatch batch, float widthPosition, float y)
		{
			String displayScore = "" + StatusBar.score;
			scoreFont.setColor(0.824f, 0.706f, 0.549f, 1.0f);		//Oppacity of Text ALWAYS the same.
			scoreFont.draw(batch, displayScore ,widthPosition,y+ 40);
		}
		void drawTime(SpriteBatch batch, float widthPosition, float y)
		{
			//split up time for Display.
			String displayTime = StatusBar.displayTime;
			
			timeFont.setColor(0.824f, 0.706f, 0.549f, 1.0f);		//Oppacity of Text ALWAYS the same.
			timeFont.draw(batch, displayTime ,widthPosition,y+40);
		}
		void drawLives(SpriteBatch batch, float widthPosition, float y)
		{
			//split up time for Display.
			String displayLives = "x" + Character.lives;
			
			timeFont.setColor(0.824f, 0.706f, 0.549f, 1.0f);		//Oppacity of Text ALWAYS the same.
			timeFont.draw(batch, displayLives,widthPosition,y+40);
		}
}
