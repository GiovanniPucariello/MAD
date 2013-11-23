package com.crypt;

public class Constant 
{
	// Camera view screen height
	public final static float SCREEN_HEIGHT = 768;
	
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
	
	// numeric values for the index of the blocks
	// MUST BE keep in sync with the BLOCK enum above
	public static enum BLOCKVALUES
	{
		PASSAGE(0),
		MID(1),
		COLUMN_LEFT(2),
		COLUMN_RIGHT(3),
		END_RIGHT(4),
		END_LEFT(5),
		SINGLE(6),
		
		TRANSPORT(98),
		CHAR_START_POINT(99);
		
		private final int value;
		
		BLOCKVALUES(int value)
		{
			this.value = value;
		}
		
		public int getValue()
		{
			return value;
		}
	}
	
}
