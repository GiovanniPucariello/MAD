package com.crypt;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends Entity{

	public boolean exploding = false;
	float stateTime = 0f;

	public Bullet(Vector2 position, Animation[] animation, /*Texture image, int imageIndex, Rectangle bounds,*/ LevelMap levelMap, Vector2 bulletDirection) {
		super(position, animation);
		
		CHAR_SPEED = 300;
        this.velocity = bulletDirection;
        this.levelMap = levelMap;
        this.bounds = new Rectangle(position.x, position.y, 16,16);
	}
    
	protected void changeDirection() {
		// update statetime? what is this?
		exploding = true;
		System.out.println("The Bullet has hit a wall");
	}	
}
