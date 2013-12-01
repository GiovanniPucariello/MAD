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
		super(position, animation, new Vector2(8,2), new Vector2(-8,-2));
				
		CHAR_SPEED = 100;
		
		//Set velocity of mummy at spawn, if 0,0: do it again.
		velocity.x = randomGenerator.nextInt(2)-1;
		velocity.y = randomGenerator.nextInt(2)-1;		
		
		while (velocity.x == 0.0 && velocity.y == 0.0) 
		{
			velocity.x = randomGenerator.nextInt(3)-1;
			velocity.y = randomGenerator.nextInt(3)-1;
		}
		
		this.levelMap = levelMap;
	}
	
	@Override
	public void changeDirection() //Set velocity of mummy on collision with wall, if 0,0: do it again.	
	{
		velocity.x = randomGenerator.nextInt(3)-1;
		velocity.y = randomGenerator.nextInt(3)-1;
		
		while (velocity.x == 0.0 && velocity.y == 0.0) 
		{
			velocity.x = randomGenerator.nextInt(3)-1;
			velocity.y = randomGenerator.nextInt(3)-1;
		}
	}

	@Override
	void randomlyChangeDirection() {
		// TODO Auto-generated method stub
		
	}
}
