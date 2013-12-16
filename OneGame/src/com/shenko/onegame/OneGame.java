package com.shenko.onegame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class OneGame extends Game {

	@Override
	public void create() {
		setScreen(new LogoScreen(this));
	}
	
	public void LogoDone()
	{
		setScreen(new StartScreen(this));
	}
	
	public void StartGame()
	{
		setScreen(new StoryScreen(this));
	}
	
	public void StartGame2()
	{
		setScreen(new OneGameScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
	
	public void WinGame()
	{
		setScreen(new FailScreen(this));
	}
	
	public void LoseGame()
	{
		setScreen(new WinScreen(this));		
	}

}
