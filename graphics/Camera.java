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
	
	public void move(Position p)
	{
		pos.move(p);
	}
	
	public void rotate(float dx, float dy, float dz)
	{
		rot.move(dx, dy, dz);
	}
	
	public void rotate(Position p)
	{
		rot.move(p);
	}
	
	/**
	 * Apply the camera to the current scene
	 * @param gl		The GL context to use for rendering
	 * @param glu		The GLU context to use for rendering
	 * @param width		The width of the display in pixels
	 * @param height	The height of the display in pixels
	 */
	public void apply(GL gl, GLU glu, int width, int height)
	{
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(90, (double)width/height, 0.1f, 50000);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0, 0, 0,  // Eye location
		              0, 1, 0,  // Look at
		              0, 0, 1); // Up
		pos.applyTranslation(gl);
		//gl.glRotatef(rot.y, 1, 0, 0);
		//gl.glRotatef(rot.x, 0, 0, 1);
	}
}
