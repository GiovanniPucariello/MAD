package com.crypt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;

public class LevelMap 
{
	private WorldController worldController;
	
	// Level collection of row collections of column collection e.g. cells
	private Array<Array<IntArray>> level;
	
	// Spawn site information (one line for all sites - 
	// contains (site id; trigger area x,y,width,height - in blocks; 
	// max creatures for is site; types of creatures - 1 = mummys, 2 = snake, 4 = bat, 6 = ghost)
	private Array<String> spawnSiteInfo = new Array<String>();
	
	// the current level
	private int thisLevel = 0;
	
	// the number of levels load
	private int numberLevels = -1;
	
	// dimensions of map (maximum level is 50)
	private int[] mapLength = new int[50];
	private int[] mapHeight = new int[50];
	
	// map difficulty
	private int[] mapDifficulty = new int[50];
	
	// character start position
	private Vector2[] charStartPoint = new Vector2[50];
	
	// us to check that the character is within a block space
	private Rectangle blockbounds = new Rectangle(0,0,Constant.BLOCK_SIZE+1, Constant.BLOCK_SIZE+1);
	
	// Door register used to check whether a cell can be entered
	private DoorRegister doorRegister;
	
	private Vector2 movementStep = new Vector2(0,0);
	
	LevelMap(WorldController worldController)
	{
		this.worldController = worldController;
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
				mapHeight[numberLevels] = Integer.valueOf(token.nextToken());
				mapHeight[numberLevels]--;
				
				// get level difficulty
				mapDifficulty[numberLevels] = Integer.valueOf(token.nextToken());
				
				// create the map storage to the size of the map
				createMap(mapLength[numberLevels]+1,mapHeight[numberLevels]+1);
				
				// read next line
				line = br.readLine();
				// store spawn site information
				spawnSiteInfo.add(line);
				
				// initialise loop to read cell data
				int y = mapHeight[numberLevels];
				
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
				
				mapHeight[numberLevels]++;
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
	
	public Array<SpawnSite> getSpawnSites(Animation[] animation, MonsterRegister monsterReg)
	{
		// create storage
		Array<SpawnSite> sites = new Array<SpawnSite>();
		
		// get info to parse
		String info = spawnSiteInfo.get(thisLevel);

		// Setup tokenizer to parse info
		StringTokenizer token = new StringTokenizer(info, ",");
		
		int siteid = 0;
		int triggerX = 0;
		int triggerY = 0;
		int width = 0;
		int heigth = 0;
		int maxCreatures = 0;
		int creatures = 0;
		Vector2 position = new Vector2(0,0);
		
		// parse site info into objects
		
		// Spawn site information (one line for all sites - 
		// contains (site id; trigger area x,y,width,height - in blocks; 
		// max creatures for is site; types of creatures - 1 = mummies, 2 = snake, 4 = bat)
		
		while(token.hasMoreTokens()) 
		{ 
			// read one set of site data
			siteid = Integer.valueOf(token.nextToken());
			triggerX = Integer.valueOf(token.nextToken());
			triggerY = Integer.valueOf(token.nextToken());
			width = Integer.valueOf(token.nextToken());
			heigth = Integer.valueOf(token.nextToken());
			maxCreatures = Integer.valueOf(token.nextToken());
			creatures = Integer.valueOf(token.nextToken());

			if (siteid != 0)
			{
				// find position on map
				position.set(findSpawnSite(siteid));
				Rectangle triggerBounds = new Rectangle(triggerX * Constant.BLOCK_SIZE, triggerY * Constant.BLOCK_SIZE, width * Constant.BLOCK_SIZE, heigth * Constant.BLOCK_SIZE );
				sites.add(new SpawnSite(siteid, triggerBounds, position, maxCreatures, creatures, animation, monsterReg));
			}
		}		
		
		return sites;
	}
	
	private Vector2 findSpawnSite(int id)
	{
		for(int x = 0; x < mapLength[thisLevel]; x++)
		{
			for(int y = 0; y < mapHeight[thisLevel]; y++)
			{				
				if (id == cellProperties(x,y))
				{
					Vector2 position = new Vector2(x * Constant.BLOCK_SIZE,y * Constant.BLOCK_SIZE);
					return position;
				}
			}
		}
		return null;		
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
	
	public void setDoorRegister(DoorRegister doorRegister)
	{
		this.doorRegister = doorRegister;
	}

	public int Cell(int x, int y)
	{
		if (x < 0 || x > mapLength[thisLevel] || y < 0 || y> mapHeight[thisLevel]) return 0;
		// get block details
		int block = level.get(thisLevel).get(x).get(y);
		
		// check if block is a transport or character start position and if so display a passage block
		if (block == Constant.BLOCKVALUES.TRANSPORT.getValue()) return Constant.BLOCKVALUES.PASSAGE.getValue();
		else if (block == Constant.BLOCKVALUES.CHAR_START_POINT.getValue()) return Constant.BLOCKVALUES.PASSAGE.getValue();
		
		// check if a treasure block range 30 to 69 (4 blocks up, right, down, left * 10 types of treasure
		else if (block > 29 && block < 71)
		{
			// alcove blocks are 7 = up, 8= rh, 9 = dw, 10 = lh
			block = (block - 30) % 4 + Constant.BLOCKVALUES.ALCOVEUP.getValue();
		}
		
		// check if block is a key block range 110 to 119 (4 blocks up, right, down, left * 10 keys)
		else if (block > 109 && block < 120)
		{
			// alcove blocks are 7 = up, 8= rh, 9 = dw, 10 = lh
			block = (block - 110) % 4 + Constant.BLOCKVALUES.ALCOVEUP.getValue();
		}
		
		// check if block is a door block range 120 to 129 (120 = key1, 121 = key2 etc.)
		else if (block >= 150 && block <= 159)
		{
			if (doorRegister.isDoorOpen(x,y) == false)
			{
				return Constant.BLOCKVALUES.PASSAGE_NO_WALK.getValue();
			} 
			else
			{
				return Constant.BLOCKVALUES.PASSAGE.getValue();
			}
		}
		
		// check if block is a spawn site
		else if (block >= 170 && block <= 190)
		{
			return Constant.BLOCKVALUES.PASSAGE.getValue();
		}
		
		// next level
		else if (block == Constant.BLOCKVALUES.NEXT_LEVEL.getValue()) return Constant.BLOCKVALUES.PASSAGE.getValue();
		
		return block;
	}
	
	public int cellProperties(int x, int y)
	{
		if (x < 0 || x>mapLength[thisLevel]-1 || y<0 || y>mapHeight[thisLevel]-1 )
				return 0;
		// get block details
		int block = level.get(thisLevel).get(x).get(y);
				
		// check if a treasure type block
		if (block >= 30 && block <= 70)
		{
			// return 1 - 10 for the treasure types
			block = (int) (block - 30) / 4 + 1;
			return block;
		}
		
		
		// check if a key type block
		else if (block >= 110 && block <= 149)
		{
			// return 11 - 20 for the key value
			block = (int) (block - 110) / 4 + 11;
			return block;
		}
		
		// check if a door block
		else if (block >= 150 && block <= 159)
		{
			// return 21 - 30 for the key value
			block = (int) (block - 150) + 21;
			return block;
		}
				
		// return 100 SPAWN_SITE if found
		else if (block >= 170 && block <= 190) return block;
		
		// next level
		else if (block == Constant.BLOCKVALUES.NEXT_LEVEL.getValue()) return block;
		return 0;
	}
	
	public int getMapLengthBlocks()
	{
		return mapLength[thisLevel];
	}
	
	public int getMapHeightBlocks()
	{
		return mapHeight[thisLevel];
	}
	
	public int getDifficulty()
	{
		return mapDifficulty[thisLevel];
	}
	
	float getMapLengthPixels()
	{
		return mapLength[thisLevel] * Constant.BLOCK_SIZE;
	}
	
	float getMapHeightPixels()
	{
		return mapHeight[thisLevel] * Constant.BLOCK_SIZE;
	}
	
	public boolean canIMove(Rectangle bounds, Vector2 movement, Animation[] animation, boolean transparent)
	{
		float x, y, xstep, ystep;
		int loop = 0;
		x = movement.x;
		y = movement.y;
		
		// check no movement
		if(x == 0 && y== 0) return true;
		
		// ensure numbers are positive
		if (x < 0) x*= -1;
		if (y < 0) y*= -1;
		
		if (x == 0)
		{
			xstep = 0;
			ystep = 1;
			loop = (int) (y + 0.5f);
		}
		else if (y == 0)
		{
			xstep = 1;
			ystep = 0;
			loop = (int) (x + 0.5f);
		}
		else
		{
			if (x > y)
			{
				xstep = y / x;
				ystep = 1;
				loop = (int) (y + 0.5f); 
			}
			else
			{
				xstep = 1;
				ystep = x /y;
				loop = (int) (x + 0.5f);
			}
		}
		
		// convert back to negatives if required
		if (movement.x < 0) xstep *= -1;
		if (movement.y < 0) ystep *= -1;
		
		boolean collisionFlag = true;
		for(int i = 0; i < loop; i++)
		{
			movementStep.set(xstep, ystep);
			if (eachMove(bounds,movementStep, animation, transparent) == false)
			{
				collisionFlag = false;
			}
		}
		if (movementStep.x == 0) movement.x = 0;
		if (movementStep.y == 0) movement.y = 0;
		return collisionFlag;
	}
	
	private boolean eachMove(Rectangle bounds, Vector2 movement, Animation[] animation, boolean transparent)
	{
		boolean collisionFlag = true;
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
				collisionFlag = false;
			}
			// check if right edge of bounds has collisions 
			if (notPath(bounds.x, bounds.y, animation, transparent) || notPath(bounds.x, bounds.y + bounds.height, animation, transparent))
			{
				// cannot move this far from present position
				// adjust position to nearest block
				bounds.x = ((int) (bounds.x / Constant.BLOCK_SIZE) + 1) * Constant.BLOCK_SIZE;
				// correct the movement so the call code knows
				movement.x = 0;
				// flag to stop moving in this direction
				collisionFlag = false;
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
				collisionFlag = false;
			}
			// check if right edge of bounds has collisions
			if (notPath(bounds.x + bounds.width, bounds.y, animation, transparent) || notPath(bounds.x + bounds.width, bounds.y + bounds.height, animation, transparent))
			{
				// cannot move this far from present position
				// adjust position to nearest block
				bounds.x = ((int) (bounds.x / Constant.BLOCK_SIZE) + 1) * Constant.BLOCK_SIZE - bounds.width -1;
				// correct the movement so the call code knows
				movement.x = 0;
				// flag to stop moving in this direction
				collisionFlag = false;
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
				collisionFlag = false;
			}
			// check if bottom edge of bounds has collisions 
			if (notPath(bounds.x, bounds.y, animation, transparent) || notPath(bounds.x + bounds.width, bounds.y, animation, transparent))
			{
				// cannot move this far from present position
				// adjust position to nearest block
				bounds.y = ((int) (bounds.y / Constant.BLOCK_SIZE) + 1) * Constant.BLOCK_SIZE;
				// correct the movement so the call code knows
				movement.y = 0;
				// flag to stop moving in this direction
				collisionFlag = false;
			}
		}
		// check for moving up
		else if (movement.y > 0)
		{
			// adjust position
			bounds.y += movement.y;
			// check still on map;
			if (bounds.y > (mapHeight[thisLevel] * Constant.BLOCK_SIZE) - bounds.height - 1)
			{
				bounds.y = mapHeight[thisLevel] * Constant.BLOCK_SIZE - bounds.height - 1;
				// correct the movement so the call code knows
				movement.y = 0;
				// flag to stop moving in this direction
				collisionFlag = false;
			}
			// check if top edge of bounds has collisions
			if (notPath(bounds.x, bounds.y + bounds.height, animation, transparent) || notPath(bounds.x + bounds.width, bounds.y + bounds.height, animation, transparent))
			{
				// cannot move this far from present position
				// adjust position to nearest block
				bounds.y = ((int) (bounds.y / Constant.BLOCK_SIZE) + 1) * Constant.BLOCK_SIZE - bounds.height -1;
				// correct the movement so the call code knows
				movement.y = 0;
				// flag to stop moving in this direction
				collisionFlag = false;
			}
		}
		return collisionFlag;
	}
	
	private boolean notPath(float x, float y, Animation[] animation, boolean transparent)
	{
		// convert x & y to block references
		int xblock = (int) (x / Constant.BLOCK_SIZE);
		int yblock = (int) (y / Constant.BLOCK_SIZE);
		
		if (Cell(xblock, yblock) !=  Constant.BLOCKVALUES.PASSAGE.getValue() && animation != worldController.getGhostAnim()){
			return true;
		}
		else if (Cell(xblock, yblock) !=  Constant.BLOCKVALUES.PASSAGE.getValue() && animation == worldController.getGhostAnim() && transparent == false){
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
				// check the character has a direction
				if (movement.y !=0)
				{
					// check direction character was moving and direction to look for landing site
					int direction;
					if (movement.y > 0) direction = 1; else direction = -1;
					// check vertically for the next transport site
					yblock += direction;
					while (level.get(thisLevel).get(xblock).get(yblock) != Constant.BLOCKVALUES.TRANSPORT
							.getValue()) {
						yblock += direction;
						if (yblock > mapHeight[thisLevel] - 1)
							yblock = 0;
						if (yblock < 0)
							yblock = mapHeight[thisLevel] - 1;
					}
					// set character to the position of the landing site + one movement to ensure that he is outside the bounds of the transport site
					// and does not start on the return trip (endless loop)
					bounds.x = xblock * Constant.BLOCK_SIZE;
					bounds.y = yblock * Constant.BLOCK_SIZE + (movement.y * 2);
					// confirm transport
					return true;
				}
			}
		}
		return false;
	}
	
	public int getNumberLevels() {
		return numberLevels;
	}
}
