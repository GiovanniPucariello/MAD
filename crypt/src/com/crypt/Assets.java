package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets 
{
	// asset objects
	private EntityAnimations character = new EntityAnimations("data/ManSprites.png", 9, 0.1f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	private EntityAnimations mummy = new EntityAnimations("data/MummySprite3.png",4,0.1f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	private EntityAnimations bat = new EntityAnimations("data/BatSprite.png",4,0.05f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	private EntityAnimations snake = new EntityAnimations("data/SnakeSprite.png",4,0.1f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	private EntityAnimations bullet = new EntityAnimations("data/BulletSpriteSmall.png",3,1f, 6, 6);
	private EntityAnimations openingDoor = new EntityAnimations("data/doorSprite.png",0,0.2f, Constant.BLOCK_SIZE * 2, Constant.BLOCK_SIZE *2);
	private EntityAnimations charTransport = new EntityAnimations("data/WhiteFlash.png",0,0.007f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	private EntityAnimations spawnSiteClouds = new EntityAnimations("data/SpawnSprite.png",0,0.1f, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
	
	private TextureRegion closedDoor = new TextureRegion(new Texture(Gdx.files.internal("data/door.png")));
	private TextureRegion[] treasure = new TextureRegion[10];
	private TextureRegion[] key = new TextureRegion[10];
	
	public Animation[] getCharAnim()
	{
		return character.getAnimations();
	}
	
	public Animation[] getBulletAnim(){
		//bullet = new Texture(Gdx.files.internal("data/bullet2.png"));
	    return bullet.getAnimations();
	}

	public Animation[] getCharTeleport()
	{
		return charTransport.getAnimations();
	}
	
	public Animation[] getMummyAnim()
	{
		return mummy.getAnimations();
	}
	
	public Animation[] getBatAnim()
	{
		return bat.getAnimations();
	}
	
	public Animation[] getSnakeAnim()
	{
		return snake.getAnimations();
	}
	
	public TextureRegion[] getTreasureImages()
	{
		// Get animation images and split into 2 dim array
		TextureRegion images[][] = TextureRegion.split(new Texture(Gdx.files.internal("data/TreasureSprites.png")), Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
		
		// load texture array
		for(int i =0; i <4 ; i++)
		{
			treasure[i] = images[0][i];
		}
		return treasure;
	}
	
	public TextureRegion[] getKeyImages()
	{
		// Get animation images and split into 2 dim array
		TextureRegion images[][] = TextureRegion.split(new Texture(Gdx.files.internal("data/Keys.png")), Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
		
		// load texture array
		for(int i =0; i <10 ; i++)
		{
			key[i] = images[0][i];
		}
		return key;
	}
	
	public Animation[] getSpawnSiteClouds()
	{
		return spawnSiteClouds.getAnimations();
	}
	
	public Animation[] getOpeningDoor()
	{
		return openingDoor.getAnimations();
	}
	
	public TextureRegion getDoorClosed()
	{
		return closedDoor;
	}

}
