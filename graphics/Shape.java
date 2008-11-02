package graphics;

import java.util.*;
import javax.media.opengl.*;

public class Shape
{
	protected Position position;
	protected Vector<Position> points;
	
	public Shape()
	{
		position = new Position();
	}
	
	public void draw(GL gl)
	{
		gl.glPushMatrix();
		position.applyTranslation(gl);
		
		gl.glPopMatrix();
	}
}
