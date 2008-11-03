package graphics;

import javax.media.opengl.*;

public class Position
{
	public float x, y, z;
	
	/**
	 * Creates a new position set to the origin (0,0,0)
	 */
	public Position()
	{
		x = y = z = 0;
	}
	
	/**
	 * Creates a new position set to the specified coordinates
	 * @param x
	 * @param y
	 * @param z
	 */
	public Position(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Applies this set of coordinates as a scaling operation
	 * @param gl	The GL drawable to use
	 */
	public void applyScale(GL gl)
	{
		gl.glScalef(x, y, z);
	}
	
	/**
	 * Applies this set of coordinates as a translation opeartion
	 * @param gl
	 */
	public void applyTranslation(GL gl)
	{
		gl.glTranslatef(x, y, z);
	}
	
	/**
	 * Applies this set of coordinates as a vertex normal
	 * @param gl
	 */
	public void applyNormal(GL gl)
	{
		gl.glNormal3f(x, y, z);
	}
	
	/**
	 * Applies this set of coordinates as a vertex position
	 * @param gl
	 */
	public void applyVertex(GL gl)
	{
		gl.glVertex3f(x, y, z);
	}
	
	/**
	 * Applies this set of coordinates as the location of a light
	 * @param gl
	 * @param lightNumber
	 * @param distance
	 */
	public void applyLightPos(GL gl, int lightNumber, float distance)
	{
		float[] pos = new float[] {x, y, z, distance};
		gl.glLightfv(lightNumber, GL.GL_POSITION, pos, 0);
	}
	
	/**
	 * Find the length of this vector
	 * @return	The actual length of this vector
	 */
	public float getLength()
	{
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Move this position by a specified amount
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void move(float dx, float dy, float dz)
	{
		x += dx;
		y += dy;
		z += dz;
	}
	
	/**
	 * Move this position by another position's amount
	 * @param p
	 */
	public void move(Position p)
	{
		x += p.x;
		y += p.y;
		z += p.z;
	}
	
	/**
	 * Add the specified coordinates to this and return the results
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return
	 */
	public Position add(float dx, float dy, float dz)
	{
		return new Position(x + dy, y + dy, z + dz);
	}
	
	/**
	 * Subtract the specified coordinates to this and return the results
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return
	 */
	public Position subtract(float dx, float dy, float dz)
	{
		return new Position(x - dx, y - dy, z - dz);
	}
	
	public Position add(Position p)
	{
		return new Position(x + p.x, y + p.y, z + p.z);
	}
	
	public Position subtract(Position p)
	{
		return new Position(x - p.x, y - p.y, z - p.z);
	}
}
