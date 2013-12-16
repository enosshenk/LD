package com.shenko.onegame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Level6 extends OneGameLevel {
	
	private float TrussTime, TrussTimeElapsed;
	
	public Level6(OneGameScreen inScreen)
	{
		Screen = inScreen;
		Backdrop = new Texture(Gdx.files.internal("data/backdrop5.png"));
		
		Enemies = new ArrayList<OneGameObj>();

		TrussTimeElapsed = 0f;
		TrussTime = 4f;
		
		Enemies.add(new Bieber(Screen, new Vector3(650, 30, 0)));
	}
	
	public void Update(float Delta)
	{
		TrussTimeElapsed += Delta;
		if (TrussTimeElapsed > TrussTime)
		{
			Enemies.add(new Truss(Screen, new Vector3((float)(50 + (Math.random() * 700)), 450, 0)));
			TrussTimeElapsed = 0f;
			Screen.Renderer.Reset();
		}
	}
}
