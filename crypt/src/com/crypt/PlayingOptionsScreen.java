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
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


public class PlayingOptionsScreen implements Screen
{
	private Crypt game;
	private Stage stage;
	private BitmapFont font;
	private Skin graphicSkin;
	private Skin skin;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private OrthographicCamera camera;
	
	private TextureRegion sampleBlock;
	
	private Image sampleGraphic;
	
	private float gameSpeed = 1;
	private int graphicsSize = 12;
	private int blackground = 0;
	private int joystick = 0;
	
	// Sounds
    private Sound buttonClick;
    private Sound buttonRadio;
    private Sound buttonSlider;
	
	private final int SCREEN_HEIGHT = 768;
	
	public PlayingOptionsScreen(Crypt game) 
	{
		this.game = game;
		
		loadGameOptions();
		
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
		camera.setToOrtho(false, screenWidth, screenHeight);
		
		final int offsetX = (int) screenWidth / 2 - 265;
		final int offsetY = (int) screenHeight / 2 - 438;
		
		
		stage = new Stage(screenWidth, screenHeight, true);
		stage.clear();
		
		Gdx.input.setInputProcessor(stage);
				
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		// Done Button
		TextButtonStyle styleDoneBut = new TextButtonStyle();
		styleDoneBut.up = graphicSkin.getDrawable("DoneUp");
		styleDoneBut.down = graphicSkin.getDrawable("doneDown");
				
		Button buttonDone = new Button(styleDoneBut);
		buttonDone.setX(width);
		buttonDone.setY(20);
		
		buttonDone.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				buttonClick.play();
				saveGameOptions();
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		// Game Speed
		Image gameSpeedLbl = new Image(graphicSkin.getDrawable("GameSpeed"));
		gameSpeedLbl.setX(50 + offsetX);
		gameSpeedLbl.setY(650 + offsetY);
		
		Image gameSpeedMinusLbl = new Image(graphicSkin.getDrawable("-"));
		gameSpeedMinusLbl.setX(290 + offsetX);
		gameSpeedMinusLbl.setY(630 + offsetY);
		
		Slider gameSpeedSlider = new Slider(0.6f, 1f, 0.1f, false, skin);
		gameSpeedSlider.setX(340 + offsetX);
		gameSpeedSlider.setY(660 + offsetY);
		gameSpeedSlider.setWidth(300);
		gameSpeedSlider.setValue(gameSpeed);
		
		gameSpeedSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				buttonSlider.play();
				Slider s = (Slider) actor;
				gameSpeed = s.getValue();
				if (gameSpeed > 1) gameSpeed = 1.0f;
			}
		});
		
		Image gameSpeedPlusLbl = new Image(graphicSkin.getDrawable("+"));
		gameSpeedPlusLbl.setX(650 + offsetX);
		gameSpeedPlusLbl.setY(630 + offsetY);
		
		// Size of Graphics
		Image graphicSizeLbl = new Image(graphicSkin.getDrawable("SizeOfGraphics"));
		graphicSizeLbl.setX(50 + offsetX);
		graphicSizeLbl.setY(545 + offsetY);
			
		Slider resolutionSlider = new Slider(6, 12, 3, false, skin);
		resolutionSlider.setX(380 + offsetX);
		resolutionSlider.setY(545 + offsetY);
		resolutionSlider.setWidth(200);
		resolutionSlider.setValue(graphicsSize);
		updateGraphics(offsetY, offsetX, resolutionSlider);
		
		sampleGraphic.setX(650 + offsetX);
		sampleGraphic.setY(543 + offsetY);
		
		resolutionSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				buttonSlider.play();
				Slider s = (Slider) actor;
				graphicsSize = (int) s.getValue();
				updateGraphics(offsetY, offsetX, s);
			}
		});
		
		// Passageway Colour
		Image passageWayColourLbl = new Image(graphicSkin.getDrawable("PassagewayColour"));
		passageWayColourLbl.setX(50 + offsetX);
		passageWayColourLbl.setY(440 + offsetY);
		
		ButtonGroup passWayGroup = new ButtonGroup();
		
		TextButtonStyle stylePassBut = new TextButtonStyle();
		stylePassBut.up = graphicSkin.getDrawable("SquareNotChecked");
		stylePassBut.down = graphicSkin.getDrawable("SquareNotChecked");
		stylePassBut.checked = graphicSkin.getDrawable("SquareChecked");
		
		TextButtonStyle styleStdPassBut = new TextButtonStyle();
		styleStdPassBut.up = graphicSkin.getDrawable("StandardNotChecked");
		styleStdPassBut.down = graphicSkin.getDrawable("StandardNotChecked");
		styleStdPassBut.checked = graphicSkin.getDrawable("StandardChecked");
		
		Button buttonPassBlack = new Button(styleStdPassBut);
		buttonPassBlack.setColor(1, 1, 1, 1);
		buttonPassBlack.setX(430 + offsetX);
		buttonPassBlack.setY(440 + offsetY);
		if (blackground == 0) buttonPassBlack.setChecked(true);
		
		Button buttonPassLGray = new Button(stylePassBut);
		buttonPassLGray.setColor(0.75f, 0.75f, 0.75f, 1);
		buttonPassLGray.setX(510 + offsetX);
		buttonPassLGray.setY(440 + offsetY);
		if (blackground == 1) buttonPassLGray.setChecked(true);
		
		Button buttonPassOrange = new Button(stylePassBut);
		buttonPassOrange.setColor(1, 0.78f, 0, 1);
		buttonPassOrange.setX(590 + offsetX);
		buttonPassOrange.setY(440 + offsetY);
		if (blackground == 2) buttonPassOrange.setChecked(true);
		
		Button buttonPassYellow = new Button(stylePassBut);
		buttonPassYellow.setColor(1, 1, 0, 1);
		buttonPassYellow.setX(670 + offsetX);
		buttonPassYellow.setY(440 + offsetY);
		if (blackground == 3) buttonPassYellow.setChecked(true);
		
		Button buttonPassCyan = new Button(stylePassBut);
		buttonPassCyan.setColor(0, 1, 1, 1);
		buttonPassCyan.setX(750 + offsetX);
		buttonPassCyan.setY(440 + offsetY);
		if (blackground == 4) buttonPassCyan.setChecked(true);
		
		passWayGroup.add(buttonPassBlack);
		passWayGroup.add(buttonPassLGray);
		passWayGroup.add(buttonPassOrange);
		passWayGroup.add(buttonPassYellow);
		passWayGroup.add(buttonPassCyan);
		
		buttonPassBlack.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Button s = (Button) actor;
				if (s.isChecked())
				{
					blackground = 0;
					buttonRadio.play();
				}
			}
		});
		
		buttonPassLGray.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Button s = (Button) actor;
				if (s.isChecked())
				{
					blackground = 1;
					buttonRadio.play();
				}
			}
		});
		
		buttonPassOrange.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Button s = (Button) actor;
				if (s.isChecked())
				{
					blackground = 2;
					buttonRadio.play();
				}
			}
		});
		
		buttonPassYellow.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Button s = (Button) actor;
				if (s.isChecked())
				{
					blackground = 3;
					buttonRadio.play();
				}
			}
		});
		
		buttonPassCyan.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Button s = (Button) actor;
				if (s.isChecked())
				{
					blackground = 4;
					buttonRadio.play();
				}
			}
		});
		
		// character control
		// Passageway Colour
		Image charControlLbl = new Image(graphicSkin.getDrawable("CharacterControl"));
		charControlLbl.setX(40 + offsetX);
		charControlLbl.setY(325 + offsetY);
		
		Image joystickLbl = new Image(graphicSkin.getDrawable("ScreenJoystick"));
		joystickLbl.setX(500 + offsetX);
		joystickLbl.setY(330 + offsetY);
		
		Image tiltLbl = new Image(graphicSkin.getDrawable("Tilt&Turn"));
		tiltLbl.setX(500 + offsetX);
		tiltLbl.setY(260 + offsetY);
		
		
		ButtonGroup controlGroup = new ButtonGroup();
						
		TextButtonStyle styleJoyBut = new TextButtonStyle();
		styleJoyBut.up = graphicSkin.getDrawable("radioNotChecked");
		styleJoyBut.down = graphicSkin.getDrawable("radioNotChecked");
		styleJoyBut.checked = graphicSkin.getDrawable("radioChecked");
				
		Button buttonJoy = new Button(styleJoyBut);
		buttonJoy.setX(430 + offsetX);
		buttonJoy.setY(335 + offsetY);
		if (joystick == 1) buttonJoy.setChecked(true); 
		
		
		Button buttonTilt = new Button(styleJoyBut);
		buttonTilt.setX(430 + offsetX);
		buttonTilt.setY(265 + offsetY);
		if (joystick == 0) buttonTilt.setChecked(true); 
		
		controlGroup.add(buttonJoy);
		controlGroup.add(buttonTilt);
		buttonJoy.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Button s = (Button) actor;
				if (s.isChecked())
				{
					joystick = 1;
					buttonRadio.play();
				}
			}
		});
		
		buttonTilt.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Button s = (Button) actor;
				if (s.isChecked())
				{
					joystick = 0;
					buttonRadio.play();
				}
			}
		});
		
		stage.addActor(buttonDone);
		stage.addActor(gameSpeedLbl);
		stage.addActor(gameSpeedMinusLbl);
		stage.addActor(gameSpeedSlider);
		stage.addActor(gameSpeedPlusLbl);
		
		
		stage.addActor(graphicSizeLbl);
		stage.addActor(resolutionSlider);
		stage.addActor(sampleGraphic);
		
		stage.addActor(passageWayColourLbl);
		stage.addActor(buttonPassBlack);
		stage.addActor(buttonPassLGray);
		stage.addActor(buttonPassOrange);
		stage.addActor(buttonPassYellow);
		stage.addActor(buttonPassCyan);

		stage.addActor(charControlLbl);
		stage.addActor(joystickLbl);
		stage.addActor(tiltLbl);
		stage.addActor(buttonJoy);
		stage.addActor(buttonTilt);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/PlayerOptionsUI.pack");
		graphicSkin = new Skin();
		graphicSkin.addRegions(atlas);
			
		buttonClick = Gdx.audio.newSound(Gdx.files.internal("data/buttonClick.mp3"));
		buttonRadio = Gdx.audio.newSound(Gdx.files.internal("data/buttonRadio.mp3"));
		buttonSlider = Gdx.audio.newSound(Gdx.files.internal("data/buttonSlider.mp3"));
		sampleBlock = new TextureRegion(new Texture(Gdx.files.internal("data/MazeBlockMid.png")));
		sampleGraphic = new Image(sampleBlock);
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
	
	private void updateGraphics(float offsetY, float offsetX, Slider s)
	{
		sampleGraphic.setScale(12/s.getValue(), 12/s.getValue());
		sampleGraphic.setY(543 + offsetY - ((12/s.getValue()*64) - 64) / 2);
		sampleGraphic.setX(650  + offsetX - ((12/s.getValue()*64) - 64) / 2);
	}
	
	private void loadGameOptions()
	{
		FileHandle file = Gdx.files.local("GameOptions.csv");
		if (Gdx.files.local("GameOptions.csv").exists()) {
			String line = file.readString();
			StringTokenizer token = new StringTokenizer(line, ",");
						
			if (token.hasMoreTokens()) {
				gameSpeed = Float.valueOf(token.nextToken());
			}
			if (token.hasMoreTokens()) {
				graphicsSize = Integer.valueOf(token.nextToken());
			}
			if (token.hasMoreTokens()) {
				blackground = Integer.valueOf(token.nextToken());
			}
			if (token.hasMoreTokens()) {
				joystick = Integer.valueOf(token.nextToken());
			}
		}
	}
	
	private void saveGameOptions()
	{
		Gdx.files.local("GameOptions.csv").delete();		 
		FileHandle file = Gdx.files.local("GameOptions.csv");
		String line = Float.toString(gameSpeed)+",";
		line += Integer.toString(graphicsSize)+",";
		line += Integer.toString(blackground) + ",";
		line += Integer.toString(joystick);
		file.writeString(line, false);
	}
}
