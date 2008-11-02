package graphics;

import java.io.*;
import java.util.*;
import javax.media.opengl.*;

public class Shape
{
	protected Position position;
	protected Vector<Position> vertices;
	protected Vector<Position> normals;
	protected Vector<Integer[]> polygons;
	protected Vector<Integer[]> polyNormals;
	protected Vector<Boolean> polySmooth;
	protected String name;
	protected String materialName;

	protected Shape()
	{
		position = new Position();
		vertices = new Vector<Position>();
		normals = new Vector<Position>();
		polygons = new Vector<Integer[]>();
		polyNormals = new Vector<Integer[]>();
		polySmooth = new Vector<Boolean>();
	}

	public void draw(GL gl)
	{
		gl.glPushMatrix();
		position.applyTranslation(gl);
		
		boolean smooth;
		
		for (int nPoly=0; nPoly < polygons.size(); nPoly++)
		{
			smooth = polySmooth.get(nPoly);
			if (!smooth)
				normals.get(polyNormals.get(nPoly)[0]).applyNormal(gl);
			
			gl.glBegin(GL.GL_POLYGON);
			Integer[] verts = polygons.get(nPoly);
			Integer[] norms = polyNormals.get(nPoly);
			for (int i=0; i < verts.length; i++)
			{
				if (smooth)
					normals.get(norms[i]).applyNormal(gl);
				vertices.get(verts[i]).applyVertex(gl);
			}
			gl.glEnd();
		}

		gl.glPopMatrix();
	}
	
	public String getName()
	{
		return name;
	}

	/* ************************************* *
	 * Functions to load a shape from a file *
	 * ************************************* */

	/**
	 * Returns a Shape as loaded from a WaveFront Object file
	 * @param fileName	The name of the file to load
	 * @return	The Shape as loaded from the file
	 */
	public static Shape loadWavefrontObject(String fileName)
	{
		Shape result = new Shape();

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(new File(
					fileName)));

			String line = in.readLine();
			boolean smooth = false;
			while (line != null)
			{
				if (line.length() == 0 || line.charAt(0) == '#')
				{
					line = in.readLine();
					continue;
				}
				if (line.startsWith("o "))
				{
					result.name = line.substring(2);
				}
				else if (line.startsWith("v "))
				{
					Scanner s = new Scanner(line.substring(2));
					result.vertices.add(new Position(s.nextFloat(), s.nextFloat(), s.nextFloat()));
				}
				else if (line.startsWith("vn "))
				{
					Scanner s = new Scanner(line.substring(2));
					result.normals.add(new Position(s.nextFloat(), s.nextFloat(), s.nextFloat()));
				}
				else if (line.startsWith("usemtl "))
				{
					result.materialName = line.substring("usemtl ".length());
				}
				else if (line.startsWith("s 1"))
				{
					smooth = true;
				}
				else if (line.startsWith("f "))
				{
					String[] split = line.substring(2).split(" ");
					Integer[] indicies = new Integer[split.length];
					Integer[] normals = new Integer[split.length];
					
					for (int i=0; i < split.length; i++)
					{
						normals[i] = -1;
						
						if (split[i].contains("//"))
						{
							indicies[i] = Integer.parseInt(split[i].substring(0, split[i].indexOf('/')));
							normals[i] = Integer.parseInt(split[i].substring(1+split[i].lastIndexOf('/')));
							indicies[i] -= 1;
							normals[i] -= 1;
						}
						else
						{
							indicies[i] = Integer.parseInt(split[i]);
						}
					}
					
					result.polygons.add(indicies);
					result.polyNormals.add(normals);
					result.polySmooth.add(smooth);
					smooth = false;
				}
				
				line = in.readLine();
			}
		}
		catch (IOException er)
		{
			System.err.printf("Error reading from file '%s'!\n", fileName);
			return null;
		}

		return result;
	}
}
