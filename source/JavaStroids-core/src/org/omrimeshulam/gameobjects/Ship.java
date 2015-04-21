package org.omrimeshulam.gameobjects;

import org.omrimeshulam.game.Constants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Ship extends GameObject implements Updatable{
	
	private Vector2 direction;
	private Vector2 targetDirection;
	private Vector2 velocity;
	private final float MIN_VELOCITY = 20;
	
	public Ship(Texture texture, int x, int y) {
		sprite = new Sprite(texture);
		sprite.setOrigin(texture.getWidth()/2, texture.getHeight()/2);
		sprite.setPosition(x, y);
		direction = new Vector2(0, -1);
		targetDirection = new Vector2(0, -1);
		velocity = new Vector2(0, MIN_VELOCITY);
		setIsDrawable(true);
	}

	@Override
	public void update(float deltaTime) {
	    double cosTheta = this.direction.dot(this.targetDirection) / this.targetDirection.len(); // creating the angle by dot product (formulation is based on the Law of Cosines)
	    double deg = Math.acos(cosTheta); // converting the angle
	    deg = Math.toDegrees(deg) * deltaTime; // in accordance with deltaTime (not frame rate dependent.)
	    if (this.direction.crs(this.targetDirection) > 0.0F) { // if the cross product between the vectors is positive(new vector to the right)
	      deg = -deg; // switch the direction of rotation		    
	    }
	    this.sprite.rotate((float)deg); // rotating the ship
	    this.direction.rotate(-(float)deg); // decreasing the angle gap the ship must rotate
	     
	    this.sprite.translate(this.velocity.x * deltaTime, this.velocity.y * deltaTime); //Translate the ship sprite using the components of velocity * deltaTime
	    if (this.velocity.len() > MIN_VELOCITY) { //If the current velocity (velocity magnitude) is above the minimum velocity
	    	this.velocity = this.velocity.scl(1.0F - deltaTime); // scale the velocity vector using (1 - deltaTime) as scalar
	      }
	  }

	public void moveForward(float deltaTime) {
		// change the values of each component of the velocity vector to the previous value of the component + 
		// (the correspondent component of the direction vector * the length of the velocity vector * 2 * deltaTime).
	    this.velocity.x += (this.direction.x * this.velocity.len() * 2.0F * deltaTime);
	    this.velocity.y -= (this.direction.y * this.velocity.len() * 2.0F * deltaTime);	
	}
	
	// Assignment 1
	public void face(Vector2 targetPos){
		targetDirection = targetPos;
	}
	
	public Vector2 getDirection(){
		return direction;
	}

	public Vector2 getPosition(){
		return new Vector2(sprite.getX(), sprite.getY());
	}
}
