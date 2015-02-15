package org.omrimeshulam.gameobjects;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class GameObject {
	private boolean drawable;
	private boolean updatable;
	public Sprite sprite; 
	
	public GameObject(){
	}
	
	public boolean isDrawable(){
		return drawable;
	}
	public boolean isUpdatable(){
		return updatable;
	}
	public void setIsDrawable(boolean val){
		drawable = val;
	}
	public void setIsUpdatable(boolean val){
		updatable = val;
	}
}
