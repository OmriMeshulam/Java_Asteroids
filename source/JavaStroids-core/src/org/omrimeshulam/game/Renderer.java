package org.omrimeshulam.game;

import org.omrimeshulam.gameobjects.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
	
	public Renderer(Controller c){
		control = c;
		spriteBatch = new SpriteBatch(); 
		font = new BitmapFont();
		bg1 = new Texture("nebula.jpg");
		bg2 = new Texture("nebula.jpg");
		bg1XPos = 0;
		bg2XPos = bg1.getWidth();
		bg1Width = bg1.getWidth();
		bg2Width = bg2.getWidth();
		windowWidth = Gdx.graphics.getWidth();
		explosionSheet = new Texture(Gdx.files
				.internal("explosion17.png"));
		TextureRegion [][] tmp = TextureRegion.split(
				explosionSheet, explosionSheet.getWidth()/5, explosionSheet.getHeight()/5);
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
				
	}
	
	public void render(){
		spriteBatch.begin();
		renderBackground();
		for(GameObject gObj : control.getDrawableObjects()){
			gObj.sprite.draw(spriteBatch);
		}
		if (control.isShipCrashed() && 
				!explosionAnim.isAnimationFinished(shipExplosionStateTime)){
			shipExplosionStateTime += Gdx.graphics.getDeltaTime();
			currentExplosionFrame = explosionAnim.getKeyFrame(shipExplosionStateTime, false);
			spriteBatch.draw(currentExplosionFrame, control.getExplosionX()-Constants.SHIP_WIDTH, control.getExplosionY()-Constants.SHIP_HEIGHT);
		}
		if(control.isAsteroidCrash() &&
				!explosionAnim.isAnimationFinished(shipExplosionStateTime)){
			asteroidExplosionStateTime += Gdx.graphics.getDeltaTime();
			currentExplosionFrame = explosionAnim.getKeyFrame(asteroidExplosionStateTime, false);
			spriteBatch.draw(currentExplosionFrame, control.getAsteroidExplosionX()-Constants.ASTEROIDS_SIZE, control.getAsteroidExplosionY()-Constants.ASTEROIDS_SIZE);
			control.setAsteroidCrashFalse();
		}
		spriteBatch.end();
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

}
