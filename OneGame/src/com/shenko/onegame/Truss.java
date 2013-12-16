package com.shenko.onegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Truss implements OneGameObj {

	public OneGameScreen Screen;
	public Vector3 Location;
	private int Speed;
	
	private Texture Tex;
	
	public int Mode, IdleTime;
	
	private float Alpha, IdleTimeElapsed;
	
	public Rectangle Collision;
	
	private Sound FallSound, HitSound;
	private boolean PlayedFalling;
	
	public Truss(OneGameScreen inScreen, Vector3 inLoc)
	{
		Screen = inScreen;
		Location = inLoc;
		Speed = (int)(200 - Math.random() * 50);
		
		Tex = new Texture(Gdx.files.internal("data/truss.tga"));
		
		FallSound = Gdx.audio.newSound(Gdx.files.internal("data/trussfall.wav"));
		HitSound = Gdx.audio.newSound(Gdx.files.internal("data/trusshit.wav"));
		
		Mode = 0;
		Alpha = 1f;
		IdleTimeElapsed = 0f;
		IdleTime = 2;
		
		Collision = new Rectangle(Location.x, Location.y, 128, 48);
		PlayedFalling = false;
	}
	
	@Override
	public int y() {
		return (int)Location.y;
	}

	@Override
	public void Update(float Delta) {
		if (Mode == 0)
		{
			Location.y -= Speed * Delta;
			
			if (Location.y < 400 && !PlayedFalling)
			{
				FallSound.play();
				PlayedFalling = true;
			}
		
			Collision.setPosition(Location.x, Location.y);
			
			if (Location.y <= 10)
			{
				Mode = 1;
				HitSound.play();
			}
			
			// Check collision
			CheckCollision();
		}
		else if (Mode == 1)
		{
			IdleTimeElapsed += Delta;
			if (IdleTimeElapsed > IdleTime)
			{
				Mode = 2;
			}
		}
		else if (Mode == 2)
		{
			Alpha -= 0.01f;
			if (Alpha < 0)
			{
				Alpha = 0f;
			}
		}
	}
	
	private void CheckCollision()
	{
		if (Collision.overlaps(Screen.Guy.Collision))
		{
			// Landed on the guy, hurt him
			Screen.Guy.Kill();
		}
		
		for (OneGameObj o : Screen.CurrentLevel.Enemies)
		{
			if (Collision.overlaps(o.GetCollision()) && !o.IsDead() && o.getClass() != Truss.class && o.getClass() != Bieber.class)
			{
				// Landed on another enemy
				o.Kill();
			}
		}
	}

	@Override
	public void Draw(SpriteBatch Batch) {
		if (Alpha > 0)
		{
			Batch.setColor(1f, 1f, 1f, Alpha);
			Batch.draw(Tex, Location.x, Location.y);
			Batch.setColor(1f, 1f, 1f, 1f);
		}
	}
	
	@Override
	public Vector3 GetLocation() {
		return Location;
	}

	@Override
	public boolean IsDead() {
		if (Mode == 2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean IsAttacking() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Rectangle GetCollision() {
		return Collision;
	}

	@Override
	public void GetPunched() {
		if (Mode == 1)
		{
			Kill();
		}
	}

	@Override
	public void Kill() {
		Mode = 2;
	}

}
