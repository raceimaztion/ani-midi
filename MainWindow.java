import java.awt.*;
import javax.swing.*;
// Jogl imports
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
// Project imports
import graphics.*;
import graphics.Shape;

public class MainWindow implements GLEventListener
{
	protected JFrame mainWindow;
	protected GLCanvas drawingCanvas;
	protected GLU glu;
	protected Camera camera;
	
	// Scene objects:
	protected Shape shape;
	protected Light light;
	protected MaterialLibrary materialLibrary;
	
	public MainWindow()
	{
		materialLibrary = new MaterialLibrary("models/instruments");
		shape = Shape.loadWavefrontObject("models/instruments/bell-holder.obj");
		shape.setRotation(new SingleRotation(120, 0, 0, 1));
		light = new Light();
		light.setPosition(new Position(5, 5, 5));
		light.setAmbient(Color.gray);
		shape.setMaterial(materialLibrary.getMaterial("Metal"));
		
		mainWindow = new JFrame("MIDI Visualizer - by Daryl Van Humbeck");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		drawingCanvas = new GLCanvas();
		drawingCanvas.addGLEventListener(this);
		Dimension d = new Dimension(640, 480);
		drawingCanvas.setPreferredSize(d);
		drawingCanvas.setMinimumSize(d);
		
		glu = new GLU();
		camera = new Camera();
		camera.move(0, 2, 0);
		
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
		GL gl = drawable.getGL();
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);
		
		//gl.glColor3f(0.5f, 0.5f, 0.5f);
		
		shape.draw(gl);
		
		gl.glFlush();
		if (!drawable.getAutoSwapBufferMode())
			drawable.swapBuffers();
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
		gl.glClearColor(0, 0, 0.3f, 1);
		
		gl.setSwapInterval(0);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_NORMALIZE);
		
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		light.apply(gl, GL.GL_LIGHT0);
	}
	
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{
		GL gl = drawable.getGL();
		camera.apply(gl, glu, width, height);
	}
	
	public static void main(String[] args)
	{
		// Because we're using the heavy-weight GLCanvas, all popups need to be in front of the canvas
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		
		MainWindow window = new MainWindow();
		window.show();
	}
}
