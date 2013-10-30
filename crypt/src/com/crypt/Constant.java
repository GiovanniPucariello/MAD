package com.crypt;

public class Constant 
{
	// Camera view screen height
	public final static int SCREEN_HEIGHT = 768;
	
	// Number of rows always required on screen
	public final static int NUM_ROWS = 12;
	
	// Block size
	public final static int BLOCK_SIZE = 64;
	
	// Number of block used in map
	public final static int NUM_MAP_BLOCKS = 7;
	
	// Block types and filenames
	public static enum BLOCK
	{
		PASSAGE("data/PassageWay.png"), 
		MID("data/MazeBlockMid.png"),
		COLUMN_LEFT("data/MazePassageLH.png"),
		COLUMN_RIGHT("data/MazePassageRH.png"),
		END_RIGHT("data/MazeBlockRH.png"),
		END_LEFT("data/MazeBlockLH.png"),
		SINGLE("data/MazeBlockEnd.png");
		
		private String filename;
				
		private BLOCK(String filename)
		{
			this.filename = filename;
		}
		
		public String getfilename()
		{
			return filename;
		}
	}
	
}
