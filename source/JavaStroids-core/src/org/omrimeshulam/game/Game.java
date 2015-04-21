package org.omrimeshulam.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Game extends ApplicationAdapter {
	private Renderer render;
	
	@Override
	public void create () {
		render = new Renderer();
	}
	
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		render.render();
	}
	
	@Override
	public void dispose(){
	}
	
}
