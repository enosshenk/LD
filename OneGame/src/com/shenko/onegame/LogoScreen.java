package com.shenko.onegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LogoScreen implements Screen {
	
	private OneGame Game;
	
	private Texture Logo;
	private float Alpha, HoldTime;
	private int Mode;
	
	private SpriteBatch Batch;
	
	public LogoScreen(OneGame inGame)
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
	        	Batch.draw(Logo, 256, 148, 287, 104);
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
        	
        	if (HoldTime < 5)
        	{
	        	Batch.begin();
	        	Batch.setColor(1f, 1f, 1f, Alpha);
	        	Batch.draw(Logo, 256, 148, 287, 104);
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
	        	Batch.draw(Logo, 256, 148, 287, 104);
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
		Logo = new Texture(Gdx.files.internal("data/shi.png"));
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
