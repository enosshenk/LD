package com.shenko.onegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FailScreen implements Screen {
	
	private OneGame Game;
	
	private Texture WinScreen;
	private float Alpha, HoldTime;
	private int Mode;
	
	private SpriteBatch Batch;
	
	public FailScreen(OneGame inGame)
	{
		Game = inGame;
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
        if (Mode == 0)
        {
        	// Fade in
        	Alpha += 0.02;
        	
        	if (Alpha < 1)
        	{
	        	Batch.begin();
	        	Batch.setColor(1f, 1f, 1f, Alpha);
	        	Batch.draw(WinScreen, 0, 0, 800, 400);
	        	Batch.end();
        	}
        	else
        	{
        		Alpha = 1f;
        		Mode = 1;
        	}
        }
        if (Mode == 1)
        {
        	// Hold
        	HoldTime += delta;
        	
        	if (HoldTime < 3)
        	{
	        	Batch.begin();
	        	Batch.setColor(1f, 1f, 1f, Alpha);
	        	Batch.draw(WinScreen, 0, 0, 800, 400);
	        	Batch.end();
        	}
        	else
        	{        		
        		Mode = 2;        		
        	}
        }
        if (Mode == 2)
        {
        	Alpha -= 0.02;
        	
        	if (Alpha > 0)
        	{
	        	Batch.begin();
	        	Batch.setColor(1f, 1f, 1f, Alpha);
	        	Batch.draw(WinScreen, 0, 0, 800, 400);
	        	Batch.end();
        	}
        	else
        	{
        		// End
        		Game.LogoDone();
        	}
        }		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		WinScreen = new Texture(Gdx.files.internal("data/winscreen.png"));
		Alpha = 0f;
		HoldTime = 0f;
		
		Mode = 0;
		
		Batch = new SpriteBatch();	
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	

}
