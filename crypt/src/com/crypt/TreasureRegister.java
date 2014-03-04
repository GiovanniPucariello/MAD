package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class TreasureRegister 
{
	// collection of treasure objects for this levels 
	private Array<Treasure> treasureSites = new Array<Treasure>();
	
	// reference to levelMap
	private LevelMap levelMap;
	
	// treasure images
	private TextureRegion[] treasureImages;
	
	// sounds
	private Sound collectsound;
	
	public TreasureRegister(LevelMap levelMap, TextureRegion[] treasureImages)
	{
		this.levelMap = levelMap;
		this.treasureImages = treasureImages;
		// collect sound
		collectsound = Gdx.audio.newSound(Gdx.files.internal("data/Collect4.mp3"));
	}
	
	public void init()
	{
		// clear collection out
		treasureSites.clear();
		
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
					if (blockProperty > Constant.TREASURE_TYPE.EMRALD.getValue() && blockProperty < Constant.TREASURE_TYPE.GOLD.getValue())
					{
						Vector2 position = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
						Rectangle bounds = new Rectangle(position.x -3, position.y + Constant.BLOCK_SIZE - 3, Constant.BLOCK_SIZE + 6, Constant.BLOCK_SIZE + 6);
						treasureSites.add(new Treasure(position, treasureImages, blockProperty-1, getPoints(blockProperty), bounds));
					}
				}
				if (block == Constant.BLOCKVALUES.ALCOVERH.getValue())
				{
					// check if alcove contains treasure and if so what type
					blockProperty = levelMap.cellProperties(x, y);
					if (blockProperty > Constant.TREASURE_TYPE.EMRALD.getValue() && blockProperty < Constant.TREASURE_TYPE.GOLD.getValue())
					{
						Vector2 position = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
						Rectangle bounds = new Rectangle(position.x + Constant.BLOCK_SIZE - 3, position.y - 3, Constant.BLOCK_SIZE + 6, Constant.BLOCK_SIZE + 6);
						treasureSites.add(new Treasure(position, treasureImages, blockProperty -1, getPoints(blockProperty), bounds));
					}
				}
				if (block == Constant.BLOCKVALUES.ALCOVEDW.getValue())
				{
					// check if alcove contains treasure and if so what type
					blockProperty = levelMap.cellProperties(x, y);
					if (blockProperty > Constant.TREASURE_TYPE.EMRALD.getValue() && blockProperty < Constant.TREASURE_TYPE.GOLD.getValue())
					{
						Vector2 position = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
						Rectangle bounds = new Rectangle(position.x -3, position.y - Constant.BLOCK_SIZE - 3, Constant.BLOCK_SIZE + 6, - Constant.BLOCK_SIZE - 6);
						treasureSites.add(new Treasure(position, treasureImages, blockProperty-1, getPoints(blockProperty), bounds));
					}
				}
				if (block == Constant.BLOCKVALUES.ALCOVELH.getValue())
				{
					// check if alcove contains treasure and if so what type
					blockProperty = levelMap.cellProperties(x, y);
					if (blockProperty > Constant.TREASURE_TYPE.EMRALD.getValue() && blockProperty < Constant.TREASURE_TYPE.GOLD.getValue())
					{
						Vector2 position = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
						Rectangle bounds = new Rectangle(position.x - Constant.BLOCK_SIZE - 3, position.y - 3, Constant.BLOCK_SIZE + 6, Constant.BLOCK_SIZE + 6);
						treasureSites.add(new Treasure(position, treasureImages, blockProperty-1, getPoints(blockProperty), bounds));
						
					}
				}
			}
		}
	}
	
	private int getPoints(int blockProperty)
	{
		for(Constant.TREASURE_TYPE treasure : Constant.TREASURE_TYPE.values()) {
	        if (treasure.getValue() == blockProperty) return treasure.getPoints();
	     }
		return 0;
	}
	
	public int collectTresasure(Rectangle bounds)
	{
		int points = 0;
		for(Treasure sites: treasureSites)
		{
			points += sites.collectTreasure(bounds);
		}
		
		if (points > 0)
		{
			collectsound.play();
			System.out.println("Points "+ points);
		}
		return points;
	}
	
	public void draw(SpriteBatch batch)
	{
		for(Treasure sites: treasureSites)
		{
			sites.draw(batch);
		}
	}
	
}
