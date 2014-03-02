package com.crypt;

import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LevelSelectionScreen implements Screen 
{
	private boolean[] levels = new boolean[50];
	private int totallevels; 
	private int selectedLevel = 0;
	
	private Crypt game;
	private Stage stage;
	private BitmapFont font;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private Skin graphicSkin;
	private Skin skin;
	private OrthographicCamera camera;
	
	private TextureRegion[] number = new TextureRegion[15];
	
	private Button increaseLevel;
	private Button decreaseLevel;
	private Image levelNumberlbl;
	
	// Sounds
    private Sound buttonClick;
	
	private final int SCREEN_HEIGHT = 768;
	
	public LevelSelectionScreen(Crypt game)
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
		
		if (stage == null) stage = new Stage(screenWidth, screenHeight, true);
		stage.clear();
		
		
		Gdx.input.setInputProcessor(stage);
		
		// Done Button
		Button buttonDone = addButton("Done", Gdx.graphics.getWidth(), 50);
		
		// level selection buttons
		increaseLevel = addButton("More", (Gdx.graphics.getWidth()/ 2.0f+250), (Gdx.graphics.getHeight() / 2.0f) + 91);
		decreaseLevel = addButton("Less", (Gdx.graphics.getWidth()/ 2.0f+250), (Gdx.graphics.getHeight() / 2.0f) + 29);
		
		Image levellbl = new Image(graphicSkin.getDrawable("LevelSelection"));
		levellbl.setX((Gdx.graphics.getWidth()/ 2.0f) - 30);
		levellbl.setY((Gdx.graphics.getHeight() / 2.0f) +150);
				
		levelNumberlbl = new Image(number[selectedLevel]);
		levelNumberlbl.setX((Gdx.graphics.getWidth()/ 2.0f) + 80);
		levelNumberlbl.setY((Gdx.graphics.getHeight() / 2.0f) + 30);
		
		buttonDone.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				buttonClick.play();
				setCurrentLevel();
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		increaseLevel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				buttonClick.play();
				
			}
		});
		
		decreaseLevel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(selectedLevel > 0)
				{
					selectedLevel--;
					setLevel(selectedLevel);	
					buttonClick.play();
				}
				else {
					
				}
			}
		});
		
		increaseLevel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (selectedLevel < totallevels && levels[selectedLevel + 1] == true)
				{
					selectedLevel++;
					setLevel(selectedLevel);
					buttonClick.play();
				} else {
					
				}
			}
		});
		
		stage.addActor(buttonDone);
		stage.addActor(increaseLevel);
		stage.addActor(decreaseLevel);
		stage.addActor(levellbl);
		stage.addActor(levelNumberlbl);
	}
	
	public void setLevel(int level)
	{
		levelNumberlbl.setDrawable(new TextureRegionDrawable(number[level]));
	}
	
	private Button addButton(String name, float x, float y)
	{
		TextButtonStyle styleBut = new TextButtonStyle();
		styleBut.up = graphicSkin.getDrawable(name+"Up");
		styleBut.down = graphicSkin.getDrawable(name+"Down");
				
		Button button = new Button(styleBut);
		
		button.setX(x);
		button.setY(y);
		return button;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/LevelOptionUI.pack");
		graphicSkin = new Skin();
		graphicSkin.addRegions(atlas);
			
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("data/buttonClick.mp3"));
		
		TextureRegion levelNumber[][] = TextureRegion.split(new Texture(Gdx.files.internal("data/LevelNumbers.png")), 74, 102);
		// load texture array
		for(int i =0; i <15 ; i++)
		{
			number[i] = levelNumber[i][0];
		}
		
		loadlevelsAttained();
		currentlySelectedLevel();
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
		batch.dispose();
		skin.dispose();
		atlas.dispose();
		font.dispose();
		stage.dispose();		
	}
	
	private void loadlevelsAttained()
	{
		FileHandle file = Gdx.files.local("LevelCompleted.csv");
		if (Gdx.files.local("LevelCompleted.csv").exists()) {
			String line = file.readString();
			StringTokenizer token = new StringTokenizer(line, ",");
			
			int i = 0;
			while(token.hasMoreTokens() && i < 50)
			{
				levels[i] = Boolean.valueOf(token.nextToken());
				i++;
			}
			totallevels = i;
		}
	}
	
	private void setCurrentLevel()
	{
		Gdx.files.local("StartingsLevel.csv").delete();		 
		FileHandle file = Gdx.files.local("LevelSelected.csv");
		String line = Integer.toString(selectedLevel);
		file.writeString(line, false);
	}
	
	private void currentlySelectedLevel()
	{
		FileHandle file = Gdx.files.local("LevelSelected.csv");
		if (Gdx.files.local("LevelSelected.csv").exists()) {
			String line = file.readString();
			if (line !="") selectedLevel = Integer.valueOf(line);
		}
	}
	
}
