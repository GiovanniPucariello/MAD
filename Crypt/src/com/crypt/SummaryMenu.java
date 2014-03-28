package com.crypt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class SummaryMenu 
{
		private TextureRegion parchment, summaryMessage, gameOverTitle, highScoresMessage, overMessage, summaryTitle, cursor;
		private final int MENU_WIDTH = 500;
		float labelTitleX, labelTitleY;
		private Sound buttonClick;
		
		private TextureRegion[] alphabet = new TextureRegion[27];
		
		private WorldController world;
	    
		private String[] hsName = new String[10];
		private int[] hsScore = new int[10];
		
		public Rectangle parchmentArea = new Rectangle();
		
		public boolean fireButtons;
		
		private int column = 0;
		private int[] initials = new int[3];
		
		public SummaryMenu(WorldController world) 
		{
			this.world= world; 
			// get file form disk 
			parchment = new TextureRegion(new Texture(Gdx.files.internal("data/parchment.png")));
			summaryTitle = new TextureRegion(new Texture(Gdx.files.internal("data/Congratulations.png")));
			summaryMessage = new TextureRegion(new Texture(Gdx.files.internal("data/CompleteMessage.png")));
			gameOverTitle = new TextureRegion(new Texture(Gdx.files.internal("data/GameOver.png")));
			highScoresMessage = new TextureRegion(new Texture(Gdx.files.internal("data/GameOverHighScoreMessage.png")));
			overMessage = new TextureRegion(new Texture(Gdx.files.internal("data/GameOverMessage.png")));
			cursor = new TextureRegion(new Texture(Gdx.files.internal("data/Cursor.png")));
						
			TextureRegion letters[][] = TextureRegion.split(new Texture(Gdx.files.internal("data/Alphabet.png")), 45, 71);
			// load texture array
			for(int i =0; i <27 ; i++)
			{
				alphabet[i] = letters[i][0];
			}
			
			// button clicks
			buttonClick = Gdx.audio.newSound(Gdx.files.internal("data/buttonClick.mp3"));
			
			// load high scores
			loadHighScore();
		}

		public void draw(SpriteBatch batch, Rectangle viewport, Boolean hasWon) 
		{
			float scale = ((float)Constant.NUM_ROWS / 12);
			
			float centerX = viewport.x + (viewport.width/2);		//Center of Display's X axis
			float centerY = viewport.y + (viewport.height/2);		//Center of Display's Y axis
			
			parchmentArea.width = MENU_WIDTH * scale;
			parchmentArea.height = 375 * scale;
			parchmentArea.x = centerX -(parchmentArea.width/2);
			parchmentArea.y = centerY-(parchmentArea.height/2);
			
			batch.draw(parchment, parchmentArea.x, parchmentArea.y, parchmentArea.width, parchmentArea.height);	//draw background
			
			if(hasWon == true) {
				// render level summary
				batch.draw(summaryTitle, centerX - (summaryTitle.getRegionWidth() / 2 * scale), parchmentArea.y + ((355 - summaryTitle.getRegionHeight()) * scale), summaryTitle.getRegionWidth() * scale, summaryTitle.getRegionHeight() * scale);
				batch.draw(summaryMessage, centerX - (summaryMessage.getRegionWidth() / 2 * scale), parchmentArea.y + (113 * scale), summaryMessage.getRegionWidth() * scale, summaryMessage.getRegionHeight() * scale);
			}
			else {
				// render game over
				batch.draw(gameOverTitle, centerX - (gameOverTitle.getRegionWidth() / 2 * scale), parchmentArea.y + ((355 - gameOverTitle.getRegionHeight()) * scale), gameOverTitle.getRegionWidth() * scale, gameOverTitle.getRegionHeight() * scale);
				
				// check if the score is higher than the lowest high score
				if (world.statusBar.getScore() > hsScore[9]) {
					batch.draw(highScoresMessage, centerX - (highScoresMessage.getRegionWidth() / 2 * scale), parchmentArea.y + (20 * scale), highScoresMessage.getRegionWidth() * scale, highScoresMessage.getRegionHeight() * scale);
					// draw initials for entering score
					batch.draw(alphabet[initials[0]], centerX -(67 * scale), parchmentArea.y + (70 * scale), 45 * scale, 71 * scale);
					batch.draw(alphabet[initials[1]], centerX -(22 * scale), parchmentArea.y + (70 * scale), 45 * scale, 71 * scale);
					batch.draw(alphabet[initials[2]], centerX +(23 * scale), parchmentArea.y + (70 * scale), 45 * scale, 71 * scale);
					batch.draw(cursor, centerX - (71 * scale) + (column * 45 * scale), parchmentArea.y + (71 * scale), 53 * scale, 71 * scale);
					fireButtons = true;
				}
				else {
					batch.draw(overMessage, centerX - (overMessage.getRegionWidth() / 2 * scale), parchmentArea.y + (77 * scale), overMessage.getRegionWidth() * scale, overMessage.getRegionHeight() * scale);
					fireButtons = false;
				}
			}
		}
		
		public void nextCol() {
			column += 1;
			if (column > 2) column = 0;
			buttonClick.play();
		}
		
		public void prevCol() {
			column -= 1;
			if (column < 0) column = 2;
			buttonClick.play();
		}
		
		public void nextLetter() {
			initials[column] += 1;
			if (initials[column] > 26) initials[column] = 0;
			buttonClick.play();
		}
		
		public void prevLetter() {
			initials[column] -= 1;
			if (initials[column] < 0) initials[column] = 26;
			buttonClick.play();
		}
		
		public void updateHighScores() {
			// find position in high score table and update it
			for(int i =0; i < 10; i++) {
				if (hsScore[i] < world.statusBar.getScore())
				{
					// move down the existing scores
					for(int z=8; z > i; z--) {
						hsScore[z+1] = hsScore[z];
						hsName[z+1] = hsName[z];
					}
					
					// insert new score and initials
					hsScore[i] = world.statusBar.getScore();
					String name = "";
					for(int w =0; w < 3; w++) {
						if (initials[w] != 26) {
							name += String.valueOf((char) (initials[w] + 65));
						}
						else {
							name += " ";
						}
					}
					hsName[i] = name;
					break;
				}
			}
			saveHighScores();
		}
		
		private void loadHighScore()
		{
			// initialise high scores in case the file is empty
			for(int i=0; i<10; i++)
			{
				hsName[i] = "AAA";
				hsScore[i] = 0;
			}
			
			FileHandle file = Gdx.files.local("HighScores.csv");
			if (Gdx.files.local("HighScores.csv").exists()) {
				String line = file.readString();
				
				String[] temp = line.split(",",-1); 
				
				int buff = 0;
				int i = 0;
				while(i< temp.length && i < 10)
				{
					hsName[i] = temp[buff];
					if(hsName[i].length()>3) {
						hsName[i] = hsName[i].substring(0,3);
					}
					hsScore[i] = Integer.parseInt(temp[buff+1]);
					i++;
					buff +=2;
				}
			}
		}
		
		private void saveHighScores()
		{
			String highScores = "";
			
			for(int i=0; i<10; i++)
			{
				highScores += hsName[i] + "," + String.format("%09d",hsScore[i]) + ",";
			}
			
			Gdx.files.local("HighScores.csv").delete();		 
			FileHandle file = Gdx.files.local("HighScores.csv");
			file.writeString(highScores, false);
		}
}
