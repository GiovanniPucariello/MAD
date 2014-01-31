package com.crypt;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SpawnSite 
{
	// sites reference
	private int id;
	
	// trigger area
	private Rectangle triggerBounds = new Rectangle(0,0,0,0);
	
	// spawn area
	private Rectangle spawnArea = new Rectangle(0,0,0,0);
	
	// spawn position
	Vector2 position = new Vector2(0,0);
	
	// max creatures from this site in the game
	int maxCreatures;
	
	// spawn types that is site makes e.g. mummies, snakes etc.
	int spawnTypes;
	
	// flags if in the middle of spawning
	boolean spawningNow = false;
	
	// time laps between monsters
	float timeSinceLastMonster = 0;
	
	// random number generator
	Random randomGenerator = new Random();
	
	// Spawning Animation
	private Animation animation[]; 
	
	// animation timer
	float stateTime = 0;
	
	// Reference to monsterReg
	MonsterRegister monsterReg;
	
	// frame to render
	private TextureRegion currentFrame;
	
	// sounds
	private Sound spawnSound;
	
	// monster refer
	private Entity monsterRef;
	
	SpawnSite(int id, Rectangle trigger, Vector2 position, int maxCreatures, int spawnTypes, Animation[] animation, MonsterRegister monsterReg)
	{
		this.id = id;
		this.triggerBounds = trigger;
		this.maxCreatures = maxCreatures;
		this.spawnTypes = spawnTypes;
		this.animation = animation;
		this.monsterReg = monsterReg;
		this.position.set(position);
		// set a margin around the spawn are to ensure it clear before creating a new creature
		spawnArea.set(position.x , position.y , Constant.BLOCK_SIZE , Constant.BLOCK_SIZE );
		
		// spawn sound
		spawnSound = Gdx.audio.newSound(Gdx.files.internal("data/SpawnCreature.mp3"));
	}
	
	public void update(Rectangle viewPort, Rectangle characterBounds, float deltaTime)
	{
		if (spawningNow == false)
		{
			// check that the spawn site is on screen
			if (viewPort.overlaps(spawnArea))
			{
				// check that the character is within the trigger area
				if (triggerBounds.overlaps(characterBounds))
				{
					// Spawn if necessary
					Spawn(deltaTime);
				}
			}
		}
	}

	private void Spawn(float deltaTime) 
	{
		// update time
		timeSinceLastMonster += deltaTime * Constant.GAME_SPEED;

		// check if the spawn site is clear of creatures
		if (monsterReg.checkAreaClear(spawnArea))
		{
			// check if the number of entities in existence < maxCreatures allowed
			int currentCount = monsterReg.numberOfCreatures(id);
			if (currentCount < maxCreatures)
			{
				if (currentCount < maxCreatures / 3) 
				{
					timeSinceLastMonster = 0;
					spawningNow = true;
					spawnSound.play();
					int creatureType = picACreature();
					monsterRef = monsterReg.addMonster(id, creatureType,
							position, false);
				}
				else
				{
					if ((timeSinceLastMonster > 1 + (3*(currentCount / maxCreatures)))
							) // (currentCount / maxCreatures * 6) && (randomGenerator.nextInt(maxCreatures * 6) < currentCount + 1 * 2)
					{
						// reset timer
						timeSinceLastMonster = 0;
						spawningNow = true;
						spawnSound.play();
						int creatureType = picACreature();
						monsterRef = monsterReg.addMonster(id, creatureType,
								position, false);
					}
				}
			}
		}
	}
	
	private int picACreature()
	{
		// temporary 1 = mummy; 2 = snake; 4 = bat;
		int creatureType = randomGenerator.nextInt(3);
		
		if (creatureType == 0) 
		{	
			creatureType = 1;
		}
		else if (creatureType == 1)
		{ 
			creatureType = 2;
		}
		else if (creatureType == 2)
		{ 
			creatureType = 4;
		}
		
		return creatureType;
	}
	
	public void draw(SpriteBatch batch, float deltaTime)
	{
		if (spawningNow == true)
		{
			stateTime += deltaTime;
			// pick correct frame
			currentFrame = animation[0].getKeyFrame(stateTime, false);
			if (animation[0].isAnimationFinished(stateTime))
			{
				spawningNow = false;
				// add creature

				stateTime = 0;
				monsterRef.active = true;
			}
			// draw animation
			batch.draw(currentFrame, position.x, position.y);
		}
		
	}
	

}
