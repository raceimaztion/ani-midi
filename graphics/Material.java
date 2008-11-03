package graphics;

import java.awt.Color;
import java.io.*;
import java.util.Scanner;
import javax.media.opengl.GL;

public class Material
{
	protected Color ambient, diffuse, specular;
	protected float shininess;
	protected String name;
	
	public Material()
	{
		ambient = Color.darkGray;
		specular = Color.lightGray;
		diffuse = Color.orange.darker();
		shininess = 60;
	}
	
	public void apply(GL gl)
	{
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, diffuse.getComponents(null), 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, ambient.getComponents(null), 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, specular.getComponents(null), 0);
		gl.glMaterialf(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, shininess);
	}
	
	public boolean equals(Material m)
	{
		if (this == m) return true;
		return false;
	}
	
	public static Material loadMaterial(String fileName)
	{
		Material result = new Material();
		
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			
			String line = in.readLine();
			while (line != null)
			{
				if (line.length() == 0 || line.startsWith("#"))
				{
					line = in.readLine();
					continue;
				}
				else if (line.startsWith("newmtl "))
				{
					result.name = line.substring("newmtl ".length());
				}
				else if (line.startsWith("Ns "))
				{
					result.shininess = 0.1f * Float.parseFloat(line.substring(3));
				}
				else if (line.startsWith("Ka "))
				{
					Scanner s = new Scanner(line.substring(3));
					result.ambient = new Color(s.nextFloat(), s.nextFloat(), s.nextFloat());
				}
				else if (line.startsWith("Kd "))
				{
					Scanner s = new Scanner(line.substring(3));
					result.diffuse = new Color(s.nextFloat(), s.nextFloat(), s.nextFloat());
				}
				else if (line.startsWith("Ks "))
				{
					Scanner s = new Scanner(line.substring(3));
					result.specular = new Color(s.nextFloat(), s.nextFloat(), s.nextFloat());
				}
				// All other constructs are ignored
				
				line = in.readLine();
			}
			
			return result;
		}
		catch (IOException er)
		{
			System.err.printf("Error reading material from file '%s'\n", fileName);
			return null;
		}
	}

	public Color getAmbient()
	{
		return ambient;
	}

	public void setAmbient(Color ambient)
	{
		this.ambient = ambient;
	}

	public Color getDiffuse()
	{
		return diffuse;
	}

	public void setDiffuse(Color diffuse)
	{
		this.diffuse = diffuse;
	}

	public float getShininess()
	{
		return shininess;
	}

	public void setShininess(float shininess)
	{
		this.shininess = shininess;
	}

	public Color getSpecular()
	{
		return specular;
	}

	public void setSpecular(Color specular)
	{
		this.specular = specular;
	}

	public String getName()
	{
		return name;
	}
}
