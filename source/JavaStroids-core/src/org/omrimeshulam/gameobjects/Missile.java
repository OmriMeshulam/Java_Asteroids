package org.omrimeshulam.gameobjects;

import org.omrimeshulam.game.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Missile extends GameObject implements Updatable{
	
	private Vector2 dirAndVel;
	
	private final float VELOCITY = 100.0F;
	public boolean remove;
	int screenWidth;
	int screenHeight;
	  
	public Missile(Texture texture, Vector2 dir, Vector2 pos){
		this.sprite = new Sprite(texture);
	    this.sprite.setOrigin(texture.getWidth() / 2, texture.getHeight() / 2);
	    this.sprite.setPosition(pos.x+5, pos.y);
	    this.dirAndVel = new Vector2(dir.x, -dir.y);
	    this.dirAndVel.scl(VELOCITY);
	    this.sprite.rotate(this.dirAndVel.angle() - 90.0F);
	    this.screenWidth = Gdx.graphics.getWidth();
	    this.screenHeight = Gdx.graphics.getHeight();
	    setIsDrawable(true);
	}
	  
	public void update(float deltaTime)
	  {
	    this.sprite.translate(this.dirAndVel.x * deltaTime, this.dirAndVel.y * deltaTime);
	    if ((this.sprite.getX() > this.screenWidth) || //if the missile is out of bounds, removing this missile object
	      (this.sprite.getX() < 0.0F) || 
	      (this.sprite.getY() < 0.0F) || 
	      (this.sprite.getY() > this.screenHeight)) {
	      this.remove = true;
	    }
	  }
	
}