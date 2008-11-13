package graphics;

import javax.media.opengl.GL;

public class RotationXYZ extends Rotation
{
	protected float rx, ry, rz;
	
	public RotationXYZ()
	{
		this(0, 0, 0);
	}
	
	public RotationXYZ(float rx, float ry, float rz)
	{
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public float getRx()
	{
		return rx;
	}

	public void setRx(float rx)
	{
		this.rx = rx;
	}

	public float getRy()
	{
		return ry;
	}

	public void setRy(float ry)
	{
		this.ry = ry;
	}

	public float getRz()
	{
		return rz;
	}

	public void setRz(float rz)
	{
		this.rz = rz;
	}

	public void apply(GL gl)
	{
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
	}
}
