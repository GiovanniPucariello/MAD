package com.crypt;

import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
	private Sound killedSound;
	private Rectangle charCollisionBounds;
	
	public Array<Entity> monsters;

	
	public MonsterRegister(WorldController worldController, LevelMap levelMap, Animation[] animation)
	{
		// Create monster array
		this.monsters = new Array<Entity>();
		
		killedSound = Gdx.audio.newSound(Gdx.files.internal("data/Splat.mp3"));
		
		this.worldController = worldController;
		this.levelMap = levelMap;
		this.animation = animation;
	}
	
	public void init()
	{
		monsters.clear();
	}
	
	public boolean checkAreaClear(Rectangle area)
	{
		boolean empty = true;
		for(Entity monster : monsters)
		{
			if (monster.collision(area)) empty = false;
		}
		return empty;
	}	
	
	public int numberOfCreatures(int spawnSite)
	{
		int count = 0;
		for(Entity monster : monsters)
		{
			if (monster.spawnSiteID == spawnSite) count++;
		}
		return count;
	}
	
	public void addMonster(int spawnSiteID,int creatureType, Vector2 position)
	{
		Entity creatureToAdd = null;
		switch (creatureType)
		{
		case 1 :	
			creatureToAdd = new Mummy(position, animation, levelMap, spawnSiteID);
			break;
		}
		
		// if creature selected add it
		if (creatureToAdd != null) monsters.add(creatureToAdd);
	}
	
	public void update(float deltaTime, Rectangle viewPort)
	{
		// get characters position
		charCollisionBounds = worldController.getCharacterCollisionBounds();
		
		// Loop through monsters and update
		// setup iterator so elements can be removed
		Iterator<Entity> iter = monsters.iterator();
				
		// check that an item is available
		while (iter.hasNext()) {
			Entity monster = iter.next();
					
			// update bullet
			monster.update(deltaTime, viewPort, monsters);
			
			// check if creature is dead
			if (monster.dead == true)
			{
				iter.remove();
			}
			else
			{
				// check if the monster has been off screen for the required time
				if (monster.removeFromGame() == true)
				{
					iter.remove();
				}
				else
				{
					// check if monster has catch the character
					if (monster.collision(charCollisionBounds) == true)
					{
						worldController.characterCaught();
					}
				}				
			}
		}
	}
	
	public void draw(SpriteBatch batch)
	{
		for(Entity monster: monsters)
		{
			monster.draw(batch);
		}
	}
	
	public boolean caughtCharacter(Vector2 position, int width, int height)
	{
		return false;
	}
	
	public boolean beenShot(Rectangle bounds)
	{
		boolean hitFlag = false;
		for (Entity monster: monsters)
		{
			if (monster.collision(bounds) == true)
			{
				monster.setHit(true);
				hitFlag = true;
			}
		}
		
		if (hitFlag == true)
		{
			// play creature killed sound
			killedSound.play();
		}
		
		return hitFlag;
	}
}
