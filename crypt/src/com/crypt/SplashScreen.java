package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen 
{
	private Crypt game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Sprite splashSprite;
	private float timer = 0;
	private float fadeIn = 0.7f;
	private float hold = 1.0f;
	
	private final int SCREEN_HEIGHT = 768;
	
	public SplashScreen(Crypt game) 
	{
		this.game = game;
		
		// set to the camera view of the game
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.79097f, 0.82013f, 0.67885f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		timer += delta;
		if (timer < fadeIn) 
		{
			float alpha = (timer / fadeIn);
			splashSprite.setColor(1, 1, 1, alpha);
		}
		
		if (timer > (fadeIn + hold))
		{
			float alpha = 1 - ((timer - (fadeIn + hold))/ fadeIn);
			splashSprite.setColor(1, 1, 1, alpha);
		}
		if (timer > (hold + fadeIn * 2)) introCompleted();
		
		batch.begin();
			splashSprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// assuming that the height will be the set dimension of the constant
		// calculate the proportion width of the screen
		float proportion = (SCREEN_HEIGHT / height);
		float screenWidth = width * proportion;
		float screenHeight = SCREEN_HEIGHT;
		camera.setToOrtho(false, screenWidth, screenHeight);
		
		// centre the splashSprite
		splashSprite.setPosition(screenWidth / 2 - splashSprite.getWidth() / 2, screenHeight / 2 - splashSprite.getHeight() / 2);
		
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		
		Texture splashTexture = new Texture("data/splashImage.png");
		splashSprite = new Sprite(splashTexture);
		splashSprite.setColor(1, 1, 1, 0);
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private void introCompleted()
	{
		game.setScreen(new MainMenuScreen(game));
	}
}
