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
	
	public void rotate(float dx, float dy)
	{
		rot.move(dx, dy, 0);
	}
	
	public void rotate(Position p)
	{
		rot.move(p);
	}
	
	public void lookAt(Position p)
	{
		
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
		glu.gluPerspective(70, (double)width/height, 0.001f, 50000);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0, 0, 0,  // Eye location
		              0, 1, 0,  // Look at
		              0, 0, 1); // Up
		gl.glRotatef(rot.y, 0, 0, 1);
		gl.glRotatef(rot.x, 1, 0, 0);
		pos.applyTranslation(gl);
	}
}
