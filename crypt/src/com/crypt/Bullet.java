package com.crypt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity{

	public boolean exploding = false;

	public Bullet(Vector2 position, Animation[] animation, LevelMap levelMap, Vector2 bulletDirection) {
		super(position, animation);
		
		CHAR_SPEED = 300;
        this.velocity = bulletDirection;
        this.levelMap = levelMap;
        this.bounds = new Rectangle(position.x, position.y, 16,16);
	}
    
	protected void changeDirection() {
		exploding = true;
		System.out.println("The Bullet has hit a wall");
	}	
}
