package com.shenko.onegame;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

public class PunchTest {
	
	public Rectangle Collision;
	public GridPoint2 Location;
	public int Health;
	
	public PunchTest(GridPoint2 Loc)
	{
		Collision = new Rectangle(Loc.x, Loc.y, 64, 96);
		Location = Loc;
		Health = 5;
	}
	
	public void GetPunched()
	{
		Health -= 1;
	}

}
