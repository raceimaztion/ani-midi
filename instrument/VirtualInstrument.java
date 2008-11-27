package instrument;

import java.io.*;
import java.util.Stack;
import java.util.Vector;
import java.util.Scanner;

import javax.media.opengl.GL;

import graphics.*;

public class VirtualInstrument implements Constants
{
	protected String name;
	protected Vector<Integer> supportedPatches;
	protected Vector<InstrumentPart> parts;
	protected Shape thisPart;
	protected float width;
	
	protected Vector<MidiNoteEvent> allNotes;
	protected int lastNote = 0;
	
	protected VirtualInstrument()
	{
		setup();
	}
	
	protected void setup()
	{
		supportedPatches = new Vector<Integer>();
		parts = new Vector<InstrumentPart>();
		allNotes = new Vector<MidiNoteEvent>();
		width = 1.0f;
	}
	
	public void addNote(MidiNoteEvent event)
	{
		allNotes.add(event);
	}
	
	public void assignTextures(MaterialLibrary library)
	{
		for (InstrumentPart p : parts)
			p.assignTextures(library);
	}
	
	public Vector<InstrumentPart> getParts()
	{
		return parts;
	}
	
	public boolean animate(float dTime, float totalTime)
	{
		boolean needUpdate = false;
		
		for (InstrumentPart p : parts)
			if (p.animate(dTime, totalTime))
				needUpdate = true;
		
		if (allNotes.size() < 1)
			return false;
		
		if (lastNote >= allNotes.size())
			return false;
		
		if (allNotes.get(lastNote).getTime() < totalTime)
		{
			int note = allNotes.get(lastNote).getNote();
			
			for (InstrumentPart p : parts)
				triggerAnimations(p, note, allNotes.get(lastNote).getVelocity(), totalTime);
			lastNote++;
		}
		
		return needUpdate;
	}
	
	private void triggerAnimations(InstrumentPart p, int note, float velocity, float curTime)
	{
		if (p.getRoll().contains("hammer"))
		{
			p.getAnimationForNote(note).start(velocity, curTime);
		}
		
		for (InstrumentPart part : p.getChildren())
			triggerAnimations(part, note, velocity, curTime);
	}
	
	public void draw(GL gl)
	{
		for (int note=0; note < 128; note ++)
		{
			gl.glPushMatrix();
			
			// Transform the current object into place
			gl.glTranslatef(0, (note-63)*width, 0);
			
			for (InstrumentPart p : parts)
				p.draw(gl, note);
			
			gl.glPopMatrix();
		}
	}
	
	public boolean isPatchSupported(int patch)
	{
		if (supportedPatches == null)
			return true;
		else
			return supportedPatches.contains((Integer)patch);
	}
	
	public Vector<InstrumentPart> getAllByRoll(String roll)
	{
		Vector<InstrumentPart> result = new Vector<InstrumentPart>();
		
		for (InstrumentPart part : parts)
		{
			if (part.getRoll().startsWith(roll))
				result.add(part);
			
			findAllByRoll(result, part, roll);
		}
		
		return result;
	}
	
	private void findAllByRoll(Vector<InstrumentPart> list, InstrumentPart part, String roll)
	{
		for (InstrumentPart p : part.getChildren())
		{
			if (p.getRoll().startsWith(roll))
				list.add(p);
			
			findAllByRoll(list, p, roll);
		}
	}
	
	public static VirtualInstrument loadVirtualInstrument(String file)
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			VirtualInstrument result = new VirtualInstrument();
			
			Vector<InstrumentPart> swings = new Vector<InstrumentPart>(),
									animated = new Vector<InstrumentPart>();
			
			Stack<InstrumentPart> stack = new Stack<InstrumentPart>();
			String line = in.readLine();
			while (line != null)
			{
				// Comments and blank lines are ignored
				if (line.startsWith("#") || line.length() < 1)
				{
					line = in.readLine();
					continue;
				}
				
				// Remove any extra whitespace
				line = line.trim();
				
				// The string name of this instrument
				if (line.startsWith("instrument "))
				{
					result.name = line.substring("instrument ".length());
				}
				// The instruments that we can use this instrument for
				else if (line.startsWith("patches "))
				{
					if (line.endsWith("all"))
						result.supportedPatches = null;
					else
						for (String s : line.substring("patches ".length()).split(" "))
							result.supportedPatches.add(Integer.parseInt(s));
				}
				else if (line.startsWith("width "))
				{
					result.width = Float.parseFloat(line.substring("width ".length()));
				}
				// This starts a new object and a new level in the heirarchy
				else if (line.startsWith("shape "))
				{
					if (line.equals("shape empty"))
					{
						InstrumentPart p = new InstrumentPart(null);
						if (stack.size() > 0)
							stack.peek().addChild(p);
						else
							result.parts.add(p);
						stack.push(p);
					}
					else
					{
						InstrumentPart p = new InstrumentPart(Shape.loadWavefrontObject("models/instruments/" + line.substring("shape ".length())));
						if (stack.size() > 0)
							stack.peek().addChild(p);
						else
							result.parts.add(p);
						stack.push(p);
					}
				}
				// This goes up a level in the heirarcy
				else if (line.startsWith("pop"))
				{
					stack.pop();
				}
				// Set the roll that the current part has
				else if (line.startsWith("roll "))
				{
					String roll = line.substring("roll ".length());
					stack.peek().roll = roll;
					
					if (roll.contains("swings"))
					{
						if (roll.contains("x") || roll.contains("X"))
							stack.peek().setAnimation(new PendulumAnimation(AXIS_X));
						else if (roll.contains("y") || roll.contains("Y"))
							stack.peek().setAnimation(new PendulumAnimation(AXIS_Y));
						else if (roll.contains("z") || roll.contains("Z"))
							stack.peek().setAnimation(new PendulumAnimation(AXIS_Z));
					}
					else if (roll.contains("vibrates"))
					{
						stack.peek().setAnimation(new VibrationAnimation(Position.parsePosition(roll.substring(roll.indexOf(" ")))));
					}
					if (roll.contains("swings") || roll.contains("vibrates"))
						swings.add(stack.peek());
				}
				// If this part stretches at all, which axis?
				else if (line.startsWith("stretch "))
				{
					if (line.contains("X") || line.contains("x"))
						stack.peek().stretchAxis |= InstrumentPart.AXIS_X;
					if (line.contains("Y") || line.contains("y"))
						stack.peek().stretchAxis |= InstrumentPart.AXIS_Y;
					if (line.contains("Z") || line.contains("z"))
						stack.peek().stretchAxis |= InstrumentPart.AXIS_Z;
				}
				// Does this part have an offset from its parent?
				else if (line.startsWith("offset "))
				{
					Scanner scan = new Scanner(line.substring("offset ".length()));
					stack.peek().position = new Position(scan.nextFloat(), scan.nextFloat(), scan.nextFloat());
				}
				// Does this part have an offset from its parent already?
				else if (line.startsWith("offset_fix "))
				{
					Scanner scan = new Scanner(line.substring("offset_fix ".length()));
					stack.peek().position = new Position(scan.nextFloat(), scan.nextFloat(), scan.nextFloat());
					Position offset = stack.peek().position;
					for (Position p : stack.peek().shape.getVertices())
					{
						p.move(offset, -1);
					}
				}
				// Is this part rotated in relation to its parent?
				else if (line.startsWith("rotation "))
				{
					String amount = line.substring("rotation ".length());
					Rotation rot = Rotation.parseRotation(amount);
					
					if (rot != null)
						stack.peek().rotation = rot;
				}
				// Is this the start of an animation?
				else if (line.startsWith("animation "))
				{
					IpoAnimation animation = IpoAnimation.loadAnimation(in, line);
					stack.peek().setAnimation(animation);
					
					animated.add(stack.peek());
				}
				
				line = in.readLine();
			} // end while we have more lines
			
			// Tie up some loose ends
			for (InstrumentPart a : animated)
				for (InstrumentPart s : swings)
					for (int note=0; note < 128; note++)
					{
						((IpoAnimation)a.animations[note]).addAffectedAnimation((OscilationAnimation)s.animations[note]);
					}
			
			return result;
		}
		catch (IOException er)
		{
			return null;
		}
	} // end loadVirtualInstrument()
}
