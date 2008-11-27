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
	
	public AnimationStep(AnimationStep a)
	{
		timeValue = a.timeValue;
		stepType = a.stepType;
		pos = a.pos;
		rot = a.rot;
	}
	
	public boolean sameType(AnimationStep as)
	{
		if ((pos == null) != (as.pos == null))
			return false;
		if ((rot == null) != (as.rot == null))
			return false;
		
		return true;
	}
	
	public static AnimationStep parseAnimationStep(String line)
	{
		if (!line.contains("step "))
			return null;
		
		line = line.substring(line.indexOf("step ") + "step ".length());
		
		float time = Float.parseFloat(line.substring(0, line.indexOf(' ')));
		
		line = line.substring(line.indexOf(' ') + 1);
		
		if (line.startsWith("rot "))
		{
			return new AnimationStep(time, Rotation.parseRotation(line));
		}
		else if (line.startsWith("move "))
		{
			Scanner scan = new Scanner(line.substring("move ".length()).trim());
			return new AnimationStep(time, new Position(scan.nextFloat(), scan.nextFloat(), scan.nextFloat()));
		}
		System.err.printf("instrument.AnimationStep.parseAnimationStep(String): Unknown step type:\n%s\n", line);
		
		return null;
	}
}
