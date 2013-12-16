package com.shenko.onegame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public interface OneGameObj {
	public int y();
	
	public void Draw(SpriteBatch Batch);
	
	public void Update(float Delta);
	
	public Vector3 GetLocation();
	
	public boolean IsDead();
	public boolean IsAttacking();
	
	public Rectangle GetCollision();
	
	public void GetPunched();
	public void Kill();
}
