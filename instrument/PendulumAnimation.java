package instrument;

import graphics.*;

public class PendulumAnimation extends Animation implements Constants
{
	public static final float DECAY_RATE = 0.25f;
	
	protected int affectedAxis;
	protected float angle, velocity;
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
	}
	
	public void strike(float force)
	{
		velocity += force;
	}
	
	public boolean animate(float dTime)
	{
		velocity -= angle*dTime;
		angle += velocity*dTime;
		velocity = velocity * (1 - dTime*DECAY_RATE);
		
		rotation.setRotationAmount(angle);
		
		return Math.abs(velocity) > 0.01f || Math.abs(angle) > 0.01f;
		//return true;
	}
}
