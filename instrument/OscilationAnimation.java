package instrument;

public abstract class OscilationAnimation extends Animation
{
	public static final float DECAY_RATE = 0.25f;
	
	protected float offset, velocity, speed;
	
	public OscilationAnimation(InstrumentPart part)
	{
		super(part);
		
		offset = velocity = 0;
		speed = 2;
	}
	
	protected boolean animateStep(float dTime)
	{
		velocity -= offset*speed*dTime;
		offset += velocity*dTime;
		velocity *= (1 - DECAY_RATE*dTime);
		
		return Math.abs(velocity) > 0.0001f || Math.abs(offset) > 0.0001f;
	}
	
	public void strike(float force)
	{
		offset += force;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	public float getOffset()
	{
		return offset;
	}

	public float getVelocity()
	{
		return velocity;
	}
}
