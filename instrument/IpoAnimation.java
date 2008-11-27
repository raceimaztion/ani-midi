package instrument;

import java.io.*;

public class IpoAnimation extends Animation
{
	private IpoAnimation(InstrumentPart part)
	{
		super(part);
	}
	
	public boolean animate(float dTime, float curTime)
	{
		return false;
	}
	
	public static IpoAnimation loadAnimation(InstrumentPart part, BufferedReader in) throws IOException
	{
		IpoAnimation result = new IpoAnimation(part);
		
		String line = in.readLine();
		while (!line.contains("end_animation"))
		{
			
			
			line = in.readLine();
		}
		
		return result;
	}
}
