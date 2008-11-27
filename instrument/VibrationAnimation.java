package instrument;

import graphics.*;

public class VibrationAnimation extends OscilationAnimation
{
	private OffsetPosition axis;
	
	public VibrationAnimation(InstrumentPart part, Position affectedAxis)
	{
		super(part);
		part.offset = axis = new OffsetPosition(affectedAxis);
		
		offset = velocity = 0;
	}
	
	public boolean animate(float dTime, float curTime)
	{
		boolean result = animateStep(dTime);
		
		axis.setAmount(offset);
		
		return result;
	}
}
