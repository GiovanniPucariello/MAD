package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class SpawnSiteReg 
{
	// collection of keys on this levels
		private Array<SpawnSite> spawnSites;
		
		// reference to levelMap
		private LevelMap levelMap;
		
		// reference monsterReg
		MonsterRegister monsterReg;
		
		// Spawn Animation
		private Animation[] animation;
		
		public SpawnSiteReg(LevelMap levelMap, Animation[] animations, MonsterRegister monsterReg)
		{
			this.levelMap = levelMap;
			this.animation = animations;
			this.monsterReg = monsterReg;
			// setup sites
			spawnSites = levelMap.getSpawnSites(animation, monsterReg);	
		}
		
		public void init()
		{
			// clear any old site infor
			spawnSites.clear();
			spawnSites = levelMap.getSpawnSites(animation, monsterReg);			
		}
		
		public void update(Rectangle viewPort, float deltaTime, Rectangle characterBounds)
		{
			for(SpawnSite sites : spawnSites)
			{
				sites.update(viewPort, characterBounds, deltaTime);
			}
		}
		
		public void draw(SpriteBatch batch, float deltatime)
		{
			for(SpawnSite sites : spawnSites)
			{
				sites.draw(batch, deltatime);
			}
		}
}
