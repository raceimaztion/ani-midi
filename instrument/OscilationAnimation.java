package instrument;

public abstract class OscilationAnimation implements Animation
{
	public static final float DECAY_RATE = 0.25f;
	
	protected float amount, velocity, speed;
	
	public OscilationAnimation()
	{
		amount = velocity = 0;
		speed = 1;
	}
	
	public OscilationAnimation(OscilationAnimation a)
	{
		amount = a.amount;
		velocity = a.velocity;
		speed = a.speed;
	}
	
	protected boolean animateStep(float dTime)
	{
		velocity -= amount*speed*dTime;
		amount += velocity*dTime;
		velocity *= (1 - DECAY_RATE*dTime);
		
		return Math.abs(velocity) > 0.0001f || Math.abs(amount) > 0.0001f;
	}
	
	public void strike(float force)
	{
		amount += force;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public void start(float velocity, float startTime)
	{
//		strike(10*velocity);
	}
	
	public void stop(float velocity, float endTime)
	{
		// We don't need to do anything for this one
	}

	public float getAmount()
	{
		return amount;
	}

	public float getVelocity()
	{
		return velocity;
	}
}
