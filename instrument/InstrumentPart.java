package instrument;

import java.util.Vector;
import javax.media.opengl.GL;
import graphics.*;

public class InstrumentPart implements Constants
{
	/**
	 * Does nothing
	 */
	public static final String ROLL_HOLDER = "holder";
	/**
	 * Starts any strings or swings oscilating
	 */
	public static final String ROLL_HAMMER = "hammer";
	/**
	 * Swings when the hammer strikes
	 */
	public static final String ROLL_SWINGS = "swings";
	/**
	 * Vibrates when the hammer strikes
	 */
	public static final String ROLL_STRING = "string";
	
	protected Vector<InstrumentPart> children;
	// Part appearance:
	protected Shape shape;
	protected Position position;
	protected Rotation rotation;
	
	// Part attributes:
	protected String roll;
	protected int stretchAxis;
	
	// Animation attributes
	protected Animation[] animations;
	protected Position[] offsets;
	protected Rotation[] rotationOffsets;
	
	// Temporary things
//	private int currentNote;
	
	public InstrumentPart(Shape s)
	{
		children = new Vector<InstrumentPart>();
		shape = s;
		
		animations = new Animation[128];
		offsets = new Position[128];
		rotationOffsets = new Rotation[128];
		
		position = new Position();
	}
	
	public void assignTextures(MaterialLibrary library)
	{
		shape.setMaterial(library.getMaterial(shape.getMaterialName()));
		for (InstrumentPart p : children)
			p.assignTextures(library);
	}
	
	public Vector<InstrumentPart> getChildren()
	{
		return children;
	}
	
	/**
	 * Draw this part of the virtual instrument
	 * @param gl	The graphics context to use
	 * @param note	The note that this instrument part is supposed to be right now
	 */
	public void draw(GL gl, int note)
	{
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		
		if (rotation != null)
			rotation.apply(gl);
		if (animations[note] != null && animations[note].getRotation() != null)
			animations[note].getRotation().apply(gl);
		
		if (stretchAxis != 0)
		{
			gl.glPushMatrix();
			float scale = 1.0f + (float)note / 64;
			if ((stretchAxis & AXIS_X) == AXIS_X)
				gl.glScalef(scale, 1, 1);
			if ((stretchAxis & AXIS_Y) == AXIS_Y)
				gl.glScalef(1, scale, 1);
			if ((stretchAxis & AXIS_Z) == AXIS_Z)
				gl.glScalef(1, 1, scale);
		}
		
		if (position != null)
			position.applyTranslation(gl);
		if (animations[note] != null && animations[note].getOffset() != null)
			animations[note].getOffset().applyTranslation(gl);
		
		shape.drawNoTransformation(gl);
		
		if (stretchAxis != 0)
			gl.glPopMatrix();
		
		if (position != null)
			position.applyTranslation(gl);
		if (offsets[note] != null)
			offsets[note].applyTranslation(gl);
		
		for (InstrumentPart part : children)
			part.draw(gl, note);
		
		gl.glPopMatrix();
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		boolean needUpdate = false;
		
		for (int note=0; note < 128; note ++)
		{
			if (animations[note] != null && animations[note].animate(dTime, currentTime))
				needUpdate = true;
		}
		
		for (InstrumentPart p : children)
			if (p.animate(dTime, currentTime))
				needUpdate = true;
		
		return needUpdate;
	}
	
	public void addChild(InstrumentPart child)
	{
		children.add(child);
	}

	public void setAnimation(Animation animation)
	{
		for (int i=0; i < animations.length; i++)
		{
			animations[i] = animation.duplicate();
			offsets[i] = animations[i].getOffset();
			rotationOffsets[i] = animations[i].getRotation();
		}
	}
	
	public void setAnimation(Animation animation, int note)
	{
		animations[note] = animation;
		offsets[note] = animation.getOffset();
		rotationOffsets[note] = animation.getRotation();
	}

	public Position getPosition()
	{
		return position;
	}

	public void setPosition(Position position)
	{
		this.position = position;
	}

	public Rotation getRotation()
	{
		return rotation;
	}

	public void setRotation(Rotation rotation)
	{
		this.rotation = rotation;
	}

	public String getRoll()
	{
		return roll;
	}

	public Shape getShape()
	{
		return shape;
	}
}
