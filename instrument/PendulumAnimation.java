package instrument;

import graphics.*;

public class PendulumAnimation extends Animation implements Constants
{
	public static final float DECAY_RATE = 0.25f;
	
	protected int affectedAxis;
	protected float angle, velocity, speed;
	protected SingleRotation rotation;
	
	/**
	 * Create a pendulum animation that 
	 * @param part
	 * @param axis
	 */
	public PendulumAnimation(InstrumentPart part, int axis)
	{
		super(part);
		affectedAxis = axis;
		switch(axis)
		{
			case AXIS_X:
				rotation = new SingleRotation(0, 1, 0, 0);
				break;
			case AXIS_Y:
				rotation = new SingleRotation(0, 0, 1, 0);
				break;
			case AXIS_Z:
				rotation = new SingleRotation(0, 0, 0, 1);
				break;
			default:
				rotation = new SingleRotation(0, 1, 0, 0);
		}
		part.rotation = rotation;
		
		angle = rotation.getRotationAmount();
		setSpeed(2);
	}
	
	/**
	 * Apply a new force to this animation
	 * @param force	The amount of force to add
	 */
	public void strike(float force)
	{
		velocity += force;
	}
	
	/**
	 * Set the swing speed of this animation
	 * @param speed	The new swing speed
	 */
	public void setSpeed(float speed)
	{
		if (Math.abs(this.speed) > 0.001f)
		{
			angle *= this.speed;
			velocity *= this.speed;
		}
		
		this.speed = speed;
		angle /= speed;
		velocity /= speed;
	}
	
	public float getSpeed()
	{
		return speed;
	}
	
	public boolean animate(float dTime)
	{
		velocity -= angle*dTime;
		angle += velocity*dTime * speed;
		velocity = velocity * (1 - dTime*DECAY_RATE);
		
		rotation.setRotationAmount(angle);
		
		return Math.abs(velocity) > 0.01f || Math.abs(angle) > 0.01f;
		//return true;
	}
}
