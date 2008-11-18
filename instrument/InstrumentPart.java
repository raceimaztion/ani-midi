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
	protected Position offset;
	protected Rotation rotation;
	
	// Part attributes:
	protected String roll;
	protected int stretchAxis;
	protected Animation animation;
	
	public InstrumentPart(Shape s)
	{
		children = new Vector<InstrumentPart>();
		shape = s;
		
		offset = new Position();
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
		
		/*if (stretchAxis != 0)
		{
			gl.glPushMatrix();
			float scale = 1.0f + (float)note / 64;
			if ((stretchAxis & AXIS_X) == AXIS_X)
				gl.glScalef(scale, 1, 1);
			if ((stretchAxis & AXIS_Y) == AXIS_Y)
				gl.glScalef(1, scale, 1);
			if ((stretchAxis & AXIS_Z) == AXIS_Z)
				gl.glScalef(1, 1, scale);
		}*/
		
		if (offset != null)
			offset.applyTranslation(gl);
		if (rotation != null)
			rotation.apply(gl);
		
		shape.drawNoTransformation(gl);
		
//		if (stretchAxis != 0)
//			gl.glPopMatrix();
		
		for (InstrumentPart part : children)
			part.draw(gl, note);
		
		gl.glPopMatrix();
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		boolean needUpdate = false;
		
		if (animation != null && animation.animate(dTime))
			needUpdate = true;
		
		for (InstrumentPart p : children)
			if (p.animate(dTime, currentTime))
				needUpdate = true;
		
		return needUpdate;
	}
	
	public void addChild(InstrumentPart child)
	{
		children.add(child);
	}

	public Animation getAnimation()
	{
		return animation;
	}

	public void setAnimation(Animation animation)
	{
		this.animation = animation;
	}
}
