package org.omrimeshulam.desktop;

import org.omrimeshulam.intro.IntroToLibGDX;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class LaunchIntro {
	public static void main(String [] args){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Wandering Bug";
		config.width = 800;
		config.height = 480; 
		new LwjglApplication(new IntroToLibGDX(), config);
		
	}
}
