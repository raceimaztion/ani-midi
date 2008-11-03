package graphics;

import javax.media.opengl.GL;

public class SingleRotation implements Rotation
{
	protected float amount, rx, ry, rz;
	
	public SingleRotation()
	{
		amount = 0;
		rx = 0;
		ry = 0;
		rz = 1;
	}
	
	public SingleRotation(float amount, float x, float y, float z)
	{
		this.amount = amount;
		rx = x;
		ry = y;
		rz = z;
	}
	
	public void apply(GL gl)
	{
		gl.glRotatef(amount, rx, ry, rz);
	}

	public float getRotationAmount()
	{
		return amount;
	}

	public void setRotationAmount(float amount)
	{
		this.amount = amount;
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
}
