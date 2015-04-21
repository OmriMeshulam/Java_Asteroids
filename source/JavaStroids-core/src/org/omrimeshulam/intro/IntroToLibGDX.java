package org.omrimeshulam.intro;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class IntroToLibGDX extends ApplicationAdapter{

	private SpriteBatch spriteBatch;
	private Sprite bug;
	private Sprite chest;
	Vector2 line;
	int degreeRate = 10; // bug rotation speed
	int pixelsPerSec = 10; // bug movement speed
	int state = 0;
	
	@Override
	public void create() {
		// Game Initialization  
		spriteBatch = new SpriteBatch(); 
		bug = new Sprite(new Texture("EnemyBug.png"));
		bug.setSize(50, 85);
		bug.setOrigin(bug.getWidth() / 2, bug.getHeight() / 2);
		bug.setPosition(0, 0);
		chest = new Sprite(new Texture("ChestClosed.png"));
		chest.setSize(50, 85);
		chest.setOrigin(chest.getWidth()/2, chest.getHeight()/2);
		chest.setPosition(270, 240);
		line  = new Vector2(Gdx.graphics.getWidth() - 0, Gdx.graphics.getHeight()-0); // B - A
		line.nor();
		line.scl(pixelsPerSec);
		bug.rotate(line.angle());
		
	}

	@Override
	public void render() {
		// Game Loop
		Gdx.gl.glClearColor(0.7f, 0.7f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		if (state==0){
			if(bug.getX()<Gdx.graphics.getWidth()-90){ // going to upper right corner stopping right before 800x480, checking if there yet
				bug.translate(line.x * Gdx.graphics.getDeltaTime(), line.y * Gdx.graphics.getDeltaTime()); // sets position relative to the current position, walking the line 	
			}else{
				line.rotate(180); // flipping the line, (angle flipped too +180)
				state=1;
			}
		}else if(state==1){ // smooth bug rotation
			if(bug.getRotation() > line.angle() - 0.5){  // checking if its angle of rotation increased more than the vector angle (tests show .5-2 degree over (not consciously visual))
				bug.setRotation(line.angle()-360); // Realigning "graphical artifact", -360 for mathematic comparison purpose in stage 3.
				state=2;
			}else{
				bug.rotate(degreeRate * Gdx.graphics.getDeltaTime()); // turning in accordance with deltaTime
			}
		}
		else if(state==2){
			if(bug.getX()>10){ // going to lower left corner, checking if there yet
				bug.translate(line.x * Gdx.graphics.getDeltaTime(), line.y * Gdx.graphics.getDeltaTime());	
			}else{
				line.rotate(180); //(angle - 180)
				state=3;
			}
		}else if (state==3){
			if(bug.getRotation() > line.angle() - 0.5){ // checking if its angle of rotation increased more than the vector angle (tests show .5-2 degree over(not consciously visual))
				bug.setRotation(line.angle()); // Realigning "graphical artifact"
				state=0;
			}else{
				bug.rotate(degreeRate * Gdx.graphics.getDeltaTime()); // turning in accordance with deltaTime
			}
		}
		bug.draw(spriteBatch);
		chest.draw(spriteBatch);
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		super.dispose();
	}
}
