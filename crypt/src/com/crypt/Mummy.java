package com.crypt;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Mummy extends Entity
{	
	Random randomGenerator = new Random();
	
	public Mummy(Vector2 position, Animation[] animation, LevelMap levelMap)
	{	
		super(position, animation);
				
		CHAR_SPEED = 150;
		
		
		velocity.y = -1;
		
		this.levelMap = levelMap;
	}
	
	public void changeDirection()
	{
		velocity.x = randomGenerator.nextInt(3)-1;
		velocity.y = randomGenerator.nextInt(3)-1;
	}
}
