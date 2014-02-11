package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.crypt.Constant.BLOCK;

public class Background 
{
	// LevelMap
	private LevelMap levelMap;
	
	// Images of Blocks
	private TextureRegion blocks [];
	
	Background(LevelMap levelMap)
	{
		this.levelMap = levelMap;
		
		// Create texture regions
		blocks = new TextureRegion[Constant.NUM_MAP_BLOCKS];
		
		// load each of the block textures
		int i = 0;
		for(BLOCK blockpaths: BLOCK.values())
		{
			// get file form disk based upon the constant defined
			blocks[i] = new TextureRegion(new Texture(Gdx.files.internal(blockpaths.getfilename())));
			i++;
		}
		
		if (Constant.PASSAGEWAY_COLOUR !=0) 
		{
			blocks[Constant.BLOCKVALUES.PASSAGE_NO_WALK.getValue()] = new TextureRegion(new Texture(Gdx.files.internal("data/Transparent.png")));
			blocks[Constant.BLOCKVALUES.PASSAGE.getValue()] = new TextureRegion(new Texture(Gdx.files.internal("data/Transparent.png")));
		}
	}
	
	void draw(SpriteBatch batch, Rectangle viewport)
	{
		// adjust the X and Y positions to start of the nearest block - modules of BLOCK_SIZE pixels
		float renderXPos = viewport.x - (viewport.x % Constant.BLOCK_SIZE);
		float renderYPos = viewport.y - (viewport.y % Constant.BLOCK_SIZE);
		
		// convert the X and Y position to which rows and columns of the map - modules of BLOCK_SIZE pixels
		int startCol = (int) (renderXPos / Constant.BLOCK_SIZE);
		int startRow = (int) (renderYPos / Constant.BLOCK_SIZE);

		// calculate the last column to render and 1 because there will normally 
		// be part of a column hang off the screen
		int lastColumn = (int)(viewport.width / Constant.BLOCK_SIZE) + 2 + startCol;
		
		// Deduct a column if lastColumn exceeds the map length
		if (lastColumn > levelMap.getMapLengthBlocks())
			lastColumn = levelMap.getMapLengthBlocks();
		
		// calculate the last row to render and 1 because there will normally 
		// be part of a row hang off the screen
		int lastRow = startRow + Constant.NUM_ROWS + 2;
		
		// Deduct a column if lastColumn exceeds the map length
				if (lastRow > levelMap.getMapHeightBlocks())
					lastRow = levelMap.getMapHeightBlocks();
				
		
		// loop across the map drawing end cell
		for(int col = startCol; col < lastColumn; col++)
		{
			float y = renderYPos;
			for (int row = startRow; row < lastRow; row++)
			{
				
				// Use the map cell index to reference the array of images
				
				batch.draw(blocks[levelMap.Cell(col, row)], renderXPos, y);

				y += Constant.BLOCK_SIZE;
			}
			renderXPos += Constant.BLOCK_SIZE;
		}		
	}
}
