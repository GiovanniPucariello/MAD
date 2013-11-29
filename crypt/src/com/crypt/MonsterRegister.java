package com.crypt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MonsterRegister 
{
	private WorldController worldController;
	private LevelMap levelMap;
	private Animation[] animation = new Animation[5];
	
	public Array<Entity> monsters;
	private float timeSinceLastMonster = 0;
	
	public MonsterRegister(WorldController worldController, LevelMap levelMap, Animation[] animation)
	{
		// Create monster array
		this.monsters = new Array<Entity>();
		
		this.worldController = worldController;
		this.levelMap = levelMap;
		this.animation = animation;
		
		// Instantiate Mummy
		Mummy mummy = new Mummy(new Vector2(96,1200), animation, levelMap);
		monsters.add(mummy);
	}
	
	public void addMonster(float deltaTime)
	{
		timeSinceLastMonster += deltaTime;
		
		if (timeSinceLastMonster > 5.0f) 
		{
			Mummy mummy = new Mummy(new Vector2(96,1200), animation, levelMap);
			monsters.add(mummy);
			timeSinceLastMonster = 0;
		}
	}
	
	public void update(float deltaTime, Rectangle viewPort)
	{
		//Loop through array of monsters and update
		for (Entity monster : monsters)
		{
			monster.update(deltaTime, viewPort);
		} 
		
		//Add new monster to array
		addMonster(deltaTime);
		
		//Check if removal is needed.
		offScreenRemove();
	}
	
	public void draw(SpriteBatch batch)
	{
		for(Entity monster: monsters)
		{
			monster.draw(batch);
		}
	}
	
	private void offScreenRemove()
	{
		for (Entity monster: monsters)
		{
			//Check in Entity if 5 seconds has past since monster went off screen.
			if (monster.removeFromGame() == true)
			{
				System.out.println("Removing from array.");
				monsters.removeValue(monster, true);
			}
		}
	}
	
	public boolean caughtCharacter(Vector2 position, int width, int height)
	{
		return false;
	}
	
	public boolean beenShot(Vector2 position, int length)
	{
		return false;
	}
}
