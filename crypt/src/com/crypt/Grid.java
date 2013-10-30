package com.crypt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Grid 
{
	// hold the map as a 2D array
	private int cell[][];
	
	// length of map
	private int mapLength;
	
	Grid()
	{
		FileHandle file = Gdx.files.internal("data/DemoLevel.csv");
		BufferedReader br = null;
		String line = "";
	 
		try {
			br = file.reader(400);
			while ((line = br.readLine()) != null)
			{
				// First line has the length of the map
				StringTokenizer token = new StringTokenizer(line, ",");
				mapLength = Integer.valueOf(token.nextToken());
				mapLength--;
				// create the array to the size of the map
				cell = new int[mapLength][Constant.NUM_ROWS+1];
				int y = 11;
				while ((line = br.readLine()) != null && y < Constant.NUM_ROWS)
				{
					token = new StringTokenizer(line, ",");
					int x = 0;
					while(token.hasMoreTokens() && x < mapLength) 
					{ 
						cell[x][y] = Integer.valueOf(token.nextToken());
						x++;
					}
					y--;
				}
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
	
	int Cell(int x, int y)
	{
		return cell[x][y];
	}
	
	int getMapLength()
	{
		return mapLength;
	}
}
