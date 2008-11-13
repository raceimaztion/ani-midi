package instrument;

import java.util.Scanner;
import graphics.*;

public class AnimationStep
{
	public static final int STEP_ROTATION = 0x501;
	public static final int STEP_MOTION = 0x502;
	
	/**
	 * The time at which this step takes place
	 */
	protected float timeValue;
	protected int stepType;
	protected Position pos;
	protected Rotation rot;
	
	public AnimationStep(float time, Position p)
	{
		timeValue = time;
		stepType = STEP_MOTION;
		pos = p;
		rot = null;
	}
	
	public AnimationStep(float time, Rotation r)
	{
		timeValue = time;
		stepType = STEP_ROTATION;
		pos = null;
		rot = r;
	}
	
	public static AnimationStep parseAnimationStep(String line)
	{
		line = line.substring("step ".length());
		
		float time = Float.parseFloat(line.substring(0, line.indexOf(' ')));
		
		line = line.substring(line.indexOf(' ') + 1);
		
		if (line.startsWith("rot "))
		{
			return new AnimationStep(time, Rotation.parseresultation(line));
		}
		else if (line.startsWith("move "))
		{
			Scanner scan = new Scanner(line.substring("move ".length()));
			return new AnimationStep(time, new Position(scan.nextFloat(), scan.nextFloat(), scan.nextFloat()));
		}
		System.err.printf("instrument.AnimationStep.parseAnimationStep(String): Unknown step type:\n%s\n", line);
		
		return null;
	}
}
