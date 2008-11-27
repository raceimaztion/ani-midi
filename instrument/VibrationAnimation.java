package instrument;

import graphics.*;

public class VibrationAnimation extends OscilationAnimation
{
	private OffsetPosition axis;
	
	public VibrationAnimation(Position affectedAxis)
	{
		axis = new OffsetPosition(affectedAxis);
		
		amount = velocity = 0;
	}
	
	public VibrationAnimation(VibrationAnimation a)
	{
		super(a);
		axis = new OffsetPosition(a.axis);
	}
	
	public boolean animate(float dTime, float curTime)
	{
		boolean result = animateStep(dTime);
		
		axis.setAmount(amount);
		
		return result;
	}

	public Position getOffset()
	{
		return axis;
	}

	public Rotation getRotation()
	{
		return null;
	}
	
	public Animation duplicate()
	{
		return new VibrationAnimation(this);
	}
}
