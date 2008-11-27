package instrument;

import graphics.Rotation;
import graphics.Position;

public interface Animation
{
	public abstract boolean animate(float dTime, float curTime);
	
	public abstract Rotation getRotation();
	public abstract Position getOffset();
	
	public abstract void start(float velocity, float startTime);
	public abstract void stop(float velocity, float endTime);
	
	public abstract Animation duplicate();
}
