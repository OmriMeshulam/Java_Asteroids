package org.omrimeshulam.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Game extends ApplicationAdapter {
	private Controller control; 
	private Renderer render;
	
	@Override
	public void create () {
		control = new Controller();
		render = new Renderer(control);
	}
	
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		control.update(); // Process inputs and update game world.
		render.render();
	}
	
	@Override
	public void dispose(){
	}
	
}
