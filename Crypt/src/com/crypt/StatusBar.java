package com.crypt;

import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class StatusBar 
{
	static int score;
	static String lblTime, displayTime;
	
	static float time=600;
	static float scoretime = time;
	
	private final int BAR_LENGTH = 500;
	
	private String[] hsName = new String[10];
	private String[] hsScore = new String[10];
	
	BitmapFont scoreFont;
	BitmapFont timeFont;
	
	//Screen Information bar textures
	private TextureRegion lives,background;
	
	StatusBar(TextureRegion lives, TextureRegion background)
	{
		this.lives = lives;
		this.background = background;
		scoreFont = new BitmapFont();
		timeFont = new BitmapFont();
		
		// reset variables
		score = 0;
		time = 600;
		scoretime = time;
	}
	
	public void init()
	{
		//reset variables
		score = 0;
		time = 600;
		scoretime = time;
	}
	
	public boolean timeLeft()
	{
		  time -= Gdx.app.getGraphics().getDeltaTime();;
		  //System.out.println(time);
		  if (time > 0)
			  {
			  return true;
			  }
		  else
			  {
			  return false;
			  }
	}
	
	public int getScore() {
		return score;
	}

	public static void updatePoints(int points)
	{
		score = score + points;
		if(score < 0) score = 0;
		scoretime = time;
	}
	
	void draw(SpriteBatch overlay, Rectangle viewport)
	{		
		int livesleft = Character.lives-Character.deaths;
		float centerX = viewport.x + (viewport.width/2);
		float barY = viewport.y + (viewport.height-64);
		float barX = centerX -(BAR_LENGTH/2);
		
		//System.out.println(centerX);
		//System.out.println("viewHeight: "+ viewport.height);
		//System.out.println("viewportY: "+viewport.y);
		//System.out.println("bar Y: " + barY);
		
		Color c = overlay.getColor();
		float oppacity = oppacity();

		//set oppacity.
		overlay.setColor(c.r, c.g, c.b, oppacity);
		
		drawBackground(overlay, barX, barY);
		
		barX += 20;
		for(int i = 0; i <livesleft; i++){
			drawLives(overlay, barX, barY);
			barX +=32;
		}
		barX +=100;

		drawScore(overlay, barX, barY);
		
		barX +=100;
		
		drawTime(overlay, barX, barY);
		
		//return oppacity to normal state.
		overlay.setColor(c.r, c.g, c.b, 1);
	}
	
	void drawBackground(SpriteBatch overlay, float widthPosition, float y)
	{
		overlay.draw(background,widthPosition,y);
	}
	
	void drawLives(SpriteBatch overlay, float widthPosition, float y)
	{
			overlay.draw(lives,widthPosition,y+ (lives.getRegionHeight()/2));		
	}
	
	void drawScore(SpriteBatch overlay, float widthPosition, float y)
	{
		String displayScore = ("Score: " + score);
		scoreFont.setColor(0.824f, 0.706f, 0.549f, 1.0f);		//Oppacity of Text ALWAYS the same.
		scoreFont.draw(overlay, displayScore ,widthPosition,y+ 40);
	}
	
	void drawTime(SpriteBatch overlay, float widthPosition, float y)
	{
		//split up time for Display.
		int minutes = (int) (time / 60);
		int seconds = (int) (time % 60);
		if (seconds < 10)
		{
			displayTime = (minutes + ":0" + seconds);
		}
		else
		{
			displayTime = (minutes + ":" + seconds);
		}
		lblTime = "Time: ";
		
		timeFont.setColor(0.824f, 0.706f, 0.549f, 1.0f);		//Oppacity of Text ALWAYS the same.
		timeFont.draw(overlay, lblTime + displayTime ,widthPosition,y+40);
	}
	
	float oppacity()
	{
		//Status bar becomes transparent after 5 seconds with out interaction.
		float transparency = 5 +(time - scoretime);
		
		if (transparency < 0.5)
		{
			transparency = 0.5f;
		}
		else if (transparency > 1)
		{
			transparency = 1.0f;
		}
//		System.out.println(transparency);
		return transparency;
	}
	

}
