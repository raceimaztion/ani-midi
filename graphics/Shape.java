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
	
	protected Shape()
	{
		position = new Position();
    vertices = new Vector<Position>();
    normals = new Vector<Position>();
    polygons = new Vector<Integer[]>();
	}
	
	public void draw(GL gl)
	{
		gl.glPushMatrix();
		position.applyTranslation(gl);
		
		gl.glPopMatrix();
	}
  
  /* ************************************* *
   * Functions to load a shape from a file *
   * ************************************* */
  
  /**
   * Returns a Shape as loaded from a WaveFront Object file
   */
  public static Shape loadWavefrontObject(String fileName)
  {
    Shape result = new Shape();
    
    try
    {
      BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
      
      String line = in.readLine();
      while (line != null)
      {
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
