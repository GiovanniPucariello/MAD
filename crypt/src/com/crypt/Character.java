package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Character 
{
	private static int CHAR_SPEED = 200;
	
	private Vector2 velocity;
	private float xPos;
	
	private Background background;
	
	Character(Background background)
	{
		this.background = background;
		xPos = 0;
		velocity = new Vector2(0,0);
	}
	
	void moveRight()
	{
		velocity = new Vector2(CHAR_SPEED,0);
	}
	
	void moveLeft()
	{
		velocity = new Vector2(-CHAR_SPEED,0);
	}
	
	void move()
	{
		float xMovement = velocity.x * Gdx.graphics.getDeltaTime();
		xPos += xMovement;
		if (xPos < (Crypt.screenLength/2)) xPos = Crypt.screenLength/2;
		if (xPos > background.lengthPixels() - (Crypt.screenLength/2)) xPos = background.lengthPixels() - (Crypt.screenLength/2);
		background.setXPos(xPos);
	}

}
