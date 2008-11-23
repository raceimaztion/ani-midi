package instrument;

import java.io.*;
import java.util.Stack;
import java.util.Vector;
import java.util.Scanner;

import javax.media.opengl.GL;

import graphics.*;

public class VirtualInstrument
{
	public static final String REGEX_FLOAT = "-?[0-9]+(\\.[0-9]+)?";
	
	protected String name;
	protected Vector<Integer> supportedPatches;
	protected Vector<InstrumentPart> parts;
	protected Shape thisPart;
	protected InstrumentPart parent;
	
	protected VirtualInstrument()
	{
		this(null);
	}
	
	protected VirtualInstrument(InstrumentPart parent)
	{
		this.parent = parent;
		setup();
	}
	
	protected void setup()
	{
		supportedPatches = new Vector<Integer>();
		parts = new Vector<InstrumentPart>();
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
		
		return needUpdate;
	}
	
	public void draw(GL gl)
	{
		for (InstrumentPart p : parts)
			p.draw(gl, 127);
	}
	
	public static VirtualInstrument loadVirtualInstrument(String file)
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(file));
			VirtualInstrument result = new VirtualInstrument();
			
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
					for (String s : line.substring("patches ".length()).split(" "))
					{
						result.supportedPatches.add(Integer.parseInt(s));
					}
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
					stack.peek().roll = line.substring("roll ".length());
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
					stack.peek().offset = new Position(scan.nextFloat(), scan.nextFloat(), scan.nextFloat());
				}
				// Does this part have an offset from its parent already?
				else if (line.startsWith("offset_fix "))
				{
					Scanner scan = new Scanner(line.substring("offset_fix ".length()));
					stack.peek().offset = new Position(scan.nextFloat(), scan.nextFloat(), scan.nextFloat());
					Position offset = stack.peek().offset;
					for (Position p : stack.peek().shape.getVertices())
					{
						p.move(offset, -1);
					}
				}
				// Is this part rotated in relation to its parent?
				else if (line.startsWith("rotation "))
				{
					String amount = line.substring("rotation ".length());
					Rotation rot = Rotation.parseresultation(amount);
					
					if (rot != null)
						stack.peek().rotation = rot;
				}
				// TODO: add support for animations
				
				line = in.readLine();
			} // end while we have more lines
			
			return result;
		}
		catch (IOException er)
		{
			return null;
		}
	}
}
