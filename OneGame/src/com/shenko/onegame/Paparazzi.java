package com.shenko.onegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.shenko.onegame.CrazedFan.EFace;
import com.shenko.onegame.OneGameGuy.EDir;

public class Paparazzi implements OneGameObj {
	private OneGameScreen Screen;
	
	public Vector3 Location;
	
	public enum EDir { Left, Right, None };
	public EDir Dir;
	
	public enum EFace { Left, Right };
	public EFace Face;
	
	public enum EUp { Up, Down, None };
	public EUp Up;
	
	private boolean Moving, Attacking;
	public boolean Dead;
	private boolean ShowHit;
	
	private int Health;
	
	private int Speed, CollisionDist;
	private float AnimTime;
	
	private Texture WalkTexture;
	private TextureRegion[] WalkFrames;
	private Texture AttackTexture;
	private TextureRegion[] AttackFrames;
	private Texture DieTexture;
	private TextureRegion[] DieFrames;
	private Animation DieAnim;
	private Animation WalkAnim;
	private Animation AttackAnim;
	
	public TextureRegion Paparazzi;
	public Rectangle Collision;
	
	public Paparazzi(OneGameScreen inScreen, Vector3 inLoc)
	{
		Screen = inScreen;
		
		Location = inLoc;
		WalkTexture = new Texture(Gdx.files.internal("data/papwalk.png"));
		DieTexture = new Texture(Gdx.files.internal("data/papdie.png"));
		AttackTexture = new Texture(Gdx.files.internal("data/papshoot.png"));
		
		Speed = 100;
		CollisionDist = 30;
		Dir = EDir.Left;
		Up = EUp.Up;
		Face = EFace.Left;
		
		Moving = false;
		Attacking = false;
		AnimTime = 0f;
		
		ShowHit = false;
		
		Health = 2;
		Dead = false;
		
		Collision = new Rectangle(Location.x + 32, Location.y, 64, 96);
		InitFrames();
	}
	
	public void Update(float Delta)
	{
		if (!Dead)
		{
			// Handle logic to chase the player
			if (Location.dst(Screen.Guy.Location) > 256 && Location.dst(Screen.Guy.Location) < 300)
			{
				if (Screen.Guy.Location.x < Location.x)
				{
					Dir = EDir.Left;
					Face = EFace.Left;
				}
				else if (Screen.Guy.Location.x > Location.x)
				{
					Dir = EDir.Right;
					Face = EFace.Right;
				}
				else
				{
					Dir = EDir.None;
				}
				
				if (Screen.Guy.Location.y + Screen.Guy.Location.z > Location.y)
				{
					Up = EUp.Up;
				}
				else if (Screen.Guy.Location.y + Screen.Guy.Location.z < Location.y)
				{
					Up = EUp.Down;
				}
			}
			else
			{
				if (Screen.Guy.Location.x < Location.x)
				{
					Face = EFace.Left;
					
				}
				else if (Screen.Guy.Location.x > Location.x)
				{
					Face = EFace.Right;
				}
				
				Dir = EDir.None;
				Up = EUp.None;
				Attacking = true;
			}
			
			if (Dir == EDir.Left)
			{
				if (CanMoveLeft())
				{
					Location.x -= Speed * Delta;
					Moving = true;
					AnimTime += Delta;
				}
			}
			else if (Dir == EDir.Right)
			{
				if (CanMoveRight())
				{
					Location.x += Speed * Delta;
					Moving = true;
					AnimTime += Delta;
				}
			}
			
			if (Up == EUp.Up)
			{
				if (CanMoveUp())
				{
					Location.y += (Speed / 10) * Delta;
					Moving = true;
					AnimTime += Delta;
				}
			}
			else if (Up == EUp.Down)
			{
				if (CanMoveDown())
				{
					Location.y -= (Speed / 10) * Delta;
					Moving = true;
					AnimTime += Delta;
				}
			}
	
			if (Dir == EDir.None && Up == EUp.None)
			{
				Moving = false;
			}
			
			if (Moving)
			{
				Paparazzi = WalkAnim.getKeyFrame(AnimTime);
			}
			else
			{
				if (Attacking)
				{
					AnimTime += Delta;
					Paparazzi = AttackAnim.getKeyFrame(AnimTime);
				}
				else
				{
					Paparazzi = WalkFrames[0];
				}
			}
			
			Collision.setPosition(Location.x + 32, Location.y);
		}
		else
		{
			if (AnimTime < 0.55)
			{
				AnimTime += Delta;
				Paparazzi = DieAnim.getKeyFrame(AnimTime);
			}
			else
			{
				Paparazzi = DieFrames[10];
			}
		}
	}
	
	public void GetPunched()
	{
		Health -= 1;
		ShowHit = true;
		
		if (Health <= 0)
		{
			// Dead
			Dead = true;
			AnimTime = 0f;
		}
	}
	
	private boolean CanMoveLeft()
	{
		boolean RetVal = true;

		if (Location.x < 10)
		{
			RetVal = false;
		}
		if (Math.abs(Location.x - Screen.Guy.Location.x) < CollisionDist 
				&& Math.abs(Location.y - (Screen.Guy.Location.y + Screen.Guy.Location.z)) < CollisionDist
				&& Screen.Guy.Location.x < Location.x)
		{
			RetVal = false;
		}
		
		for (OneGameObj f : Screen.CurrentLevel.Enemies)
		{
			if (!f.equals(this))
			{
				if (Math.abs(Location.x - f.GetLocation().x) < CollisionDist
						&& Math.abs(Location.y - f.GetLocation().y) < CollisionDist
						&& f.GetLocation().x < Location.x
						&& !f.IsDead())
				{
					RetVal = false;
				}
			}
		}
		
		return RetVal;
	}

	private boolean CanMoveRight()
	{
		boolean RetVal = true;

		if (Location.x > 790)
		{
			RetVal = false;
		}
		if (Math.abs(Location.x - Screen.Guy.Location.x) < CollisionDist 
				&& Math.abs(Location.y - (Screen.Guy.Location.y + Screen.Guy.Location.z)) < CollisionDist
				&& Screen.Guy.Location.x > Location.x)
		{
			RetVal = false;
		}
		
		for (OneGameObj f : Screen.CurrentLevel.Enemies)
		{
			if (!f.equals(this))
			{
				if (Math.abs(Location.x - f.GetLocation().x) < CollisionDist
						&& Math.abs(Location.y - f.GetLocation().y) < CollisionDist
						&& f.GetLocation().x > Location.x
						&& !f.IsDead())
				{
					RetVal = false;
				}
			}
		}
		
		return RetVal;
	}
	
	private boolean CanMoveUp()
	{
		boolean RetVal = true;

		if (Location.y > 45)
		{
			RetVal = false;
		}
		if (Math.abs(Location.x - Screen.Guy.Location.x) < CollisionDist 
				&& Math.abs(Location.y - (Screen.Guy.Location.y + Screen.Guy.Location.z)) < CollisionDist
				&& (Screen.Guy.Location.y + Screen.Guy.Location.z) > Location.y)
		{
			RetVal = false;
		}
		
		for (OneGameObj f : Screen.CurrentLevel.Enemies)
		{
			if (!f.equals(this))
			{
				if (Math.abs(Location.x - f.GetLocation().x) < 10
						&& Math.abs(Location.y - f.GetLocation().y) < 10
						&& f.GetLocation().y > Location.y
						&& !f.IsDead())
				{
					RetVal = false;
				}
			}
		}
		
		return RetVal;
	}
	
	private boolean CanMoveDown()
	{
		boolean RetVal = true;

		if (Location.y < 1)
		{
			RetVal = false;
		}
		if (Math.abs(Location.x - Screen.Guy.Location.x) < CollisionDist 
				&& Math.abs(Location.y - (Screen.Guy.Location.y + Screen.Guy.Location.z)) < CollisionDist
				&& (Screen.Guy.Location.y + Screen.Guy.Location.z) < Location.y)
		{
			RetVal = false;
		}
		
		for (OneGameObj f : Screen.CurrentLevel.Enemies)
		{
			if (!f.equals(this))
			{
				if (Math.abs(Location.x - f.GetLocation().x) < CollisionDist
						&& Math.abs(Location.y - f.GetLocation().y) < CollisionDist
						&& f.GetLocation().y < Location.y
						&& !f.IsDead())
				{
					RetVal = false;
				}
			}
		}
		
		return RetVal;
	}
	
	private void InitFrames()
	{
		TextureRegion[][] FanRegion;		
		WalkFrames = new TextureRegion[11];
		
		FanRegion = TextureRegion.split(WalkTexture, 128, 128);
		int i = 0;
		
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 11)
				{
					WalkFrames[i++] = FanRegion[x][y];
				}
				else
				{
					break;
				}
			}
		}
		WalkAnim = new Animation(0.05f, WalkFrames);
		WalkAnim.setPlayMode(Animation.LOOP);
		
		DieFrames = new TextureRegion[11];
		
		FanRegion = TextureRegion.split(DieTexture, 128, 128);

		i = 0;
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 11)
				{
					DieFrames[i++] = FanRegion[x][y];
				}
				else
				{
					break;
				}
			}
		}
		DieAnim = new Animation(0.05f, DieFrames);
		
		AttackFrames = new TextureRegion[11];
		
		FanRegion = TextureRegion.split(AttackTexture, 128, 128);

		i = 0;
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 11)
				{
					AttackFrames[i++] = FanRegion[x][y];
				}
				else
				{
					break;
				}
			}
		}
		AttackAnim = new Animation(0.08f, AttackFrames);
		AttackAnim.setPlayMode(Animation.LOOP);
	}

	@Override
	public int y() {
		return (int)Location.y;
	}

	@Override
	public void Draw(SpriteBatch Batch) {
		if (ShowHit)
		{			
			Batch.setColor(1f, 0.2f, 0.2f, 1f);
			ShowHit = false;
		}
    	if (Face == EFace.Left)
    	{
    		Batch.draw(Paparazzi, Location.x, Location.y, 64, 0, 128, 128, -1, 1, 0);
    	}
    	else
    	{
    		Batch.draw(Paparazzi, Location.x, Location.y, 64, 0, 128, 128, 1, 1, 0);
    	}	
    	Batch.setColor(1f, 1f, 1f, 1f);
	}

	@Override
	public Vector3 GetLocation() {
		return Location;
	}

	@Override
	public boolean IsDead() {		
		return Dead;
	}

	@Override
	public Rectangle GetCollision() {
		return Collision;
	}

	@Override
	public boolean IsAttacking() {
		return Attacking;
	}

	@Override
	public void Kill() {
		Dead = true;
		AnimTime = 0f;
	}
}
