package org.omrimeshulam.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import org.omrimeshulam.gameobjects.Asteroid;
import org.omrimeshulam.gameobjects.GameObject;
import org.omrimeshulam.gameobjects.Missile;
import org.omrimeshulam.gameobjects.Ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Vector2;

public class Controller {
	
	ArrayList<GameObject> drawableObjects; 
	Vector <Integer> removeList;
	Ship ship;
	private float screenHeight;
	private float missileWait;
	private Sound thrustersSound;
	private Music backgroundNoise;
	private Sound missileSound;
	Boolean soundCheck;
	private Boolean shipCrashed;
	private Boolean asteroidCrash;
	private Sound explosionSound;
	private float explosionX;
	private float explosionY;
	private float asteroidExplosionX;
	private float asteroidExplosionY;
	
	public Controller(){
		drawableObjects = new ArrayList<GameObject>(); 
		initShip();
		initAsteroids(10);
		initSound();
		screenHeight = Gdx.graphics.getHeight();
		soundCheck = true;
		shipCrashed = false;
		asteroidCrash = false;
		removeList = new Vector<Integer>();
	}
	
	private void initShip(){
		int w = Constants.SHIP_WIDTH; 
		int h = Constants.SHIP_HEIGHT; 
		missileWait = 0;
		Pixmap pmap = new Pixmap(w, h, Format.RGBA8888); // TODO: Check Image Format
		pmap.setColor(1, 1, 1, 1);
		pmap.drawLine(0, h, w/2, 0);
		pmap.drawLine(w, h, w/2, 0);
		pmap.drawLine(1, h-1, w, h-1);
		ship = new Ship(new Texture(pmap), 100, 100);
		drawableObjects.add(ship);
	}
	
	private void initAsteroids(int num){
		Random rand = new Random();
		for(int i = 0; i<num; i++){
			Asteroid asteroid = new Asteroid(new Texture("Asteroid_tex.png"));
			asteroid.sprite.setPosition(rand.nextInt(Gdx.graphics.getWidth()), rand.nextInt(Gdx.graphics.getHeight()));
			asteroid.sprite.setOrigin(asteroid.sprite.getWidth() / 2, asteroid.sprite.getHeight() / 2);
			asteroid.setRotVel(rand.nextFloat()*8-4);
			drawableObjects.add(asteroid);
		}
	}
	
	private void initMissile(){
		int w = Constants.SHIP_WIDTH/3;
		int h = Constants.SHIP_HEIGHT/3;
		Pixmap pmap = new Pixmap(w, h, Format.RGB565);
		pmap.setColor(1, 1, 1, 1);
		pmap.drawLine(w/2, 0, w/2, h);
		drawableObjects.add(new Missile(new Texture(pmap), ship.getDirection(), ship.getPosition()));
	}
	
	private void initSound(){
		thrustersSound = Gdx.audio.newSound(
				Gdx.files.internal(
						"125810__robinhood76__02578-rocket-start.wav"));
		missileSound = Gdx.audio.newSound(
				Gdx.files.internal(
						"missile.wav"));
		backgroundNoise = Gdx.audio.newMusic(
				Gdx.files.internal(
						"132150__soundsodd__interior-spaceship.mp3"));
		explosionSound = Gdx.audio.newSound(
				Gdx.files.internal(
						"100773__cgeffex__impact-explosion.wav"));
		
		backgroundNoise.setLooping(true);
		backgroundNoise.play();
		backgroundNoise.setVolume(0.5f);
	}	

	public void update(){
		processKeyboardInput();
		processMouseInput();
		float deltaT = Gdx.graphics.getDeltaTime();
		
		for(int i = 0; i < drawableObjects.size(); i++){
			GameObject gObj = drawableObjects.get(i);
			// updating asteroids
			if(gObj instanceof Asteroid){
				((Asteroid) gObj).update(deltaT);
				if(ship.sprite.getBoundingRectangle().overlaps(((Asteroid) gObj).sprite.getBoundingRectangle()) && !shipCrashed){
					shipCrashed = true;
					explosionSound.play();
					thrustersSound.stop();
					explosionX = ship.sprite.getX();
					explosionY = ship.sprite.getY();
				}
				for(GameObject mO : drawableObjects){
					if(mO instanceof Missile){
						if((mO).sprite.getBoundingRectangle().overlaps((gObj).sprite.getBoundingRectangle())){
							asteroidCrash = true;
							explosionSound.play();
							removeList.addElement(drawableObjects.indexOf(mO));
							removeList.addElement(i);
							asteroidExplosionX = gObj.sprite.getX();
							asteroidExplosionY = gObj.sprite.getY();

						}
					}
				}
			}
			// updating missiles
			if(gObj instanceof Missile){
				((Missile) gObj).update(deltaT);
			}
		}	
		// Update ship
		missileWait += deltaT;
		
		if(!shipCrashed){
			ship.update(deltaT);
		}else{
			drawableObjects.remove(ship);
		}
		Collections.sort(removeList);
		for(int i = removeList.size()-1; i>=0; i--){ // going backwards because once element is removed all object indexes to right decrease by 1.
			int index = removeList.get(i).intValue();
			drawableObjects.remove(index);
		}
		removeList.removeAllElements();
	
	}
	
	private void processKeyboardInput(){
		if (Gdx.app.getType() != ApplicationType.Desktop) return; // Just in case :)
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			ship.moveForward(Gdx.graphics.getDeltaTime());
			if(soundCheck){
				thrustersSound.play(0.5f);
				soundCheck=false;
			}
			else{
				if(Gdx.input.isKeyPressed(Keys.UP)){
					if(Gdx.input.isKeyJustPressed(Keys.UP)){
						soundCheck=true;
					}
				}
			}
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE) && missileWait > 1){
			missileWait = 0;
			missileSound.play(0.5f);
			initMissile();
		}
	}
	
	public ArrayList<GameObject> getDrawableObjects(){
		return drawableObjects;
	}
	
	private void processMouseInput(){
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			ship.face(new Vector2(Gdx.input.getX()-ship.sprite.getX(),
					-(screenHeight - Gdx.input.getY()-ship.sprite.getY())));
		}
		
	}
	
	public void dispose(){
		if(thrustersSound!=null){
			thrustersSound.dispose();
		}
		if(backgroundNoise!=null){
			thrustersSound.dispose();
		}
	}
	
	public boolean isShipCrashed(){
		return shipCrashed;
	}
	public boolean isAsteroidCrash(){
		return asteroidCrash;
	}
	public void setAsteroidCrashFalse(){
		asteroidCrash = false;
	}
	
	public float getExplosionX(){
		return explosionX;
	}
	
	public float getExplosionY(){
		return explosionY;
	}
	public float getAsteroidExplosionX(){
		return asteroidExplosionX;
	}
	
	public float getAsteroidExplosionY(){
		return asteroidExplosionY;
	}
}
