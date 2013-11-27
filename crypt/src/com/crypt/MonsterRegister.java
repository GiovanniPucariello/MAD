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
	
	private Array<Entity> monsters;
	private Rectangle viewPort;
	
	public MonsterRegister(WorldController worldController, LevelMap levelMap, Animation[] animation, Rectangle viewPort)
	{
		// Create monster array
		this.monsters = new Array<Entity>();
		
		this.worldController = worldController;
		this.levelMap = levelMap;
		this.animation = animation;
		this.viewPort = viewPort;
		
		
		// Instantiate Mummy
		Mummy mummy = new Mummy(new Vector2(96,1200), animation, levelMap);
		monsters.add(mummy);
	}
	
	public void add(Entity monster)
	{	
		
	}
	
	public void update(float deltaTime)
	{
		//System.out.println("Updating MonsterReg");
		//Loop through array of monsters and update
		for (Entity monster : monsters)
		{
			monster.update(deltaTime, viewPort);
		} 
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
