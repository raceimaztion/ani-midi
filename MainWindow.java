import java.awt.*;
import javax.swing.*;
// Jogl imports:
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
// Project imports
import graphics.*;

public class MainWindow implements GLEventListener
{
	protected JFrame mainWindow;
	protected GLCanvas drawingCanvas;
	protected GLU glu;
	protected Camera camera;
	protected boolean hasDrawn = false;
	
	public MainWindow()
	{
		mainWindow = new JFrame("MIDI Visualizer - by Daryl Van Humbeck");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		drawingCanvas = new GLCanvas();
		drawingCanvas.addGLEventListener(this);
		Dimension d = new Dimension(640, 480);
		drawingCanvas.setPreferredSize(d);
		drawingCanvas.setMinimumSize(d);
		
		glu = new GLU();
		camera = new Camera();
		camera.move(0, -3, 5);
		
		mainWindow.getContentPane().add(drawingCanvas, BorderLayout.CENTER);
		mainWindow.pack();
		mainWindow.setLocationRelativeTo(null);
	}
	
	public void show()
	{
		mainWindow.setVisible(true);
	}
	
	public void hide()
	{
		mainWindow.setVisible(false);
	}
	
	public void display(GLAutoDrawable drawable)
	{
		// TODO Auto-generated method stub
		GL gl = drawable.getGL();
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);
		
		// gl.glColor3f(0.6f, 0.4f, 0.5f);
		gl.glColor3f(1, 1, 1);
		gl.glDisable(GL.GL_DEPTH_TEST);
		
		gl.glBegin(GL.GL_QUADS);
		// One way round:
		gl.glVertex3f(1, 0, 1);
		gl.glVertex3f(1, 0, -1);
		gl.glVertex3f(-1, 0, -1);
		gl.glVertex3f(-1, 0, 1);
		// Other way round:
		/*gl.glVertex3f(-1, 0, -1);
		gl.glVertex3f(1, 0, -1);
		gl.glVertex3f(1, 0, 1);
		gl.glVertex3f(-1, 0, 1);*/
		gl.glEnd();
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		
		gl.glFlush();
		if (!drawable.getAutoSwapBufferMode())
			drawable.swapBuffers();
		
		if (!hasDrawn)
		{
			System.out.println("Drawn once!");
			hasDrawn = true;
		}
	}
	
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
	{
		// TODO Auto-generated method stub
	}
	
	public void init(GLAutoDrawable drawable)
	{
		// TODO Auto-generated method stub
		drawable.setGL(new DebugGL(drawable.getGL()));
		
		GL gl = drawable.getGL();
		gl.glClearColor(0, 0, 0.2f, 1);
		
		gl.setSwapInterval(0);
		
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		GL gl = drawable.getGL();
		camera.apply(gl, glu, width, height);
		
		/*float h = (float)width/(float)height;
		
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustum(-1, 1, -h, h, 0.1f, 5000);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0, 0, 0,  // Eye location
		              0, 0, 1,  // Look at
		              0, 1, 0); // Up
		gl.glTranslatef(0, -5, 5); //*/
		
		System.out.println("Resized.");
	}
	
	public static void main(String[] args)
	{
		// Because we're using the heavy-weight GLCanvas, all popups need to be
		// in front of the canvas
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		MainWindow window = new MainWindow();
		window.show();
	}
}
