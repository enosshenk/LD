package com.shenko.onegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.shenko.onegame.OneGameGuy.EDir;
import com.shenko.onegame.Paparazzi.EFace;

public class Bieber implements OneGameObj {
	private OneGameScreen Screen;
	
	public Vector3 Location;
	
	public enum EDir { Left, Right, None };
	public EDir Dir;
	
	public enum EUp { Up, Down, None };
	public EUp Up;
	
	public enum EFace { Left, Right };
	public EFace Face;
	
	private boolean Moving, Attacking;
	public boolean Dead;
	private boolean ShowHit;
	
	private int Health, AttackNum;
	
	private int Speed, CollisionDist;
	private float AnimTime, DeathShow;
	
	private Texture WalkTexture;
	private TextureRegion[] WalkFrames;
	private Texture AttackTexture;
	private TextureRegion[] AttackFrames1;
	private TextureRegion[] AttackFrames2;
	private Texture DieTexture;
	private TextureRegion[] DieFrames;
	private Animation DieAnim;
	private Animation WalkAnim;
	private Animation AttackAnim1;
	private Animation AttackAnim2;
	
	public TextureRegion Bieber;
	public Rectangle Collision;
	
	private float AttackTime;
	
	private Sound Aggro, Die;
	private boolean HasAggrod;
	
	public Bieber(OneGameScreen inScreen, Vector3 inLoc)
	{
		Screen = inScreen;
		
		Location = inLoc;
		WalkTexture = new Texture(Gdx.files.internal("data/bieberwalk.png"));
		DieTexture = new Texture(Gdx.files.internal("data/bieberdie.png"));
		AttackTexture = new Texture(Gdx.files.internal("data/bieberpunch.png"));
		
		Aggro = Gdx.audio.newSound(Gdx.files.internal("data/bieberaggro.wav"));
		Die = Gdx.audio.newSound(Gdx.files.internal("data/bieberdie.wav"));
		HasAggrod = false;
		
		Speed = 75;
		CollisionDist = 30;
		Dir = EDir.Left;
		Up = EUp.Up;
		Face = EFace.Left;
		
		Moving = false;
		Attacking = false;
		AnimTime = 0f;
		
		AttackNum = 0;
		DeathShow = 0f;
		
		Health = 10;
		Dead = false;
		ShowHit = false;
		
		Collision = new Rectangle(Location.x + 32, Location.y, 64, 96);
		InitFrames();
	}
	
	public void Update(float Delta)
	{
		if (!HasAggrod)
		{
			Aggro.play();
			HasAggrod = true;
		}
		
		if (!Dead)
		{
			AnimTime += Delta;
			
			// Handle logic to chase the player
			if (Location.dst(Screen.Guy.Location) > 50)
			{
				Attacking = false;
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
				Bieber = WalkAnim.getKeyFrame(AnimTime);
			}
			else
			{
				Bieber = WalkFrames[0];				
			}
			
			if (Attacking)
			{
				AnimTime += Delta;
				if (AttackNum == 0)
				{
					Bieber = AttackAnim1.getKeyFrame(AnimTime);
				}
				else
				{
					Bieber = AttackAnim2.getKeyFrame(AnimTime);
				}
				
				AttackTime += Delta;
				if ((int)AttackTime > 1)
				{
					Screen.Guy.GetPunched();
					AttackTime = 0f;
					
					if (AttackNum == 0)
					{
						AttackNum = 1;
					}
					else
					{
						AttackNum = 0;
					}
				}
				
			}
			
			Collision.setPosition(Location.x + 64, Location.y);
		}
		else
		{
			if (AnimTime < 1.28)
			{
				AnimTime += Delta;
				Bieber = DieAnim.getKeyFrame(AnimTime);
			}
			else
			{
				Bieber = DieFrames[15];
				DeathShow += Delta;
				if (DeathShow > 4)
				{
					Screen.WinGame();
				}
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
			
			Die.play();
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
		WalkAnim = new Animation(0.08f, WalkFrames);
		WalkAnim.setPlayMode(Animation.LOOP);
		
		DieFrames = new TextureRegion[16];
		
		FanRegion = TextureRegion.split(DieTexture, 128, 128);

		i = 0;
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 16)
				{
					DieFrames[i++] = FanRegion[x][y];
				}
			}
		}
		DieAnim = new Animation(0.08f, DieFrames);
		
		AttackFrames1 = new TextureRegion[6];
		AttackFrames2 = new TextureRegion[6];
		
		FanRegion = TextureRegion.split(AttackTexture, 128, 128);

		i = 0;
		int j = 0;
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 6)
				{
					AttackFrames1[i++] = FanRegion[x][y];
				}
				else if (j < 6)
				{
					AttackFrames2[j++] = FanRegion[x][y];
				}
			}
		}
		AttackAnim1 = new Animation(0.08f, AttackFrames1);
		AttackAnim2 = new Animation(0.08f, AttackFrames2);
		AttackAnim1.setPlayMode(Animation.LOOP);
		AttackAnim2.setPlayMode(Animation.LOOP);
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
    		Batch.draw(Bieber, Location.x, Location.y, 64, 0, 128, 128, -1, 1, 0);
    	}
    	else
    	{
    		Batch.draw(Bieber, Location.x, Location.y, 64, 0, 128, 128, 1, 1, 0);
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
	public void Kill() {
		Dead = true;
		AnimTime = 0f;
	}

	@Override
	public Rectangle GetCollision() {
		return Collision;
	}

	@Override
	public boolean IsAttacking() {
		return Attacking;
	}
}
