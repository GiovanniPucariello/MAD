package com.crypt;

import com.badlogic.gdx.math.Vector2;

public class Character 
{
	// Characters speed
	private static int CHAR_SPEED = 200;
	
	// Characters velocity 0, 1 or -1
	private Vector2 velocity;
	
	// Characters position x,y
	private Vector2 position;
		
	private WorldController world;
	private LevelMap levelMap;
	
	Character(WorldController world, LevelMap levelMap)
	{
		this.world = world;
		this.levelMap = levelMap;
		velocity = new Vector2(0,0);
		position = new Vector2(640,384);
	}
	
	Vector2 getCharacterPosition()
	{
		return position;
	}
	
	void moveRight()
	{
		velocity.x = 1;
	}
	
	void moveLeft()
	{
		velocity.x = - 1;
	}
	
	void moveUp()
	{
		velocity.y = 1;
	}
	
	void moveDown()
	{
		velocity.y = -1;
	}
	
	void stopVerticalMove()
	{
		velocity.y = 0;
	}
	
	void stopHoziontialMove()
	{
		velocity.x =0;
	}
	
	void update(float deltaTime)
	{
		// update the character position by multiplying the velocity vector by time passed and adding it to the position.
		position.add(velocity.tmp().mul(deltaTime * CHAR_SPEED));
		
		// ensure the character is not outside the confines of the Map
		if (position.x < 0) position.x = 0;
		if (position.x > levelMap.getMapLengthPixels()) position.x = levelMap.getMapLengthPixels();
		if (position.y < 0) position.y = 0;
		if (position.y > levelMap.getMapHeightPixels()) position.y = levelMap.getMapHeightPixels();
	}

}
