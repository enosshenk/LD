package com.shenko.onegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StoryScreen implements Screen {
	
	private OneGame Game;
	private Texture Backdrop, StartPrompt;
	private SpriteBatch Batch;

	public StoryScreen(OneGame inGame)
	{
		Game = inGame;
	}
	
	@Override
	public void render(float delta) {
	
		Batch.begin();

		Batch.draw(Backdrop, 0, 0, 800, 400);

		Batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Backdrop = new Texture(Gdx.files.internal("data/storyscreen.png"));
		Batch = new SpriteBatch();

		
        OneKeyProcessor InputProcessor = new OneKeyProcessor();
        Gdx.input.setInputProcessor(InputProcessor);
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
	
	public class OneKeyProcessor implements InputProcessor
	{

		@Override
		public boolean keyDown(int keycode) {
			Game.StartGame2();

			return true;
		}
	
		@Override
		public boolean keyUp(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			Game.StartGame2();
			
			return true;
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

}
