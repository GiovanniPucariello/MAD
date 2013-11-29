package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BulletRegister {

	private WorldController world;
	private LevelMap levelMap;
	private Array<Bullet> shots = new Array<Bullet>();
	private Texture bulletImages;
	private Animation[] animation = new Animation[5];
	private Rectangle viewPort;
	private Vector2 velocity;
	private Vector2 position;
	//protected Rectangle bounds;
	
	long lastFiredTime;
    
    // use a Hit sound?
    private Sound hitSound;
    
	public BulletRegister(WorldController worldController, LevelMap levelMap, Animation[] animation, Rectangle viewPort)
	{
		this.levelMap = levelMap;
		//this.bulletImages = bulletImages;
		this.animation = animation;
		this.viewPort = viewPort;
		
		hitSound = Gdx.audio.newSound(Gdx.files.internal("data/shot2.mp3"));//needs replacing.
	}
	
	public void init(){
		shots.clear();
	}
	
	public void add(Vector2 velocity, Vector2 position)
	{
		//Vector2 position = bulletPos;
		//Vector2 velocity = bulletDirection;
        //this.bounds = new Rectangle(position.x -3, position.y + Constant.BLOCK_SIZE - 3, Constant.BLOCK_SIZE + 6, Constant.BLOCK_SIZE + 6);
		hitSound.play();
		shots.add(new Bullet(position, animation, levelMap, velocity/*position, bulletImages,0, bounds, levelMap, velocity*/));
		//lastFiredTime = time.utils.nanoTime();
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
			} else {
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
