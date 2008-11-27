package instrument;

import graphics.*;

public class PendulumAnimation extends OscilationAnimation implements Constants
{
	protected int affectedAxis;
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
		// After this, we can simply alter our local copy and things will animate just fine
		part.rotationOffset = rotation;
		
		setSpeed(2);
	}
	
	public boolean animate(float dTime, float curTime)
	{
		boolean result = animateStep(dTime);
		
		rotation.setRotationAmount(offset);
		
		return result;
		//return true;
	}
}
