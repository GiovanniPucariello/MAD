package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class BulletRegister {

	//private WorldController world;
	private LevelMap levelMap;
	private Array<Bullet> shots = new Array<Bullet>();
	private Animation[] animation = new Animation[5];
	private Rectangle viewPort;
	
	long lastFiredTime = 0;
    
    // use a Hit sound?
    private Sound fireSound;
    
	public BulletRegister(WorldController worldController, LevelMap levelMap, Animation[] animation, Rectangle viewPort)
	{
		this.levelMap = levelMap;
		this.animation = animation;
		this.viewPort = viewPort;
		
		fireSound = Gdx.audio.newSound(Gdx.files.internal("data/shot2.mp3"));//needs replacing.
	}
	
	public void init(){
		shots.clear();
	}
	
	public void add(Vector2 velocity, Vector2 position)
	{
		if(TimeUtils.nanoTime() - lastFiredTime > 1000000000)
		{
			fireSound.play();
			shots.add(new Bullet(position, animation, levelMap, velocity));
			lastFiredTime = TimeUtils.nanoTime();
		}
	}
	public void update(float deltaTime)
	{
		//stateTime += deltaTime; ???
		for(Bullet bullet: shots)
        {
			//System.out.println(shots.size);
			if(bullet.exploding == false)
			{
				bullet.update(deltaTime, viewPort);
			} 
			else {
				shots.removeValue(bullet, true);		//suitable for removal?
			}
        }
	}

	public void draw(SpriteBatch batch)
    {
            for(Bullet bullet: shots)
            {
                bullet.draw(batch);
                //System.out.println("bullet "+ bullet +": " + position);
            }
    }
}
