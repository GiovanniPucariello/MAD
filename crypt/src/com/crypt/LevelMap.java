package com.crypt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
		return level.get(thisLevel).get(x).get(y);
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
}
