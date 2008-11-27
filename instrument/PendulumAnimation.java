package instrument;

import graphics.*;

public class PendulumAnimation extends OscilationAnimation implements Constants
{
	protected SingleRotation rotation;
	
	/**
	 * Create a pendulum animation that 
	 * @param part
	 * @param axis
	 */
	public PendulumAnimation(int axis)
	{
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
		
		setSpeed(2);
	}
	
	public PendulumAnimation(PendulumAnimation a)
	{
		super(a);
		rotation = new SingleRotation(a.rotation);
	}
	
	public boolean animate(float dTime, float curTime)
	{
		boolean result = animateStep(dTime);
		
		rotation.setRotationAmount(amount);
		
		return result;
	}

	public Rotation getRotation()
	{
		return rotation;
	}
	
	public Position getOffset()
	{
		return null;
	}
	
	public Animation duplicate()
	{
		return new PendulumAnimation(this);
	}
}
