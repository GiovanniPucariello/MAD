package com.crypt;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class BulletRegister {

	//Levelmap used by each bullet
	private LevelMap levelMap;
	
	// Monster Register used for detecting creature kills
	private MonsterRegister monsterRegister;
	
	// collection of bullets fired
	private Array<Bullet> shots = new Array<Bullet>();
	
	// animation for the bullets
	private Animation[] animation = new Animation[4];

	// time of last fired bullet - used to throttle fire rate
	long lastFiredTime = 0;
    
    // Sounds
    private Sound fireSound;
    private Sound hitWall;
    
	public BulletRegister(WorldController worldController, LevelMap levelMap, MonsterRegister monsterRegister, Animation[] animation)
	{
		this.levelMap = levelMap;
		this.animation = animation;
		this.monsterRegister = monsterRegister;
		
		fireSound = Gdx.audio.newSound(Gdx.files.internal("data/Shot.mp3"));
		hitWall = Gdx.audio.newSound(Gdx.files.internal("data/ImpactedWall.mp3"));
	}
	
	public void init(){
		shots.clear();
	}
	
	public void add(Vector2 velocity, Vector2 position)
	{
		// check if the character can fire again
		if(TimeUtils.nanoTime() - lastFiredTime > 250000000)
		{
			fireSound.play();
			
			// fire from centre of character
			Vector2 firefrom = new Vector2 (position.x + Constant.BLOCK_SIZE/2, position.y + Constant.BLOCK_SIZE/2);
			
			// adjust position to the edge of the character
			firefrom.add(Constant.BLOCK_SIZE/2 * velocity.x - 6, Constant.BLOCK_SIZE/2 * velocity.y -6);

			// add shot
			shots.add(new Bullet(firefrom, animation, levelMap, velocity));
			lastFiredTime = TimeUtils.nanoTime();
		}
	}
	
	public void update(float deltaTime, Rectangle viewPort)
	{
		// setup iterator so elements can be removed
		Iterator<Bullet> iter = shots.iterator();
		
		// check that an item is available
		while (iter.hasNext()) {
			Bullet bullet = iter.next();
			
			// update bullet
			bullet.update(deltaTime, viewPort);
			
			// check if hit wall
			if (bullet.exploding == true)
			{
				iter.remove();
				hitWall.play();
			}
			else
			{
				// check if bullet has hit a creature
				if (monsterRegister.beenShot(bullet.getBounds()) == true)
				{
					iter.remove();
				}
			}
		}
	}

	public void draw(SpriteBatch batch)
    {
            for(Bullet bullet: shots)
            {
                bullet.draw(batch);
            }
    }
}
