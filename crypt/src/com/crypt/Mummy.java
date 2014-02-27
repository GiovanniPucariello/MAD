package com.crypt;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Mummy extends Entity
{	
	Random randomGenerator = new Random();
	private WorldController worldController;
	
	public Mummy(WorldController worldController, Vector2 position, Animation[] animation, LevelMap levelMap, int spawnSiteID, boolean active)
	{	
		super(position, animation, new Vector2(8,2), new Vector2(-8,-2), spawnSiteID, active);
		
		CHAR_SPEED = 80 * Constant.GAME_SPEED;
		
		//Set velocity of mummy at spawn, if 0,0: do it again.
		velocity.x = randomGenerator.nextInt(2)-1;
		velocity.y = randomGenerator.nextInt(2)-1;		
		
		while (velocity.x == 0.0 && velocity.y == 0.0) 
		{
			velocity.x = randomGenerator.nextInt(3)-1;
			velocity.y = randomGenerator.nextInt(3)-1;
		}
		
		this.levelMap = levelMap;
		this.worldController = worldController;
	}
	
	@Override
	public void changeDirection() 	
	{	
		Rectangle characterBounds = new Rectangle(worldController.getCharacterBounds());
		
		int charBlockX = (int) (characterBounds.x / Constant.BLOCK_SIZE);
		int charBlockY = (int) (characterBounds.y / Constant.BLOCK_SIZE);
		
		int blockX = (int) (bounds.x / Constant.BLOCK_SIZE);
		int blockY = (int) (bounds.y / Constant.BLOCK_SIZE);
		
		if(characterBounds.x < bounds.x)
		{
			//System.out.println(blockX + ", " + blockY + " & " + charBlockX + ", " + charBlockY);
			for(int i=0; i<2; i++)
			{
				blockX--;
				
				if(levelMap.Cell(blockX, blockY) ==  Constant.BLOCKVALUES.PASSAGE.getValue())
				{
					if(blockX == charBlockX && blockY == charBlockY)
					{
						System.out.println("LEFT");
						velocity.x = -1;
						velocity.y = 0;
						break;
					}
					else
					{
						velocity.x = randomGenerator.nextInt(3)-1;
						velocity.y = randomGenerator.nextInt(3)-1;
					}
				}
				else if(levelMap.Cell(blockX, blockY) !=  Constant.BLOCKVALUES.PASSAGE.getValue())
				{
					velocity.x = randomGenerator.nextInt(3)-1;
					velocity.y = randomGenerator.nextInt(3)-1;
					break;
				}
			}
		}	
		
		if(characterBounds.y > bounds.y)
		{	
			charBlockX = (int) (characterBounds.x / Constant.BLOCK_SIZE);
			charBlockY = (int) (characterBounds.y / Constant.BLOCK_SIZE);
			
			blockX = (int) (bounds.x / Constant.BLOCK_SIZE);
			blockY = (int) (bounds.y / Constant.BLOCK_SIZE);
			
			for(int i=0; i<2; i++)
			{
				blockY++;
				
				if(levelMap.Cell(blockX, blockY) ==  Constant.BLOCKVALUES.PASSAGE.getValue())
				{
					if(blockX == charBlockX && blockY == charBlockY)
					{
						System.out.println("UP");
						velocity.y = 1;
						velocity.x = 0;
						break;
					}
					else
					{
						velocity.x = randomGenerator.nextInt(3)-1;
						velocity.y = randomGenerator.nextInt(3)-1;
					}
				}
				else if(levelMap.Cell(blockX, blockY) !=  Constant.BLOCKVALUES.PASSAGE.getValue())
				{
					velocity.x = randomGenerator.nextInt(3)-1;
					velocity.y = randomGenerator.nextInt(3)-1;
					break;
				}
			}
		}	
	
		if(characterBounds.x > bounds.x)
		{
			charBlockX = (int) (characterBounds.x / Constant.BLOCK_SIZE);
			charBlockY = (int) (characterBounds.y / Constant.BLOCK_SIZE);
			
			blockX = (int) (bounds.x / Constant.BLOCK_SIZE);
			blockY = (int) (bounds.y / Constant.BLOCK_SIZE);
	
			for(int i=0; i<2; i++)
			{
				blockX++;
				
				if(levelMap.Cell(blockX, blockY) ==  Constant.BLOCKVALUES.PASSAGE.getValue())
				{
					if(blockX == charBlockX && blockY == charBlockY)
					{
						System.out.println("RIGHT");
						velocity.x = 1;
						velocity.y = 0;
						break;
					}
					else
					{
						velocity.x = randomGenerator.nextInt(3)-1;
						velocity.y = randomGenerator.nextInt(3)-1;
					}
				}
				else if(levelMap.Cell(blockX, blockY) !=  Constant.BLOCKVALUES.PASSAGE.getValue())
				{
					velocity.x = randomGenerator.nextInt(3)-1;
					velocity.y = randomGenerator.nextInt(3)-1;
					break;
				}
			}
		}

		if(characterBounds.y < bounds.y)
		{
			
			charBlockX = (int) (characterBounds.x / Constant.BLOCK_SIZE);
			charBlockY = (int) (characterBounds.y / Constant.BLOCK_SIZE);
		
			blockX = (int) (bounds.x / Constant.BLOCK_SIZE);
			blockY = (int) (bounds.y / Constant.BLOCK_SIZE);
		
			for(int i=0; i<2; i++)
			{
				blockY--;
				
				if(levelMap.Cell(blockX, blockY) ==  Constant.BLOCKVALUES.PASSAGE.getValue())
				{
					if(blockX == charBlockX && blockY == charBlockY)
					{
						System.out.println("DOWN");
						velocity.y = -1;
						velocity.x = 0;
						break;
					}
					else
					{
						velocity.x = randomGenerator.nextInt(3)-1;
						velocity.y = randomGenerator.nextInt(3)-1;
					}
				}
				else if(levelMap.Cell(blockX, blockY) !=  Constant.BLOCKVALUES.PASSAGE.getValue())
				{
					velocity.x = randomGenerator.nextInt(3)-1;
					velocity.y = randomGenerator.nextInt(3)-1;
					break;
				}
			}
		}	
			
		while (velocity.x == 0.0 && velocity.y == 0.0) 
		{
			velocity.x = randomGenerator.nextInt(3)-1;
			velocity.y = randomGenerator.nextInt(3)-1;
		}
	}

	@Override
	void randomlyChangeDirection() 
	{	
		if (randomGenerator.nextInt(5) == 1) 
		{
			changeDirection();
		}
	}
}
