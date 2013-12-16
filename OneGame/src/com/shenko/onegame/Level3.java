package com.shenko.onegame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Level3 extends OneGameLevel {
	
	public Level3(OneGameScreen inScreen)
	{
		Screen = inScreen;
		Backdrop = new Texture(Gdx.files.internal("data/backdrop3.png"));
		
		Enemies = new ArrayList<OneGameObj>();
		
        for (int i=0; i < 3; i++)
        {
        	Enemies.add(new CrazedFan(Screen, new Vector3((float)(500 + (Math.random() * 150)), (float)(5 + Math.random() * 65), 0)));        	
        }
        for (int i=0; i < 3; i++)
        {
        	Enemies.add(new Paparazzi(Screen, new Vector3((float)(100 + (Math.random() * 150)), (float)(5 + Math.random() * 65), 0)));        	
        }
	}
}
