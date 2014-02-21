package com.crypt;

import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Constant 
{
	// Camera view screen height
	public static float SCREEN_HEIGHT = 768;
	
	// Number of rows always required on screen
	public static int NUM_ROWS = 12;
	
	// Block size
	public final static int BLOCK_SIZE = 64;
	
	// Number of block used in map
	public final static int NUM_MAP_BLOCKS = 12;
	
	// Game speed factor
	public static float GAME_SPEED;
	
	// Character control method
	public final static int JOYSTICK = 1;
	public final static int TILT = 0;
	public static int CHAR_CONTROL = 0;
	
	// PassageWays Colour
	public static int PASSAGEWAY_COLOUR = 0;
	
	public final static int PASSAGEWAY_BLACK = 0;
	public final static int PASSAGEWAY_LGRAY = 0;
	public final static int PASSAGEWAY_ORANGE = 0;
	public final static int PASSAGEWAY_YELLOW = 0;
	public final static int PASSAGEWAY_CYAN = 0;
	
	public static void loadGameOptions()
	{
		FileHandle file = Gdx.files.local("GameOptions.csv");
		if (Gdx.files.local("GameOptions.csv").exists()) {
			String line = file.readString();
			StringTokenizer token = new StringTokenizer(line, ",");
						
			if (token.hasMoreTokens()) {
				GAME_SPEED = Float.valueOf(token.nextToken());
			}
			if (token.hasMoreTokens()) {
				NUM_ROWS = Integer.valueOf(token.nextToken());
				SCREEN_HEIGHT = NUM_ROWS * BLOCK_SIZE;
			}
			if (token.hasMoreTokens()) {
				PASSAGEWAY_COLOUR = Integer.valueOf(token.nextToken());
			}
			if (token.hasMoreTokens()) {
				CHAR_CONTROL = Integer.valueOf(token.nextToken());
			}
		}
	}
	
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
		ALCOVELH("data/AlcoveLH.png"),
		PASSAGE_NO_WALK("data/PassageWay.png");
		
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
		PASSAGE_NO_WALK(11),
		
		NEXT_LEVEL(97),
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
	
	// numeric values for the treasure types
	public static enum KEY_TYPE
	{
		KEY1(11,0),
		KEY2(12,1),
		KEY3(13,2),
		KEY4(14,3),
		KEY5(15,4),
		KEY6(16,5),
		KEY7(17,6),
		KEY8(18,7),
		KEY9(19,8),
		KEY10(20,9);
		
		
		private final int value;
		private final int keyValue;
		
		KEY_TYPE(int value, int keyValue)
		{
			this.value = value;
			this.keyValue = keyValue;
		}
		
		public int getValue()
		{
			return value;
		}
		
		public int getKeyValue()
		{
			return keyValue;
		}
	}

	public static enum DOORS
	{
		DOOR1(21,0),
		DOOR2(22,1),
		DOOR3(23,2),
		DOOR4(24,3),
		DOOR5(25,4),
		DOOR6(26,5),
		DOOR7(27,6),
		DOOR8(28,7),
		DOOR9(29,8),
		DOOR10(30,9);		
		
		private final int value;
		private final int keyValue;
		
		DOORS(int value, int keyValue)
		{
			this.value = value;
			this.keyValue = keyValue;
		}
		
		public int getValue()
		{
			return value;
		}
		
		public int getKeyValue()
		{
			return keyValue;
		}
	}
}
