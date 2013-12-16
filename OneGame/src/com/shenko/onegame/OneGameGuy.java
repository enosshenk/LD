package com.shenko.onegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

public class OneGameGuy implements OneGameObj {
	
	private OneGameScreen Screen;


	private TextureRegion[] WalkFrames;
	private TextureRegion[] JumpFrames;
	private TextureRegion[] Punch1Frames;
	private TextureRegion[] Punch2Frames;	
	
	private Texture Guy1WalkTexture;
	private Texture Guy1JumpTexture;
	private Texture Guy1PunchTexture;
	
	private Texture Guy2WalkTexture;
	private Texture Guy2JumpTexture;
	private Texture Guy2PunchTexture;
	
	private Texture Guy3WalkTexture;
	private Texture Guy3JumpTexture;
	private Texture Guy3PunchTexture;
	
	private Texture Guy4WalkTexture;
	private Texture Guy4JumpTexture;
	private Texture Guy4PunchTexture;
	
	private Texture Guy5WalkTexture;
	private Texture Guy5JumpTexture;
	private Texture Guy5PunchTexture;
	
	public TextureRegion Guy;
	public Animation WalkAnim, JumpAnim, Punch1Anim, Punch2Anim;
	private float AnimTime;
	
	public enum EMode { Idle, Walking, Jumping, Punching };
	public EMode Mode;
	
	public enum EDir { Left, Right };
	public EDir Dir;
	public boolean LeftDown, RightDown, UpDown, DownDown;
	private int PunchHand;
	private boolean CanPunch;
	
	public int Health;
	
	private int CollisionDist;
	
	public Vector3 Location;
	public Vector3 Velocity;
	
	public Rectangle Collision, PunchCollision;
	
	public int CurrentGuy;
	
	private Sound JumpSound, PunchSound;
	
	public boolean ShowHit;
	
	public OneGameGuy(OneGameScreen inScreen)
	{
		Screen = inScreen;
		Guy1WalkTexture = new Texture(Gdx.files.internal("data/guywalk.png"));
		Guy1JumpTexture = new Texture(Gdx.files.internal("data/guyjump.png"));
		Guy1PunchTexture = new Texture(Gdx.files.internal("data/guypunch.png"));
		Guy2WalkTexture = new Texture(Gdx.files.internal("data/guy2walk.png"));
		Guy2JumpTexture = new Texture(Gdx.files.internal("data/guy2jump.png"));
		Guy2PunchTexture = new Texture(Gdx.files.internal("data/guy2punch.png"));
		Guy3WalkTexture = new Texture(Gdx.files.internal("data/guy3walk.png"));
		Guy3JumpTexture = new Texture(Gdx.files.internal("data/guy3jump.png"));
		Guy3PunchTexture = new Texture(Gdx.files.internal("data/guy3punch.png"));
		Guy4WalkTexture = new Texture(Gdx.files.internal("data/guy4walk.png"));
		Guy4JumpTexture = new Texture(Gdx.files.internal("data/guy4jump.png"));
		Guy4PunchTexture = new Texture(Gdx.files.internal("data/guy4punch.png"));
		Guy5WalkTexture = new Texture(Gdx.files.internal("data/guy5walk.png"));
		Guy5JumpTexture = new Texture(Gdx.files.internal("data/guy5jump.png"));
		Guy5PunchTexture = new Texture(Gdx.files.internal("data/guy5punch.png"));	
		
		JumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
		PunchSound = Gdx.audio.newSound(Gdx.files.internal("data/punch.wav"));
		
		Mode = EMode.Idle;
		Dir = EDir.Right;
		
		Location = new Vector3(50, 0, 0);
		Velocity = new Vector3();
		
		LeftDown = false;
		RightDown = false;
		PunchHand = 0;
		CanPunch = true;
		
		Health = 5;
		ShowHit = false;
		
		CollisionDist = 30;
		
		Collision = new Rectangle(Location.x + 32, Location.y + Location.z, 64, 96);
		PunchCollision = new Rectangle(Location.x + 64, Location.y + Location.z + 50, 72, 32);
		
		CurrentGuy = 1;
		InitGuyFrames(CurrentGuy);
		
		Guy = new TextureRegion();		
	}
	
	public void Update(float Delta)
	{
	
		if (Mode != EMode.Punching)
		{
			if (LeftDown)
			{
				if (CanMoveLeft())
				{
					Velocity.x = -150f;
				}
				else
				{
					Velocity.x = 0;
				}
				Dir = EDir.Left;
			}			
			else if (RightDown)
			{
				if (CanMoveRight())
				{
					Velocity.x = 150f;
				}
				else
				{
					Velocity.x = 0;
				}
				Dir = EDir.Right;
			}
			
			if (UpDown)
			{
				if (CanMoveUp())
				{
					Velocity.y = 40f;
				}
				else
				{
					Velocity.y = 0;
				}
			}
			else if (DownDown)
			{
				if (CanMoveDown())
				{
					Velocity.y = -40f;
				}
				else
				{
					Velocity.y = 0;
				}
			}

			Location.x += Velocity.x * Delta;
			Location.y += Velocity.y * Delta;
			Location.z += Velocity.z * Delta;
			
			if (Location.z > 0)
			{
				Velocity.z -= 10;
				Mode = EMode.Jumping;
				AnimTime += Delta;
				Guy = JumpAnim.getKeyFrame(AnimTime);
			}
			else
			{
				Velocity.z = 0;
				
				if (Velocity.x != 0 || Velocity.y != 0)
				{
					Mode = EMode.Walking;
					AnimTime += Delta;
					Guy = WalkAnim.getKeyFrame(AnimTime);
					
					if (!LeftDown && !RightDown)
					{
						Velocity.x *= 0.8;
						if (Math.abs(Velocity.x) < 1)
						{
							Velocity.x = 0;
						}
					}
					if (!UpDown && !DownDown)
					{
						Velocity.y *= 0.8;
						if (Math.abs(Velocity.y) < 1)
						{
							Velocity.y = 0;
						}
					}
				}
				else
				{
					Mode = EMode.Idle;
					Guy = WalkFrames[0];
				}
			}		
		}
		else
		{
			// Handle punch stuff
			AnimTime += Delta;
			if (AnimTime < 0.35)
			{
				if (PunchHand == 0)
				{
					Guy = Punch1Anim.getKeyFrame(AnimTime);
				}
				else
				{
					Guy = Punch2Anim.getKeyFrame(AnimTime);
				}
			}
			else
			{
				Mode = EMode.Idle;
			}
		}
		
		// Update collision rec
		Collision.setPosition(Location.x + 32, Location.y + Location.z);
		if (Dir == EDir.Right)
		{
			PunchCollision.setPosition(Location.x + 64, Location.y + Location.z + 50);
		}
		else
		{
			PunchCollision.setPosition(Location.x - 8, Location.y + Location.z + 50);
		}
		
		// Handle screen edge trigger
		if (Location.x > 730 && Screen.CurrentLevelIndex != 6)
		{
			Screen.NextLevel();
		}
		
	}
	
	public void StartLeft()
	{
		if (Mode == EMode.Idle || Mode == EMode.Walking)
		{
			LeftDown = true;
			AnimTime = 0f;
		}
		else
		{
			LeftDown = true;
		}
	}
	
	public void StopLeft()
	{
		LeftDown = false;
	}
	
	public void StartRight()
	{
		if (Mode == EMode.Idle || Mode == EMode.Walking)
		{
			RightDown = true;
			AnimTime = 0f;
		}
		else
		{
			RightDown = true;
		}
	}
	
	public void StopRight()
	{
		RightDown = false;
	}
	
	public void StartUp()
	{
		if (Mode == EMode.Idle || Mode == EMode.Walking)
		{
			UpDown = true;
			AnimTime = 0f;
		}
		else
		{
			UpDown = true;
		}
	}
	
	public void StopUp()
	{
		UpDown = false;
	}
	
	public void StartDown()
	{
		if (Mode == EMode.Idle || Mode == EMode.Walking)
		{
			DownDown = true;
			AnimTime = 0f;
		}
		else
		{
			DownDown = true;
		}
	}
	
	public void StopDown()
	{
		DownDown = false;
	}	
	
	public void Jump()
	{
		if (Mode == EMode.Idle || Mode == EMode.Walking)
		{
			Velocity.z = 300;
			AnimTime = 0f;
			JumpSound.play();
		}
	}
	
	public void Punch()
	{
		if ((Mode == EMode.Idle || Mode == EMode.Walking) && CanPunch )
		{
			if (PunchHand == 0)
			{
				PunchHand = 1;
			}
			else
			{
				PunchHand = 0;
			}
			
			AnimTime = 0f;
			Mode = EMode.Punching;
			
			for (OneGameObj f : Screen.CurrentLevel.Enemies)
			{
				if (PunchCollision.overlaps(f.GetCollision()) && !f.IsDead())
				{
					f.GetPunched();
					break;
				}
			}
			
			PunchSound.play();
			CanPunch = false;
		}
	}
	
	public void ResetPunch()
	{
		CanPunch = true;
	}
	
	public void Reset()
	{
		Location.x = 30;
		Location.y = 3;
		Location.z = 0;
		
		Velocity.x = 0;
		Velocity.y = 0;
		Velocity.z = 0;
		
		CanPunch = true;
		
		Dir = EDir.Right;
	}
	
	public void ResetDeath()
	{
		Location.x = 30;
		Location.y = 3;
		Location.z = 0;
		
		Velocity.x = 0;
		Velocity.y = 0;
		Velocity.z = 0;
		
		CanPunch = true;
		
		Dir = EDir.Right;
		Health = 5;
		InitGuyFrames(CurrentGuy);
	}
	
	private boolean CanMoveLeft()
	{
		boolean RetVal = true;

		if (Location.x < 10)
		{
			RetVal = false;
		}
		
		for (OneGameObj f : Screen.CurrentLevel.Enemies)
		{
			if (Math.abs(Location.x - f.GetLocation().x) < CollisionDist
					&& Math.abs(Location.y - f.GetLocation().y) < CollisionDist
					&& f.GetLocation().x < Location.x
					&& !f.IsDead())
			{
				RetVal = false;
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
		
		for (OneGameObj f : Screen.CurrentLevel.Enemies)
		{
			if (Math.abs(Location.x - f.GetLocation().x) < CollisionDist
					&& Math.abs(Location.y - f.GetLocation().y) < CollisionDist
					&& f.GetLocation().x > Location.x
					&& !f.IsDead())
			{
				RetVal = false;
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
		
		for (OneGameObj f : Screen.CurrentLevel.Enemies)
		{
			if (Math.abs(Location.x - f.GetLocation().x) < 10
					&& Math.abs(Location.y - f.GetLocation().y) < 10
					&& f.GetLocation().y > Location.y
					&& !f.IsDead())
			{
				RetVal = false;
			}
		}
		
		return RetVal;
	}
	
	private boolean CanMoveDown()
	{
		if (Location.y < 1)
		{
			return false;
		}
		
		for (OneGameObj f : Screen.CurrentLevel.Enemies)
		{
			if (Math.abs(Location.x - f.GetLocation().x) < CollisionDist
					&& Math.abs(Location.y - f.GetLocation().y) < CollisionDist
					&& f.GetLocation().y < Location.y
					&& !f.IsDead())
			{
				return false;
			}
		}
		
		return true;
	}
	
	private void InitGuyFrames(int WhichGuy)
	{
		// Get walk animation frames
		TextureRegion[][] GuyRegion;
		WalkFrames = new TextureRegion[11];
		
		switch (WhichGuy)
		{
		case 1: GuyRegion = TextureRegion.split(Guy1WalkTexture, 128, 128); break;
		case 2: GuyRegion = TextureRegion.split(Guy2WalkTexture, 128, 128); break;
		case 3: GuyRegion = TextureRegion.split(Guy3WalkTexture, 128, 128); break;
		case 4: GuyRegion = TextureRegion.split(Guy4WalkTexture, 128, 128); break;
		case 5: GuyRegion = TextureRegion.split(Guy5WalkTexture, 128, 128); break;
		default: GuyRegion = TextureRegion.split(Guy5WalkTexture, 128, 128);
		}
		
		int i = 0;
		
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 11)
				{
					WalkFrames[i++] = GuyRegion[x][y];
				}
				else
				{
					break;
				}
			}
		}
		WalkAnim = new Animation(0.05f, WalkFrames);
		WalkAnim.setPlayMode(Animation.LOOP);
		
		// Get jump animation frames
		JumpFrames = new TextureRegion[11];
		
		switch (WhichGuy)
		{
		case 1: GuyRegion = TextureRegion.split(Guy1JumpTexture, 128, 128); break;
		case 2: GuyRegion = TextureRegion.split(Guy2JumpTexture, 128, 128); break;
		case 3: GuyRegion = TextureRegion.split(Guy3JumpTexture, 128, 128); break;
		case 4: GuyRegion = TextureRegion.split(Guy4JumpTexture, 128, 128); break;
		case 5: GuyRegion = TextureRegion.split(Guy5JumpTexture, 128, 128); break;
		default: GuyRegion = TextureRegion.split(Guy5JumpTexture, 128, 128);
		}
		
		i = 0;
		
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 11)
				{
					JumpFrames[i++] = GuyRegion[x][y];
				}
				else
				{
					break;
				}
			}
		}
		JumpAnim = new Animation(0.08f, JumpFrames);
		
		// Get punch animation frames
		Punch1Frames = new TextureRegion[6];
		Punch2Frames = new TextureRegion[6];
		
		switch (WhichGuy)
		{
		case 1: GuyRegion = TextureRegion.split(Guy1PunchTexture, 128, 128); break;
		case 2: GuyRegion = TextureRegion.split(Guy2PunchTexture, 128, 128); break;
		case 3: GuyRegion = TextureRegion.split(Guy3PunchTexture, 128, 128); break;
		case 4: GuyRegion = TextureRegion.split(Guy4PunchTexture, 128, 128); break;
		case 5: GuyRegion = TextureRegion.split(Guy5PunchTexture, 128, 128); break;
		default: GuyRegion = TextureRegion.split(Guy5PunchTexture, 128, 128);
		}
		
		i = 0;
		int j = 0;
		
		for (int x=0; x < 4; x++)
		{
			for (int y=0; y < 4; y++)
			{
				if (i < 6)
				{
					Punch1Frames[i++] = GuyRegion[x][y];
				}
				else if (j < 6)
				{
					Punch2Frames[j++] = GuyRegion[x][y];
				}
			}
		}
		Punch1Anim = new Animation(0.05f, Punch1Frames);
		Punch2Anim = new Animation(0.05f, Punch2Frames);				
	}

	@Override
	public int y() {
		return (int)Location.y;
	}

	@Override
	public void Draw(SpriteBatch Batch) {
    	GridPoint2 GuyLoc = new GridPoint2((int)Location.x, (int)(Location.y + Location.z));
    	
    	if (ShowHit)
    	{
    		Batch.setColor(1f, 0f, 0f, 1f);
    		ShowHit = false;
    	}
    	if (Dir == EDir.Left)
    	{
    		Batch.draw(Guy, GuyLoc.x, GuyLoc.y, 64, 0, 128, 128, -1, 1, 0);
    	}
    	else
    	{
    		Batch.draw(Guy, GuyLoc.x, GuyLoc.y, 64, 0, 128, 128, 1, 1, 0);
    	}
    	Batch.setColor(1f, 1f, 1f, 1f);
	}

	@Override
	public Vector3 GetLocation() {
		return Location;
	}

	@Override
	public boolean IsDead() {
		return false;
	}

	@Override
	public Rectangle GetCollision() {
		return Collision;
	}

	@Override
	public void GetPunched() {
		Health -= 1;
		ShowHit = true;
		if (Health == 0)
		{
			Kill();
		}
	}
	
	@Override
	public void Kill() {
		CurrentGuy += 1;
		Screen.Lives -= 1;
		
		if (Screen.Lives > 0)
		{
			ResetDeath();
		}
		else
		{
			Screen.LoseGame();
		}
	}

	@Override
	public boolean IsAttacking() {
		// TODO Auto-generated method stub
		return false;
	}
}
