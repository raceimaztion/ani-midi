package graphics;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

public class Camera
{
	protected Position pos;
	protected Position rot;
	
	public Camera()
	{
		this(new Position());
	}
	
	public Camera(Position p)
	{
		pos = p;
		rot = new Position();
	}
	
	public void move(float dx, float dy, float dz)
	{
		pos.move(dx, dy, dz);
	}
	
	public void rotate(float dx, float dy, float dz)
	{
		rot.move(dx, dy, dz);
	}
	
	/**
	 * Apply the camera to the current scene
	 * @param gl
	 * @param glu
	 * @param width
	 * @param height
	 */
	public void apply(GL gl, GLU glu, int width, int height)
	{
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(50, (double)width/height, 0.1f, 50000);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0, 0, 0,  // Eye location
		              0, 0, 1,  // Look at
		              0, 1, 0); // Up
		pos.applyTranslation(gl);
		//gl.glRotatef(rot.y, 1, 0, 0);
		//gl.glRotatef(rot.x, 0, 0, 1);
	}
}
