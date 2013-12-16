package com.shenko.onegame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;

public class OneGameRenderer {
	
	private OneGameScreen Screen;
	public OrthographicCamera Camera;
	private SpriteBatch Batch;
	private ShapeRenderer Shape;
	
	private Texture Background, Flash, Name1, Name2, Name3, Name4, Name5, Lives5, Lives4, Lives3, Lives2, Lives1;
	private BitmapFont Font;
	
	private boolean Debug = false;
	
	private List<OneGameObj> GameObjs;
	
	private Sound CameraSound;
	
	public OneGameRenderer(OneGameScreen inScreen)
	{
		Screen = inScreen;
		Background = new Texture(Gdx.files.internal("data/backdrop1.png"));
		Flash = new Texture(Gdx.files.internal("data/flash.tga"));
		
		Name1 = new Texture(Gdx.files.internal("data/name1.png"));
		Name2 = new Texture(Gdx.files.internal("data/name2.png"));
		Name3 = new Texture(Gdx.files.internal("data/name3.png"));
		Name4 = new Texture(Gdx.files.internal("data/name4.png"));
		Name5 = new Texture(Gdx.files.internal("data/name5.png"));
		
		Lives5 = new Texture(Gdx.files.internal("data/lives5.png"));
		Lives4 = new Texture(Gdx.files.internal("data/lives4.png"));
		Lives3 = new Texture(Gdx.files.internal("data/lives3.png"));
		Lives2 = new Texture(Gdx.files.internal("data/lives2.png"));
		Lives1 = new Texture(Gdx.files.internal("data/lives1.png"));
		
		CameraSound = Gdx.audio.newSound(Gdx.files.internal("data/camera.wav"));
		
		Batch = new SpriteBatch();
		Shape = new ShapeRenderer();
		Font = new BitmapFont();
		
		Camera = new OrthographicCamera();
		Camera.setToOrtho(false);
		Camera.update();
		
		GameObjs = new ArrayList<OneGameObj>();
		
		GameObjs.add(Screen.Guy);

		GameObjs.addAll(Screen.CurrentLevel.Enemies);

	}
	
	public void Render(float Delta)
	{
        Gdx.gl.glClearColor(0.55f, 0.55f, 0.55f, 1f);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        Camera.update();
        Batch.setProjectionMatrix(Camera.combined);
        
        Batch.begin();
        Batch.draw(Screen.CurrentLevel.Backdrop, 0, 00, 800, 400);
        
        Collections.sort(GameObjs, new Comparator<OneGameObj>() {
        	public int compare(OneGameObj a, OneGameObj b) {
        		if (a.y() > b.y()) { return -1; }
        		if (a.y() < b.y()) { return 1; }
        		return 0;
        	}
        });
        
        for (OneGameObj g : GameObjs)
        {
        	g.Draw(Batch);
        	
        	if (g.IsAttacking() && !g.IsDead() && g.getClass() == Paparazzi.class)
        	{
        		if (Math.random() * 100 < 2)
        		{
        			Batch.draw(Flash, g.GetLocation().x - 120, g.GetLocation().y - 30, 256, 256);
        			CameraSound.play();
        		}
        	}
        }
        
        Font.draw(Batch, "Guy health: " + Screen.Guy.Health, 10, 55);
       
        switch (Screen.Lives)
        {
        case 5:
        	Batch.draw(Lives5, 10, 400 - 35);
        	Batch.draw(Name5, 10, 5);
        	break;
        case 4:
        	Batch.draw(Lives4, 10, 400 - 35);
        	Batch.draw(Name4, 10, 5);
        	break;
        case 3:
        	Batch.draw(Lives3, 10, 400 - 35);
        	Batch.draw(Name3, 10, 5);
        	break;
        case 2:
        	Batch.draw(Lives2, 10, 400 - 35);
        	Batch.draw(Name2, 10, 5);
        	break;
        case 1:
        	Batch.draw(Lives1, 10, 400 - 35);
        	Batch.draw(Name1, 10, 5);
        	break;
        }
        
        Batch.end();
        
        if (Debug)
        {
	        Shape.begin(ShapeType.Line);
	        Shape.rect(Screen.Guy.Collision.x, Screen.Guy.Collision.y, Screen.Guy.Collision.width, Screen.Guy.Collision.height);
	        Shape.rect(Screen.Guy.PunchCollision.x, Screen.Guy.PunchCollision.y, Screen.Guy.PunchCollision.width, Screen.Guy.PunchCollision.height); 
	        
	        for (OneGameObj g : GameObjs)
	        {
	        	Shape.rect(g.GetCollision().x, g.GetCollision().y, g.GetCollision().width, g.GetCollision().height);
	        }
	        
	        Shape.end();
        }        
	}
	
	public void Reset()
	{
		GameObjs.clear();
		GameObjs.add(Screen.Guy);
		GameObjs.addAll(Screen.CurrentLevel.Enemies);
	}
	
	public GridPoint2 LocationToScreen(Vector3 inLoc)
	{
		GridPoint2 Ret = new GridPoint2();
		
		Ret.x = (int)inLoc.x;
		Ret.y = (int)(inLoc.y + inLoc.z);
		
		return Ret;
	}
	
}
