package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.crypt.screens.GameScreen;

public class MainMenuScreen implements Screen
{
	private Crypt game;
	private Stage stage;
	private BitmapFont font;
	private Skin skin;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private TextureRegion parchment;
	private OrthographicCamera camera;
	// Sounds
    private Sound buttonClick;
	
	private final int SCREEN_HEIGHT = 768;
	
	public MainMenuScreen(Crypt game) 
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
		
		stage.act(delta);
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// assuming that the height will be the set dimension of the constant
		// calculate the proportion width of the screen
		float proportion = (SCREEN_HEIGHT / height);
		float screenWidth = width * proportion;
		float screenHeight = SCREEN_HEIGHT;
		//stage.setViewport(screenWidth, screenHeight, false);
		//camera.setToOrtho(false, screenWidth, screenHeight);
		
		if (stage == null) stage = new Stage(screenWidth, screenHeight, true);
		stage.clear();
		
		
		Gdx.input.setInputProcessor(stage);
		
		Image parchmentbackground = new Image(parchment);
		float parchheight = SCREEN_HEIGHT - 80; 
		float parchwidth = parchheight * 1.4333f;
		parchmentbackground.setHeight(parchheight);
		parchmentbackground.setWidth(parchwidth);
		parchmentbackground.setX(40);
		parchmentbackground.setY(40);
		
		TextButtonStyle stylePlay = new TextButtonStyle();
		stylePlay.up = skin.getDrawable("playUp");
		stylePlay.down = skin.getDrawable("playDown");
				
		Button buttonPlay = new Button(stylePlay);
		float x = parchwidth + 60;
		float y = 50;
		buttonPlay.setX(x);
		buttonPlay.setY(y);
		
		TextButtonStyle stylePlayOptions = new TextButtonStyle();
		stylePlayOptions.up = skin.getDrawable("playingOptionsUp");
		stylePlayOptions.down = skin.getDrawable("playingOptionsDown");
				
		Button buttonPlayOptions = new Button(stylePlayOptions);
		y += 135;
		buttonPlayOptions.setX(x);
		buttonPlayOptions.setY(y);
		
		TextButtonStyle styleLevelPick = new TextButtonStyle();
		styleLevelPick.up = skin.getDrawable("levelSelectionUp");
		styleLevelPick.down = skin.getDrawable("levelSelectionDown");
				
		Button buttonLevelPick = new Button(styleLevelPick);
		y += 135;
		buttonLevelPick.setX(x);
		buttonLevelPick.setY(y);
		
		TextButtonStyle styleGamePick = new TextButtonStyle();
		styleGamePick.up = skin.getDrawable("gameSelectionUp");
		styleGamePick.down = skin.getDrawable("gameSelectionDown");
				
		Button buttonGamePick = new Button(styleGamePick);
		y += 135;
		buttonGamePick.setX(x);
		buttonGamePick.setY(y);
		
		TextButtonStyle styleEditor = new TextButtonStyle();
		styleEditor.up = skin.getDrawable("LevelEditorUp");
		styleEditor.down = skin.getDrawable("LevelEditorDown");
				
		Button buttonEditor = new Button(styleEditor);
		buttonEditor.setX(x);
		buttonEditor.setY(587);
		
		Image labelHighScores = new Image(new Texture("data/highScore.png"));
		labelHighScores.setX((parchwidth / 2) - (labelHighScores.getWidth()/ 2) + 50);
		labelHighScores.setY(parchheight - 50);
		
		buttonEditor.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				buttonClick.play();
				// requires class writing
				//game.setScreen(new LevelEditor(game));
			}
		});
		
		buttonGamePick.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				buttonClick.play();
				//game.setScreen(new GamePickScreen(game));
			}
		});
		
		buttonLevelPick.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				buttonClick.play();
				//game.setScreen(new LevelScreen(game));
			}
		});
		
		buttonPlayOptions.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				// play click sound
				buttonClick.play();
				
				// calls play option screen
				game.setScreen(new PlayingOptionsScreen(game));
			}
		});
		
		
		buttonPlay.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				return true;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button)
			{
				buttonClick.play();
				game.setScreen(new GameScreen(game));
			}
		});
		
		stage.addActor(parchmentbackground);
		stage.addActor(buttonPlay);
		stage.addActor(buttonPlayOptions);
		stage.addActor(buttonLevelPick);
		stage.addActor(buttonGamePick);
		stage.addActor(buttonEditor);
		stage.addActor(labelHighScores);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/MainUIButtons.pack");
		skin = new Skin();
		skin.addRegions(atlas);
		
		// get file form disk 
		parchment = new TextureRegion(new Texture(Gdx.files.internal("data/parchment.png")));
		
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("data/buttonClick.mp3"));
				
		font = new BitmapFont(Gdx.files.internal("data/TahomaFont.fnt"), false);
		
	}

	@Override
	public void hide() {
		dispose();
		
	}

	@Override
	public void pause() {
		
		
	}

	@Override
	public void resume() {
		
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		atlas.dispose();
		font.dispose();
		stage.dispose();
	}

}
