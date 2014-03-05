package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class KeyRegister 
{
	// collection of keys on this levels
	private Array<Key> keySites = new Array<Key>();
	
	// collection of keys collected each time of checking
	private Array<Key> collectedKeys = new Array<Key>();
	
	// reference to levelMap
	private LevelMap levelMap;
	
	// treasure images
	private TextureRegion[] keyImages;
	
	// sounds
	private Sound collectsound;
	
	public KeyRegister(LevelMap levelMap, TextureRegion[] keyImages)
	{
		this.levelMap = levelMap;
		this.keyImages = keyImages;
		// collect sound
		collectsound = Gdx.audio.newSound(Gdx.files.internal("data/Collect4.mp3"));
	}
	
	public void init()
	{
		// clear collection out
		keySites.clear();
		
		int block;
		int blockProperty;
		// look though the map for treasure
		for(int x = 0; x < levelMap.getMapLengthBlocks(); x++)
		{
			for(int y = 0; y < levelMap.getMapHeightBlocks(); y++)
			{
				// get block details
				block = levelMap.Cell(x, y);
				// check for an alcove block
				if (block == Constant.BLOCKVALUES.ALCOVEUP.getValue())
				{
					// check if alcove contains treasure and if so what type
					blockProperty = levelMap.cellProperties(x, y);
					if (blockProperty >= Constant.KEY_TYPE.KEY1.getValue() && blockProperty <= Constant.KEY_TYPE.KEY10.getValue())
					{
						Vector2 position = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
						Rectangle bounds = new Rectangle(position.x -3, position.y + Constant.BLOCK_SIZE - 3, Constant.BLOCK_SIZE + 6, Constant.BLOCK_SIZE + 6);
						keySites.add(new Key(position, keyImages, blockProperty-11, getKeyValue(blockProperty), bounds));
					}
				}
				if (block == Constant.BLOCKVALUES.ALCOVERH.getValue())
				{
					// check if alcove contains treasure and if so what type
					blockProperty = levelMap.cellProperties(x, y);
					if (blockProperty >= Constant.KEY_TYPE.KEY1.getValue() && blockProperty <= Constant.KEY_TYPE.KEY10.getValue())
					{
						Vector2 position = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
						Rectangle bounds = new Rectangle(position.x + Constant.BLOCK_SIZE - 3, position.y - 3, Constant.BLOCK_SIZE + 6, Constant.BLOCK_SIZE + 6);
						keySites.add(new Key(position, keyImages, blockProperty -11, getKeyValue(blockProperty), bounds));
					}
				}
				if (block == Constant.BLOCKVALUES.ALCOVEDW.getValue())
				{
					// check if alcove contains treasure and if so what type
					blockProperty = levelMap.cellProperties(x, y);
					if (blockProperty >= Constant.KEY_TYPE.KEY1.getValue() && blockProperty <= Constant.KEY_TYPE.KEY10.getValue())
					{
						Vector2 position = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
						Rectangle bounds = new Rectangle(position.x -3, position.y - Constant.BLOCK_SIZE - 3, Constant.BLOCK_SIZE + 6, Constant.BLOCK_SIZE + 6);
						keySites.add(new Key(position, keyImages, blockProperty-11, getKeyValue(blockProperty), bounds));
					}
				}
				if (block == Constant.BLOCKVALUES.ALCOVELH.getValue())
				{
					// check if alcove contains treasure and if so what type
					blockProperty = levelMap.cellProperties(x, y);
					if (blockProperty >= Constant.KEY_TYPE.KEY1.getValue() && blockProperty <= Constant.KEY_TYPE.KEY10.getValue())
					{
						Vector2 position = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
						Rectangle bounds = new Rectangle(position.x - Constant.BLOCK_SIZE - 3, position.y - 3, Constant.BLOCK_SIZE + 6, Constant.BLOCK_SIZE + 6);
						keySites.add(new Key(position, keyImages, blockProperty-11, getKeyValue(blockProperty), bounds));
					}
				}
			}
		}
	}
	
	public Array<Key> collectKey(Rectangle bounds)
	{
	// clear the collection before checking for new keys collected
		collectedKeys.clear();
		
		// temp variable
		Key tempkey;
		
		for(Key sites: keySites)
		{
			tempkey = sites.collectKey(bounds);
			if (tempkey != null)
			{
				collectedKeys.add(tempkey);
			}
		}
		
		if (collectedKeys.size > 0)
		{
			collectsound.play();
		}
		
		return collectedKeys;
	}
	
	public void draw(SpriteBatch batch)
	{
		for(Key sites: keySites)
		{
			sites.draw(batch);
		}
	}
	
	private int getKeyValue(int blockProperty)
	{
		for(Constant.KEY_TYPE key : Constant.KEY_TYPE.values()) {
	        if (key.getValue() == blockProperty) return key.getKeyValue();
	     }
		return 0;
	}
}
