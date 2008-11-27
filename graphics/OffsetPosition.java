package graphics;

import javax.media.opengl.GL;

public class OffsetPosition extends Position
{
	private float amount;
	
	public OffsetPosition()
	{
		super();
		amount = 0.0f;
	}
	
	public OffsetPosition(float x, float y, float z)
	{
		super(x, y, z);
		amount = 0.0f;
	}
	
	public OffsetPosition(float x, float y, float z, float amount)
	{
		super(x, y, z);
		this.amount = amount;
	}
	
	public OffsetPosition(OffsetPosition p)
	{
		super(p);
		amount = p.amount;
	}
	
	public OffsetPosition(Position p)
	{
		super(p);
		amount = 0.0f;
	}
	
	public float getAmount()
	{
		return amount;
	}
	
	public void setAmount(float amount)
	{
		this.amount = amount;
	}
	
	/**
	 * Apply this offset as a translation
	 */
	public void applyTranslation(GL gl)
	{
		applyTranslation(gl, amount);
	}
}
