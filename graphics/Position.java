package graphics;

import javax.media.opengl.*;

public class Position
{
	public float x, y, z;
	
	public Position()
	{
		x = y = z = 0;
	}
	
	public Position(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void applyScale(GL gl)
	{
		gl.glScalef(x, y, z);
	}
	
	public void applyTranslation(GL gl)
	{
		gl.glTranslatef(x, y, z);
	}
	
	public float getLength()
	{
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public void move(float dx, float dy, float dz)
	{
		x += dx;
		y += dy;
		z += dz;
	}
	
	public void move(Position p)
	{
		x += p.x;
		y += p.y;
		z += p.z;
	}
	
	public Position add(float dx, float dy, float dz)
	{
		return new Position(x + dy, y + dy, z + dz);
	}
	
	public Position add(Position p)
	{
		return new Position(x + p.x, y + p.y, z + p.z);
	}
}
