package instrument;

import graphics.*;

import java.io.*;

public class IpoAnimation implements Animation
{
	private Rotation rotation;
	private Position offset;
	
	private IpoAnimation()
	{
	}
	
	public boolean animate(float dTime, float curTime)
	{
		return false;
	}
	
	public static IpoAnimation loadAnimation(BufferedReader in) throws IOException
	{
		IpoAnimation result = new IpoAnimation();
		
		String line = in.readLine();
		while (!line.contains("end_animation"))
		{
			
			
			line = in.readLine();
		}
		
		return result;
	}

	public Position getOffset()
	{
		return offset;
	}

	public Rotation getRotation()
	{
		return rotation;
	}
	
	public Animation duplicate()
	{
		// TODO implement this!
		return null;
	}
}
