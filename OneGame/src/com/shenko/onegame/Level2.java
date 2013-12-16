package com.shenko.onegame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Level2 extends OneGameLevel {
	
	public Level2(OneGameScreen inScreen)
	{
		Screen = inScreen;
		Backdrop = new Texture(Gdx.files.internal("data/backdrop2.png"));
		
		Enemies = new ArrayList<OneGameObj>();
		
        for (int i=0; i < 5; i++)
        {
        	Enemies.add(new CrazedFan(Screen, new Vector3((float)(400 + (Math.random() * 150)), (float)(5 + Math.random() * 65), 0)));
        }
	}
}
