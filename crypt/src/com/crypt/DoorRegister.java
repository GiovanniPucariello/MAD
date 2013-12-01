package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class DoorRegister 
{
	// collection of keys on this levels
	private Array<Door> doors= new Array<Door>();
	
	// reference to levelMap
	private LevelMap levelMap;
	
	// door images
	private TextureRegion closedImage;
	private Animation[] animation;
	
	// sounds
	private Sound opensound;
	
	public DoorRegister(LevelMap levelMap, TextureRegion closedImage, Animation[] animations)
	{
		this.levelMap = levelMap;
		this.closedImage = closedImage;
		this.animation = animations;
		// collect sound
		opensound = Gdx.audio.newSound(Gdx.files.internal("data/OpeningDoor.mp3"));
	}
	
	public void init()
	{
		// clear collection out
		doors.clear();
		
		int blockProperty;
		// look though the map for treasure
		for(int x = 0; x < levelMap.getMapLengthBlocks(); x++)
		{
			for(int y = 0; y < levelMap.getMapHeightBlocks(); y++)
			{
				// get block details
				blockProperty = levelMap.cellProperties(x, y);
				
				// check if found a door block
				if (blockProperty >= Constant.DOORS.DOOR1.getValue() && blockProperty <= Constant.DOORS.DOOR10.getValue())
				{
					// As doors span 2 x 2 blocks check found bottom left corner
					if (levelMap.cellProperties(x-1, y) != blockProperty && levelMap.cellProperties(x, y-1) != blockProperty)
					{
						// found bottom left corner of a set of 2 x 2 door blocks
						Vector2 position = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
						doors.add(new Door(position, closedImage, animation, getKeyValue(blockProperty)));
					}
				}
			}
		}
	}
	
	public void pleaseOpenDoor(Rectangle character, Array<Key> keyCollection)
	{
		boolean doorsOpened = false;
		for (Door door : doors) {
			if(door.pleaseOpenDoor(character, keyCollection)) doorsOpened = true;
		}

		if (doorsOpened == true) {
			opensound.play();
		}
	}
	
	public void draw(SpriteBatch batch, float deltatime)
	{
		for(Door sites: doors)
		{
			sites.draw(batch, deltatime);
		}
	}
	
	private int getKeyValue(int blockProperty)
	{
		for(Constant.DOORS key : Constant.DOORS.values()) {
	        if (key.getValue() == blockProperty) return key.getKeyValue();
	     }
		return 0;
	}

	public boolean isDoorOpen(int x, int y) 
	{
		for(Door door : doors)
		{
			if (door.contains(x,y))
			{
				return door.isOpen(x, y);
			}
		}
		return false;
	}
}
