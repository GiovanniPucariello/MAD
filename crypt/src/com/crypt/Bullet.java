package com.crypt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity{

	public boolean exploding = false;

	public Bullet(Vector2 position, Animation[] animation, LevelMap levelMap, Vector2 bulletDirection) {
		super(position, animation, new Vector2(0,0), new Vector2(0,0),0);
		CHAR_SPEED = 600;
        this.velocity = bulletDirection;
        this.levelMap = levelMap;
        this.bounds = new Rectangle(position.x, position.y, 16,16);
	}
    
	@Override
	public void changeDirection() 
	{
		exploding = true;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}

	@Override
	void randomlyChangeDirection() {
		// TODO Auto-generated method stub
		
	}
}
