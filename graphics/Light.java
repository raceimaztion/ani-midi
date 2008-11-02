package graphics;

import java.awt.Color;
import javax.media.opengl.*;

public class Light
{
	protected Position pos;
	protected Color ambient, diffuse, specular;
	
	public Light()
	{
		
	}
	
	public Color getAmbient()
	{
		return ambient;
	}
	
	public void setAmbient(Color a)
	{
		ambient = a;
	}
	
	public Color getDiffuse()
	{
		return diffuse;
	}
	
	public void setDiffuse(Color d)
	{
		diffuse = d;
	}
	
	public Color getSpecular()
	{
		return specular;
	}
	
	public void setSpecular(Color s)
	{
		specular = s;
	}
	
	public void apply(GL gl, int lightNumber)
	{
		pos.applyLightPos(gl, lightNumber, 1);
		gl.glLightfv(lightNumber, GL.GL_DIFFUSE, diffuse.getComponents(null), 0);
		gl.glLightfv(lightNumber, GL.GL_AMBIENT, ambient.getComponents(null), 0);
		gl.glLightfv(lightNumber, GL.GL_SPECULAR, specular.getComponents(null), 0);
	}
}
