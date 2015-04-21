package org.omrimeshulam.gameobjects;

import java.util.Random;

import org.omrimeshulam.game.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Asteroid extends GameObject implements Updatable{

	private float rotationalVel;
	private Vector2 dirAndVel;
	int screenWidth;
	int screenHeight;
	
	public Asteroid(Texture tex){
		sprite = new Sprite(tex);
		sprite.setSize(Constants.ASTEROIDS_SIZE, Constants.ASTEROIDS_SIZE); 
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		Random rand = new Random();
		this.dirAndVel = new Vector2(rand.nextInt(100), rand.nextInt(100));
	    this.screenWidth = Gdx.graphics.getWidth();
	    this.screenHeight = Gdx.graphics.getHeight();
		setIsDrawable(true);
	}
	
	@Override
	public void update(float deltaTime) {
		sprite.rotate(getRotVel());
		this.sprite.translate(this.dirAndVel.x * deltaTime, this.dirAndVel.y * deltaTime);
		if ((this.sprite.getX() < 0.0F) || (this.sprite.getX() > this.screenWidth-sprite.getWidth())) { // if the asteroid coordinates meet the bounds of the screen
			this.dirAndVel.x = (-this.dirAndVel.x); // flip the directionAndVelocity accordingly.
		}
		if ((this.sprite.getY() < 0.0F) || (this.sprite.getY() > this.screenHeight-sprite.getHeight())) {
			this.dirAndVel.y = (-this.dirAndVel.y);
		}
	}
		
	public void setRotVel(float vel){
		rotationalVel = vel;
	}
	
	public float getRotVel(){
		return rotationalVel;
	}

}
