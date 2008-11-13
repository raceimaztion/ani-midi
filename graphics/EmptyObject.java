package graphics;

import java.util.Iterator;
import java.util.Vector;
import javax.media.opengl.GL;

public class EmptyObject implements Iterable<EmptyObject>
{
	protected Position position;
	protected Rotation rotation;
	protected Vector<EmptyObject> children;
	
	public EmptyObject()
	{
		this(new Position());
	}
	
	public EmptyObject(Position pos)
	{
		position = pos;
		rotation = new SingleRotation();
		children = new Vector<EmptyObject>();
	}
	
	/**
	 * EmptyObjects don't have anything to draw, but subclasses can be drawn.
	 * @param gl	The GL context to use for drawing
	 */
	public void draw(GL gl)
	{
		
	}
	
	/**
	 * 
	 * @param dTime	The amount of time (in seconds) that have elapsed since the last update
	 * @return		Whether this object has changed enought to warrant a repaint
	 */
	public boolean animate(float dTime)
	{
		return false;
	}
	
	public Position getPosition()
	{
		return position;
	}
	
	public void setPosition(Position pos)
	{
		position = pos;
	}
	
	public Rotation getRotation()
	{
		return rotation;
	}
	
	public void setRotation(Rotation rotation)
	{
		this.rotation = rotation;
	}
	
	public void addChild(EmptyObject child)
	{
		children.add(child);
	}
	
	public void removeChild(EmptyObject child)
	{
		children.remove(child);
	}
	
	/**
	 * This allows us to iterate over the list, without giving anybody access to the underlying list
	 */
	public Iterator<EmptyObject> iterator()
	{
		return children.iterator();
	}
}
