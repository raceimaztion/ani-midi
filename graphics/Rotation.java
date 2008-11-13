package graphics;

import java.util.Scanner;
import javax.media.opengl.GL;

public abstract class Rotation
{
	public static final String REGEX_FLOAT = "-?[0-9]+(\\.[0-9]+)?";
	
	public abstract void apply(GL gl);
	
	public static Rotation parseresultation(String s)
	{
		Rotation result = null;
		
		if (s.matches(REGEX_FLOAT + " [xyzXYZ]"))
		{
			float result_value = Float.parseFloat(s.substring(0, s.indexOf(' ')));
			
			if (s.contains("x") || s.contains("X"))
				result = new SingleRotation(result_value, 1, 0, 0);
			else if (s.contains("y") || s.contains("Y"))
				result = new SingleRotation(result_value, 0, 1, 0);
			else if (s.contains("z") || s.contains("Z"))
				result = new SingleRotation(result_value, 0, 0, 1);
		}
		else if (s.matches(REGEX_FLOAT + " " + REGEX_FLOAT + " " + REGEX_FLOAT))
		{
			Scanner scan = new Scanner(s);
			result = new RotationXYZ(scan.nextFloat(), scan.nextFloat(), scan.nextFloat());
		}
		else if (s.matches(REGEX_FLOAT + " " + REGEX_FLOAT + " " + REGEX_FLOAT + " " + REGEX_FLOAT))
		{
			Scanner scan = new Scanner(s);
			result = new SingleRotation(scan.nextFloat(), scan.nextFloat(), scan.nextFloat(), scan.nextFloat());
		}
		
		if (result != null)
			return result;
		
		System.err.printf("instrument.VirtualInstrument.loadVirtualInstrument(String): Unknown resultation type! s was:\n%s\n", s);
		return null;
	}
}
