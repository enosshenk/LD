package com.shenko.onegame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Level1 extends OneGameLevel {
	
	public Level1(OneGameScreen inScreen)
	{
		Screen = inScreen;
		Backdrop = new Texture(Gdx.files.internal("data/backdrop1.png"));
		
		Enemies = new ArrayList<OneGameObj>();
		
       for (int i=0; i < 2; i++)
        {
        	Enemies.add(new Paparazzi(Screen, new Vector3((float)(400 + (Math.random() * 100)), (float)(20 + Math.random() * 20), 0)));
        }
	}

}
