package instrument;

import graphics.*;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class IpoAnimation implements Animation, Constants
{
	private SingleRotation rotation;
	private Position offset;
	private float duration, strikeTime, startTime;
	
	private Vector<AnimationStep> steps;
	private Vector<OscilationAnimation> affectedAnimations;
	
	private int curStep = -1;
	
	private IpoAnimation(int axis)
	{
		steps = new Vector<AnimationStep>();
		affectedAnimations = new Vector<OscilationAnimation>();
		rotation = new SingleRotation();
	}
	
	private IpoAnimation(IpoAnimation a)
	{
		rotation = a.rotation;
		offset = a.offset;
		duration = a.duration;
		strikeTime = a.strikeTime;
		startTime = a.startTime;
		
		affectedAnimations = new Vector<OscilationAnimation>();
		steps = new Vector<AnimationStep>();
		for (AnimationStep as : a.steps)
			steps.add(as);
	}
	
	public boolean animate(float dTime, float curTime)
	{
		if (curStep < 0)
			return false;
		else if (curStep+1 >= steps.size())
		{
			rotation.setRotationAmount(steps.get(curStep).rot.getRotationAmount());
			curStep = -1;
			return false;
		}
		
		float delta = curTime - startTime - steps.get(curStep).timeValue;
		float rotAmount = steps.get(curStep).rot.getRotationAmount();
		rotAmount = (steps.get(curStep+1).rot.getRotationAmount() - rotAmount)*delta*(steps.get(curStep+1).timeValue - steps.get(curStep).timeValue);
		rotation.setRotationAmount(steps.get(curStep).rot.getRotationAmount());
		
		return false;
	}
	
	public void addAffectedAnimation(OscilationAnimation a)
	{
		affectedAnimations.add(a);
	}

	public Position getOffset()
	{
		return offset;
	}

	public Rotation getRotation()
	{
		return rotation;
	}
	
	public void start(float velocity, float startTime)
	{
		this.startTime = startTime;
		curStep = 0;
	}
	
	public void stop(float velocity, float endTime)
	{
		
	}
	
	public Animation duplicate()
	{
		return new IpoAnimation(this);
	}
	
	public static IpoAnimation loadAnimation(BufferedReader in, String firstLine) throws IOException
	{
		IpoAnimation result = new IpoAnimation(AXIS_Y);
		
		Scanner scan = new Scanner(firstLine.substring(firstLine.indexOf(" ")+1));
		result.duration = scan.nextFloat();
		result.strikeTime = scan.nextFloat();
		
		String line = in.readLine().trim();
		while (!line.contains("end_animation"))
		{
			if (line.startsWith("step "))
			{
				AnimationStep step = AnimationStep.parseAnimationStep(line);
				if (step != null)
					result.steps.add(step);
				else
					System.err.printf("Error reading animtion 'step' line! Line was:\n%s\n", line);
			}
			
			line = in.readLine().trim();
		}
		
		return result;
	}
}
