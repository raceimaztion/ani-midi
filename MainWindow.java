
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
		camera.move(0, -3, 0);
		
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
		
		//gl.glColor3f(0.6f, 0.4f, 0.5f);
		gl.glColor3f(1, 1, 1);
		
    gl.glDisable(GL.GL_DEPTH_TEST);
    
		gl.glBegin(GL.GL_QUADS);
    // One way round:
    gl.glVertex3f(1, 0, 1);
		gl.glVertex3f(1, 0, -1);
    gl.glVertex3f(-1, 0, -1);
		gl.glVertex3f(-1, 0, 1);
    // Other way round:
    gl.glVertex3f(-1, 0, -1);
    gl.glVertex3f(1, 0, -1);
    gl.glVertex3f(1, 0, 1);
    gl.glVertex3f(-1, 0, 1);
		gl.glEnd();
		
    gl.glEnable(GL.GL_DEPTH_TEST);
    
    //drawable.swapBuffers();
    
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
		GL gl = drawable.getGL();
    gl.glClearColor(0, 0, 0.3f, 1);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		GL gl = drawable.getGL();
		
		camera.apply(gl, glu, width, height);
    System.out.println("Resized.");
	}
	
	public static void main(String[] args)
	{
		// Because we're using the heavy-weight GLCanvas, all popups need to be in front of the canvas
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		MainWindow window = new MainWindow();
		window.show();
	}
}
