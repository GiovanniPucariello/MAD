package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class touchPad {
	// unprojected screen touch position
	private Vector3 touchPoint = new Vector3();
	private Vector2 direction = new Vector2();
	private Character character;
	// Joystick Area
	public Circle touchPad = new Circle();
	public Circle touchPadLimit = new Circle();
	private Vector2 joystickMovement = new Vector2();
	public Vector2 touchPadCentre = new Vector2(0,0);
	
	// Joystick image
	private TextureRegion joystick;
	private float joystickSize = 192;
	
	Rectangle viewPort;
	
	touchPad(Character character)
	{
		this.character = character;
		// setup Joystick ready for possible use
		joystick = new TextureRegion(new Texture(Gdx.files.internal("data/TouchPad.png")));
	}
	
	public void size(float size)
	{
		touchPad.radius = size /2;
		joystickSize = size;
		
		// limit area around pad
		touchPadLimit.radius = size/2 + size/6;
	}
	
	public void viewport(Rectangle viewPort)
	{
		this.viewPort = viewPort;
	}
	
	public boolean contains(float x, float y)
	{
		return false;
	}
	
	public void draw(SpriteBatch batch)
	{
		batch.draw(joystick, touchPad.x-touchPad.radius, touchPad.y-touchPad.radius, joystickSize, joystickSize);
	}
	
	/*public void touchpadMovement(Vector3 touchPoint)
	{
		direction = renderer.joystickMovement(touchPoint.x, touchPoint.y);
		
		// horizontal movement
		if (direction.x < -renderer.touchPad.radius / 7) {
			character.moveRight();
		} else if (direction.x > renderer.touchPad.radius / 7) {
			character.moveLeft();
		} else {
			character.stopHoziontialMove();
		}

		// vertical movement
		if (direction.y < -renderer.touchPad.radius / 7) {
			character.moveUp();
		} else if (direction.y > renderer.touchPad.radius / 7) {
			character.moveDown();
		} else {
			character.stopVerticalMove();
		}	
	}*/
}
