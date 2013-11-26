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
	public final static int NUM_MAP_BLOCKS = 11;
	
	// Block types and filenames
	public static enum BLOCK
	{
		PASSAGE("data/PassageWay.png"), 
		MID("data/MazeBlockMid.png"),
		COLUMN_LEFT("data/MazePassageLH.png"),
		COLUMN_RIGHT("data/MazePassageRH.png"),
		END_RIGHT("data/MazeBlockRH.png"),
		END_LEFT("data/MazeBlockLH.png"),
		SINGLE("data/MazeBlockEnd.png"),
		ALCOVEUP("data/AlcoveUP.png"),
		ALCOVERH("data/AlcoveRH.png"),
		ALCOVEDW("data/AlcoveDW.png"),
		ALCOVELH("data/AlcoveLH.png");
		
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
		ALCOVEUP(7),
		ALCOVERH(8),
		ALCOVEDW(9),
		ALCOVELH(10),
		
		TRANSPORT(98),
		CHAR_START_POINT(99),
		SPAWN_SITE(100);
		
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
	
	// numeric values for the treasure types
	public static enum TREASURE_TYPE
	{
		EMRALD(0,400),
		RUBY(1,600),
		SAPPHIRE(2,800),
		DIAMMOND(3,1000),
		SILVER(4,500),
		CRYSTALS(5,750),
		GOLD(9, 1000);
		
		
		private final int value;
		private final int points;
		
		TREASURE_TYPE(int value, int points)
		{
			this.value = value;
			this.points = points;
		}
		
		public int getValue()
		{
			return value;
		}
		
		public int getPoints()
		{
			return points;
		}
	}
}
