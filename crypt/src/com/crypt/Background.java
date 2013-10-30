package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.crypt.Constant.BLOCK;

public class Background 
{
	// Images of Blocks
	private TextureRegion blocks [];
	
	// The overall size of the screen in pixels
	private float screenWidth;
	
	// size of the screen in cells / blocks
	private float screenWidthBlocks;

	// size of each cell block
	private int blockSize;
	
	// map class for grid layout
	private Grid map;

	// starting column to render the current background from
	private int startCol;
	// the pixel offset from the first column to render the current background from
	private float colOffset;
	// the offset that is required to render the last block at the far right hand side of the screen
	private float finalOffset;
	
	Background(float width, float height)
	{
		// Create the map grid
		map = new Grid();
		
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
				
		screenWidth = width;
						
		// calculate the screen width in blocks based upon the size of the blocks
		blockSize = Constant.BLOCK_SIZE; // blocks[0].getRegionWidth(); 
		screenWidthBlocks = (width / blockSize);
		
		// screenWidthBlock could be a decimal and we need next whole numbers
		if (screenWidthBlocks != (int)screenWidthBlocks) 
		{
			finalOffset = (screenWidthBlocks - (int)screenWidthBlocks) * blockSize;
			screenWidthBlocks=(int) screenWidthBlocks++;
		}
		System.out.println("screenWidthBlocks :"+screenWidthBlocks);
	}
	
	/* Sets the x position for the centre of screen of the grid
	 */
	void setXPos(float x)
	{
		//TODO need to revisit this method and thing about what happens if the grid is shorter than the screen width
		
		// check if the x position is less than the centre of the screen
		if (x < screenWidth / 2) 
		{
			x = 0;
		} else
		{
			x = x - (screenWidth / 2);
		}
		
		// check x is not beyond half the screen from the end of the map
		if (x > (map.getMapLength() * blockSize) - (screenWidth/2)) x = (map.getMapLength() * blockSize) - (screenWidth/2);
				
		// calculate the column and pixel offset with the column
		startCol = (int)(x / blockSize);
		colOffset = x % blockSize;
	}
	
	float lengthPixels()
	{
		return map.getMapLength() * blockSize;
	}
		
	void draw(SpriteBatch batch)
	{
		// calculate the last column to render
		int lastColumn = (int)screenWidthBlocks + startCol + 1;
		
		// if colOffset is not 0 then render and extra column
		// because the columns are hang off the screen at each end
		if (colOffset != 0 && lastColumn < map.getMapLength()) 
		{
			lastColumn++;
		} 
		else if (lastColumn > map.getMapLength())
		{
			lastColumn = map.getMapLength();
			colOffset = -finalOffset;
		}
		
		// adjust the rendering start position 
		float x = -colOffset;
		
		// loop across the map array drawing end cell
		for(int col = startCol; col < lastColumn; col++)
		{
			float y = 0;
			for (int row = 0; row < Constant.NUM_ROWS; row++)
			{
				// Use the map cell index to reference the array of images
				batch.draw(blocks[map.Cell(col, row)], x, y);

				y += blockSize;
			}
			x += blockSize;
		}
	}
}
