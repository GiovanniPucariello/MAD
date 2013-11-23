package com.crypt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

public class LevelMap 
{
	// Level collection of row collections of column collection e.g. cells
	private Array<Array<IntArray>> level;
	
	// the current level
	private int thisLevel = 0;
	
	// the number of levels load
	private int numberLevels = -1;
	
	// length of map (maximum level is 50)
	private int[] mapLength = new int[50];
	private int[] mapHeigth = new int[50];
	
	// character start position
	private Vector2[] charStartPoint = new Vector2[50];
	
	// us to check that the character is within a block space
	private Rectangle blockbounds = new Rectangle(0,0,Constant.BLOCK_SIZE+1, Constant.BLOCK_SIZE+1);
	
	LevelMap()
	{
		// Open level data		
		FileHandle file = Gdx.files.internal("data/DemoLevel.csv");
		BufferedReader br = null;
		String line = "";
	 
		try {
			// setup buffer
			br = file.reader(400);
			while ((line = br.readLine()) != null)
			{
				// increment the level
				numberLevels++;
				
				// First line has the length of the map; parse it
				StringTokenizer token = new StringTokenizer(line, ",");
				
				// get number of columns
				mapLength[numberLevels] = Integer.valueOf(token.nextToken());
				
				// get number of rows
				mapHeigth[numberLevels] = Integer.valueOf(token.nextToken());
				mapHeigth[numberLevels]--;
				
				// create the map storage to the size of the map
				createMap(mapLength[numberLevels]+1,mapHeigth[numberLevels]+1);
				
				// initialise loop to read cell data
				int y = mapHeigth[numberLevels];
				
				// until end of file or all row have been read
				while ((line = br.readLine()) != null && y > -1)
				{
					token = new StringTokenizer(line, ",");
					int x = 0;
					
					// parse each cell
					while(token.hasMoreTokens() && x < mapLength[numberLevels]) 
					{ 
						storeCell(numberLevels,x,y,Integer.valueOf(token.nextToken()));
						x++;
					}
					y--;
				}
				mapHeigth[numberLevels]++;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void createMap(int x, int y)
	{
		// check if 
		if (level == null)
		{
			level = new Array<Array<IntArray>>();
		}
		
		// add new row collection
		Array<IntArray> row = new Array<IntArray>(x);
		level.add(row);
		
		// add each column to the row collection
		for (int rowCount = 0; rowCount < x; rowCount++)
		{
			// create initialise and add each cell
			IntArray col = new IntArray(y);
			for (int i=0; i < y; i++)
			{
				// default value of 0 - path block
				col.add(0);
			}
			row.add(col);
		}
	}
	
	
	// store a cell value in collection already setup
	private void storeCell(int levelNum, int x, int y, int value)
	{
		level.get(levelNum).get(x).set(y, value);
		
		// cell is a start position for the character on this level then store it
		if (value == Constant.BLOCKVALUES.CHAR_START_POINT.getValue())
		{
			charStartPoint[levelNum] = new Vector2(x * Constant.BLOCK_SIZE, y * Constant.BLOCK_SIZE);
		}
	}
	
	// Change the map level
	public void setLevel(int level)
	{
		if (level > -1 && level <= numberLevels)
		{
			thisLevel = level;
		}
	}

	int Cell(int x, int y)
	{
		// get block details
		int block = level.get(thisLevel).get(x).get(y);
		
		// check if block is a transport or character start position and if so display a passage block
		if (block == Constant.BLOCKVALUES.TRANSPORT.getValue()) return Constant.BLOCKVALUES.PASSAGE.getValue();
		if (block == Constant.BLOCKVALUES.CHAR_START_POINT.getValue()) return Constant.BLOCKVALUES.PASSAGE.getValue();
		
		return block;
	}
	
	int getMapLengthBlocks()
	{
		return mapLength[thisLevel];
	}
	
	int getMapHeightBlocks()
	{
		return mapHeigth[thisLevel];
	}
	
	float getMapLengthPixels()
	{
		return mapLength[thisLevel] * Constant.BLOCK_SIZE;
	}
	
	float getMapHeightPixels()
	{
		return mapHeigth[thisLevel] * Constant.BLOCK_SIZE;
	}
	
	public boolean canIMove(Rectangle bounds, Vector2 movement)
	{
		// Potential problem if the frame rate is slow.
		// A entity can move pass an opening in one frame and therefore a check for openings between start and moved to position should be added.
		
		boolean collisionFlag = false;
		// check for moving left
		if (movement.x < 0)
		{
			// adjust position
			bounds.x += movement.x;
			// check still on map
			if (bounds.x < 0)
			{
				// put back on the map
				bounds.x = 0;
				// correct the movement so the call code knows
				movement.x = 0;
				// flag to stop moving in this direction
				collisionFlag = true;
			}
			// check if right edge of bounds has collisions 
			if (notPath(bounds.x, bounds.y) || notPath(bounds.x, bounds.y + bounds.height))
			{
				// cannot move this far from present position
				// adjust position to nearest block
				bounds.x = ((int) (bounds.x / Constant.BLOCK_SIZE) + 1) * Constant.BLOCK_SIZE;
				// correct the movement so the call code knows
				movement.x = 0;
				// flag to stop moving in this direction
				collisionFlag = true;
			}
		}
		// check for moving right
		else if (movement.x > 0)
		{
			// adjust position
			bounds.x += movement.x;
			// check still on map;
			if (bounds.x > (mapLength[thisLevel] * Constant.BLOCK_SIZE) - bounds.width)
			{
				bounds.x = mapLength[thisLevel] * Constant.BLOCK_SIZE - bounds.width-1;
				// correct the movement so the call code knows
				movement.x = 0;
				// flag to stop moving in this direction
				collisionFlag = true;
			}
			// check if right edge of bounds has collisions
			if (notPath(bounds.x + bounds.width, bounds.y) || notPath(bounds.x + bounds.width, bounds.y + bounds.height))
			{
				// cannot move this far from present position
				// adjust position to nearest block
				bounds.x = ((int) (bounds.x / Constant.BLOCK_SIZE) + 1) * Constant.BLOCK_SIZE - bounds.width -1;
				// correct the movement so the call code knows
				movement.x = 0;
				// flag to stop moving in this direction
				collisionFlag = true;
			}
		}
		// check for moving down
		if (movement.y < 0)
		{
			// adjust position
			bounds.y += movement.y;
			// check still on map
			if (bounds.y < 0)
			{
				bounds.y = 0;
				// correct the movement so the call code knows
				movement.y = 0;
				// flag to stop moving in this direction
				collisionFlag = true;
			}
			// check if bottom edge of bounds has collisions 
			if (notPath(bounds.x, bounds.y) || notPath(bounds.x + bounds.width, bounds.y))
			{
				// cannot move this far from present position
				// adjust position to nearest block
				bounds.y = ((int) (bounds.y / Constant.BLOCK_SIZE) + 1) * Constant.BLOCK_SIZE;
				// correct the movement so the call code knows
				movement.y = 0;
				// flag to stop moving in this direction
				collisionFlag = true;
			}
		}
		// check for moving up
		else if (movement.y > 0)
		{
			// adjust position
			bounds.y += movement.y;
			// check still on map;
			if (bounds.y > (mapHeigth[thisLevel] * Constant.BLOCK_SIZE) - bounds.height - 1)
			{
				bounds.y = mapHeigth[thisLevel] * Constant.BLOCK_SIZE - bounds.height - 1;
				// correct the movement so the call code knows
				movement.y = 0;
				// flag to stop moving in this direction
				collisionFlag = true;
			}
			// check if top edge of bounds has collisions
			if (notPath(bounds.x, bounds.y + bounds.height) || notPath(bounds.x + bounds.width, bounds.y + bounds.height))
			{
				// cannot move this far from present position
				// adjust position to nearest block
				bounds.y = ((int) (bounds.y / Constant.BLOCK_SIZE) + 1) * Constant.BLOCK_SIZE - bounds.height -1;
				// correct the movement so the call code knows
				movement.y = 0;
				// flag to stop moving in this direction
				collisionFlag = true;
			}
		}
		return collisionFlag;
	}
	
	private boolean notPath(float x, float y)
	{
		// convert x & y to block references
		int xblock = (int) (x / Constant.BLOCK_SIZE);
		int yblock = (int) (y / Constant.BLOCK_SIZE);
		
		if (Cell(xblock, yblock) !=  Constant.BLOCKVALUES.PASSAGE.getValue()){
			return true;
		}
		return false;
	}
	
	public Vector2 getCharStartPoint()
	{
		return charStartPoint[thisLevel];
	}
	
	public boolean transportMe(Rectangle bounds, Vector2 movement)
	{
		// convert x & y to block references
		int xblock = (int) (bounds.x / Constant.BLOCK_SIZE);
		int yblock = (int) (bounds.y / Constant.BLOCK_SIZE);
		
		// check if the block at that location is a transport block
		if (level.get(thisLevel).get(xblock).get(yblock) == Constant.BLOCKVALUES.TRANSPORT.getValue())
		{
			// check that the character is fully within the block
			// note blockbounds is bigger than the block by 1 pixel margin
			blockbounds.x = xblock * Constant.BLOCK_SIZE - 1;
			blockbounds.y = yblock * Constant.BLOCK_SIZE - 1;
			
			// check character within the blocks boundaries
			if (blockbounds.contains(bounds))
			{
				// check direction character was moving and direction to look for landing site
				int direction;
				if (movement.y > 0 ) direction = 1; else direction = -1;
				
				// check vertically for the next transport site
				yblock += direction;
				while (level.get(thisLevel).get(xblock).get(yblock) != Constant.BLOCKVALUES.TRANSPORT.getValue())
				{
					yblock += direction;
					if (yblock > mapHeigth[thisLevel] -1) yblock =0;
					if (yblock < 0) yblock = mapHeigth[thisLevel] -1;
				}
				
				// set character to the position of the landing site + one movement to ensure that he is outside the bounds of the transport site
				// and does not start on the return trip (endless loop)
				bounds.x = xblock * Constant.BLOCK_SIZE + movement.x;
				bounds.y = yblock * Constant.BLOCK_SIZE + movement.y;
				
				// confirm transport
				return true;
			}
		}
		return false;
	}
}
