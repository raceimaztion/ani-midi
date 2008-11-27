package instrument;

import graphics.*;

public class PendulumAnimation extends OscilationAnimation implements Constants
{
	public static float max_value = 0;
	
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
		
		setSpeed(1);
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
		if (Math.abs(dTime) > max_value)
		{
			max_value = Math.abs(dTime);
			System.out.printf("New max for rotation value: %.2f\n", amount);
		}
		
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
