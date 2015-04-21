package org.omrimeshulam.game;

import org.omrimeshulam.gameobjects.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Renderer {
	
	private SpriteBatch spriteBatch;
	private Controller control;
	BitmapFont font;
	Texture bg1, bg2;
	float bg1XPos, bg2XPos;
	float bg1Width, bg2Width, windowWidth;
	Animation explosionAnim;
	Texture explosionSheet;
	TextureRegion [] explosionFrames;
	TextureRegion currentExplosionFrame;
	float shipExplosionStateTime;
	float asteroidExplosionStateTime;
	Boolean lostLife = false;
	
	private byte gameState; // 1==Main_Menu, 2 == Main_Game, 3 == Next_level_screen, 4 == Gameover_screen, 5 == GameComplete_screen
	
	private Rectangle menuButtonNext, menuButtonStart, menuButtonRestart;
	private Sprite menuCongrats, menuNext,	menuStart, menuGameOver, menuRestart, menuGameComplete;
	private Texture menuButtonTex, menuButtonGameCompleteTex;
	
	private BitmapFont ScoreFont;
	private BitmapFont CreditsFont;
	private BitmapFont TitleFont;
	
	public Boolean controllerCalled = false;
	
	public Renderer(){
		spriteBatch = new SpriteBatch(); 
		font = new BitmapFont();
		bg1 = new Texture("sprites/nebula.jpg");
		bg2 = new Texture("sprites/nebula.jpg");
		bg1XPos = 0;
		bg2XPos = bg1.getWidth();
		bg1Width = bg1.getWidth();
		bg2Width = bg2.getWidth();
		windowWidth = Gdx.graphics.getWidth();
		explosionSheet = new Texture(Gdx.files.internal("sprites/explosion17.png"));
		TextureRegion [][] tmp = TextureRegion.split(explosionSheet, explosionSheet.getWidth()/5, explosionSheet.getHeight()/5);
		explosionFrames = new TextureRegion[25];
		int index = 0;
		for (int i = 0; i < 5; i++){
			for (int j = 0; j < 5; j++){
				explosionFrames[index++] = tmp[i][j];
			}
		}
		explosionAnim = new Animation(0.040F, explosionFrames);
		shipExplosionStateTime = 0.0F;
		asteroidExplosionStateTime = 0.0F;
		
		menuButtonTex = new Texture(Gdx.files.internal("sprites/menuButtons.png"));
		menuButtonGameCompleteTex = new Texture(Gdx.files.internal("sprites/gameComplete.png"));
		
		//menuCongrats = new Sprite(menuButtonTex, 65, 221, 390, 224); // position on sprite sheet (menuButtonTex)
		//menuCongrats.setPosition(((Gdx.graphics.getWidth()/2)-(menuCongrats.getWidth()/2)), Gdx.graphics.getHeight()/3);
		menuNext = new Sprite(menuButtonTex,130,524,303,100);
		menuNext.setPosition(((Gdx.graphics.getWidth()/2)-(menuNext.getWidth()/2)), Gdx.graphics.getHeight()/2);
		menuButtonNext = new Rectangle(0, 0, 303, 100);
		menuButtonNext.x = menuNext.getX();
		menuButtonNext.y = menuNext.getY();

		
		menuStart = new Sprite(menuButtonTex, 131, 644, 250,250);
		menuStart.setPosition(((Gdx.graphics.getWidth()/2)-(menuStart.getWidth()/2)), 70);
		
		menuButtonStart = new Rectangle(0, 0, 250, 250);
		menuButtonStart.x = menuStart.getX();
		menuButtonStart.y = menuStart.getY();
		
		menuGameOver = new Sprite(menuButtonTex, 25, 0, 475, 220);
		menuGameOver.setPosition(((Gdx.graphics.getWidth()/2)-(menuGameOver.getWidth()/2)), 145);
		
		menuRestart = new Sprite(menuButtonTex, 168, 452, 170, 63);
		menuRestart.setPosition(((Gdx.graphics.getWidth()/2)-(menuRestart.getWidth()/2)), 85);
		
		menuButtonRestart = new Rectangle(0, 0, 170, 63);
		menuButtonRestart.x = menuRestart.getX();
		menuButtonRestart.y = menuRestart.getY();
		
		menuGameComplete = new Sprite(menuButtonGameCompleteTex, 0, 0, 270, 105);
		menuGameComplete.setPosition(((Gdx.graphics.getWidth()/2)-(menuGameComplete.getWidth()/2)), 145);
		
		ScoreFont = new BitmapFont(Gdx.files.internal("font/ScoreFont.fnt"));
		Gdx.files.internal("font/ScoreFont.png" );
		ScoreFont.setScale(2);
		ScoreFont.setColor(Color.WHITE);
			
		CreditsFont = new BitmapFont(Gdx.files.internal("font/CreditsFont.fnt"));
		Gdx.files.internal("font/CreditsFont.png" );
		CreditsFont.setScale(1);
	
		TitleFont = new BitmapFont(Gdx.files.internal("font/TitleFont.fnt"));
		Gdx.files.internal("font/TitleFont.png" );
		TitleFont.setScale(2);
	
		gameState = 1;
	}
	
	public void render(){
		switch(this.gameState){
		case 1:
			this.mainMenu();
			break;
		case 2:
			this.mainGame();
			break;
		case 3:
			this.nextLevel();
			break;
		case 4:
			this.gameOver();
			break;
		case 5:
			//this.gameComplete();
			break;
		}
		
	}
	
	public void mainGame(){
		if(!controllerCalled){
			control = new Controller();
			controllerCalled = true;
		}else{
			control.update(); // Process inputs and update game world.
		}
		
		spriteBatch.begin();
		renderBackground();
		
		for(GameObject gObj : control.getDrawableObjects()){
			gObj.sprite.draw(spriteBatch);
		}
		if (control.isShipCrashing() && 
				!explosionAnim.isAnimationFinished(shipExplosionStateTime)){
			shipExplosionStateTime += Gdx.graphics.getDeltaTime();
			currentExplosionFrame = explosionAnim.getKeyFrame(shipExplosionStateTime, false);
			spriteBatch.draw(currentExplosionFrame, control.getExplosionX()-Constants.SHIP_WIDTH, control.getExplosionY()-Constants.SHIP_HEIGHT);
			this.lostLife = true;
			if(explosionAnim.isAnimationFinished(shipExplosionStateTime)){
				shipExplosionStateTime = 0;
				control.setShipCrashingFalse();
			}
		}
		if(control.isAsteroidCrash() &&
				!explosionAnim.isAnimationFinished(asteroidExplosionStateTime)){
			asteroidExplosionStateTime += Gdx.graphics.getDeltaTime();
			currentExplosionFrame = explosionAnim.getKeyFrame(asteroidExplosionStateTime, false);
			spriteBatch.draw(currentExplosionFrame, control.getAsteroidExplosionX()-Constants.ASTEROIDS_SIZE, control.getAsteroidExplosionY()-Constants.ASTEROIDS_SIZE);
			if(explosionAnim.isAnimationFinished(asteroidExplosionStateTime)){
				control.setAsteroidCrashFalse();
				asteroidExplosionStateTime = 0;
			}
		}
		
		ScoreFont.draw(spriteBatch, "Lives=" + control.checkLives(), 0, Gdx.graphics.getHeight());
		ScoreFont.draw(spriteBatch, "Score=" + control.getScore() , 0, Gdx.graphics.getHeight() - ScoreFont.getLineHeight());
		ScoreFont.draw(spriteBatch, "Asteroids Left=" + control.getAstroidNum() , 0, ScoreFont.getLineHeight());
		
		spriteBatch.end();
		
		if(lostLife){
			if(control.checkLives() == 0){
				gameState = 4;
			}
		}
		
		if(!control.checkAstroid()){
			gameState = 3;
			control.levelUp();
		}
	}
	
	public void renderBackground(){
		spriteBatch.draw(bg1, bg1XPos, 0);
		spriteBatch.draw(bg2, bg2XPos, 0);
		
		// Background Image Repositioning
		if (bg1XPos + bg1Width < windowWidth) { // if the end of the bg1 slides over the right side of the window
			bg2XPos = (bg1XPos + bg1Width); // fix bg2 X position to the end of bg1
		}
		if (bg2XPos + bg2Width < windowWidth) { // if the end of the bg2 slides over the right side of the window
			bg1XPos = (bg2XPos + bg2Width); // fix bg1 X position to the end of bg2
		}
		bg1XPos -= 0.3;
		bg2XPos -= 0.3;
	}
	
	
	private void mainMenu() {
		Gdx.gl.glClearColor(1f, 1f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);		
	
		spriteBatch.begin();
		renderBackground();
		menuStart.draw(spriteBatch);

		CreditsFont.draw(spriteBatch, "Java Astroids Game", 0, 56);
		CreditsFont.draw(spriteBatch, "Further developement by Omri Meshulam,", 0, 38);
		CreditsFont.draw(spriteBatch, "Originally developed for Game Programming Class", 0, 20);
		
		TitleFont.draw(spriteBatch, "Java Astroids", 260, 369);
		
		CreditsFont.draw(spriteBatch, "Press Right Button or Up to Move", (Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/3), 56);
		CreditsFont.draw(spriteBatch, "Press Left Button to Face Direction", (Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/3), 38);
		CreditsFont.draw(spriteBatch, "Press Space to Fire!", (Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/3), 20);
		
		spriteBatch.end();

		if(Gdx.input.justTouched()){
			Rectangle touch = new Rectangle(Gdx.input.getX(), -( Gdx.input.getY()-Gdx.graphics.getHeight()), 1, 1);
			if(touch.overlaps(menuButtonStart)){
				gameState = 2;
			}
		}
	}
	
	public void nextLevel(){
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		spriteBatch.begin();
		renderBackground();
		
		for(GameObject gObj : control.getDrawableObjects()){
			gObj.sprite.draw(spriteBatch);
		}
		
		if(control.isAsteroidCrash() &&
				!explosionAnim.isAnimationFinished(asteroidExplosionStateTime)){
			asteroidExplosionStateTime += Gdx.graphics.getDeltaTime();
			currentExplosionFrame = explosionAnim.getKeyFrame(asteroidExplosionStateTime, false);
			spriteBatch.draw(currentExplosionFrame, control.getAsteroidExplosionX()-Constants.ASTEROIDS_SIZE, control.getAsteroidExplosionY()-Constants.ASTEROIDS_SIZE);
			if(explosionAnim.isAnimationFinished(asteroidExplosionStateTime)){
				control.setAsteroidCrashFalse();
				asteroidExplosionStateTime = 0;
			}
		}
		
		ScoreFont.draw(spriteBatch, "Lives=" + control.checkLives(), 0, Gdx.graphics.getHeight());
		ScoreFont.draw(spriteBatch, "Score=" + control.getScore() , 0, Gdx.graphics.getHeight() - ScoreFont.getLineHeight());
		ScoreFont.draw(spriteBatch, "Asteroids Left=" + control.getAstroidNum() , 0, ScoreFont.getLineHeight());
		
		//this.menuCongrats.draw(spriteBatch);
		this.menuNext.draw(spriteBatch);

		spriteBatch.end();

		if(Gdx.input.justTouched()){
			Rectangle touch = new Rectangle(Gdx.input.getX(), -( Gdx.input.getY()-Gdx.graphics.getHeight()), 1, 1);
			if(touch.overlaps(menuButtonNext)){
				gameState = 2;
				control.refresh();				
			}	
		}
		
	}
	
	public void gameOver(){

		Gdx.gl.glClearColor(1f, 1f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		spriteBatch.begin();
		
		renderBackground();
		
		for(GameObject gObj : control.getDrawableObjects()){
			gObj.sprite.draw(spriteBatch);
		}
		
		
		if (control.isShipCrashing() && 
				!explosionAnim.isAnimationFinished(shipExplosionStateTime)){
			shipExplosionStateTime += Gdx.graphics.getDeltaTime();
			currentExplosionFrame = explosionAnim.getKeyFrame(shipExplosionStateTime, false);
			spriteBatch.draw(currentExplosionFrame, control.getExplosionX()-Constants.SHIP_WIDTH, control.getExplosionY()-Constants.SHIP_HEIGHT);
			this.lostLife = true;
			if(explosionAnim.isAnimationFinished(shipExplosionStateTime)){
				shipExplosionStateTime = 0;
				control.setShipCrashingFalse();
			}
		}
		ScoreFont.draw(spriteBatch, "Lives=" + control.checkLives(), 0, Gdx.graphics.getHeight());
		ScoreFont.draw(spriteBatch, "Score=" + control.getScore() , 0, Gdx.graphics.getHeight() - ScoreFont.getLineHeight());
		ScoreFont.draw(spriteBatch, "Asteroids Left=" + control.getAstroidNum() , 0, ScoreFont.getLineHeight());
		
		menuRestart.draw(spriteBatch);
		menuGameOver.draw(spriteBatch);
		
		spriteBatch.end();		

		if(Gdx.input.justTouched()){
			Rectangle touch = new Rectangle(Gdx.input.getX(), -( Gdx.input.getY()-Gdx.graphics.getHeight()), 1, 1);
			if(touch.overlaps(menuButtonRestart)){
				gameState = 1;
				control.levelStart();
				control.scoreClear();
				controllerCalled = false;
			}
		}

	}

}
