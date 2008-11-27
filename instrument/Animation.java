package instrument;

import graphics.Rotation;
import graphics.Position;

public interface Animation
{
	public abstract boolean animate(float dTime, float curTime);
	
	public abstract Rotation getRotation();
	public abstract Position getOffset();
	
	public abstract Animation duplicate();
}
