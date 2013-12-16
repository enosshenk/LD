package com.shenko.onegame;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class OneGameScreen implements Screen {
	private OneGame Game;
	
	public OneGameGuy Guy;
	public OneGameRenderer Renderer;
	
	// Guess who learned about lists halfway through this?
	public List<OneGameLevel> Levels;
	public OneGameLevel CurrentLevel;
	public int CurrentLevelIndex;
	
	public int Lives;
	
	public OneGameScreen(OneGame inGame)
	{
		Game = inGame;
	}
	
	@Override
	public void render(float delta) {
		if (Guy != null)
		{
			Guy.Update(delta);
		}
		
		if (CurrentLevel != null)
		{
			CurrentLevel.Update(delta);
		}
		
		for (OneGameObj e : CurrentLevel.Enemies)
		{
			e.Update(delta);
		}
		
		Renderer.Render(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Levels = new ArrayList<OneGameLevel>();
		
		CurrentLevelIndex = 0;
		Levels.add(new Level1(this));
		Levels.add(new Level2(this));
		Levels.add(new Level3(this));
		Levels.add(new Level4(this));
		Levels.add(new Level5(this));
		Levels.add(new Level6(this));
		CurrentLevel = Levels.get(CurrentLevelIndex);
		
		Guy = new OneGameGuy(this);
		
		Renderer = new OneGameRenderer(this);
		
		Lives = 5;
		
        OneKeyProcessor InputProcessor = new OneKeyProcessor();
        Gdx.input.setInputProcessor(InputProcessor);
	}
	
	public void NextLevel()
	{
		CurrentLevelIndex += 1;
		CurrentLevel = Levels.get(CurrentLevelIndex);
		Guy.Reset();
		Renderer.Reset();
	}
	
	public void WinGame()
	{
		Game.WinGame();
	}
	
	public void LoseGame()
	{
		Game.LoseGame();
	}
	
	public class OneKeyProcessor implements InputProcessor
	{

		@Override
		public boolean keyDown(int keycode) {
		//	System.out.print(keycode + "\n");
			if (keycode == Keys.LEFT)
			{
				Guy.StartLeft();
			}
			else if (keycode == Keys.RIGHT)
			{
				Guy.StartRight();
			}
			if (keycode == Keys.UP)
			{
				Guy.StartUp();
			}
			else if (keycode == Keys.DOWN)
			{
				Guy.StartDown();
			}
			else if (keycode == Keys.W)
			{
				Guy.Jump();
			}
			else if (keycode == Keys.S)
			{
				Guy.Punch();
			}
			
			
			return true;			
		}

		@Override
		public boolean keyUp(int keycode) {
			if (keycode == Keys.LEFT)
			{
				Guy.StopLeft();
			}
			else if (keycode == Keys.RIGHT)
			{
				Guy.StopRight();
			}
			if (keycode == Keys.UP)
			{
				Guy.StopUp();
			}
			else if (keycode == Keys.DOWN)
			{
				Guy.StopDown();
			}
			else if (keycode == Keys.S)
			{
				Guy.ResetPunch();
			}
			
			return true;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
		
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
