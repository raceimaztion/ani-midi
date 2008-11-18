import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import graphics.*;
import instrument.*;

public class MainWindow implements GLEventListener, ActionListener
{
	public static final int TIMER_TICK = 10;
	
	protected JFrame mainWindow;
	protected GLCanvas drawingCanvas;
	protected GLU glu;
	protected Camera camera;
	
	// Animation and timing:
	protected Timer timer;
	protected long lastTickTime;
	
	// Scene objects:
	protected VirtualInstrument instrument;
	protected Light light;
	protected MaterialLibrary materialLibrary;
	
	public MainWindow()
	{
		materialLibrary = new MaterialLibrary("models/instruments");
		materialLibrary.loadAllMaterials();
		
		instrument = VirtualInstrument.loadVirtualInstrument("models/tubular-bells.ins");
		instrument.assignTextures(materialLibrary);
		
		InstrumentPart part = instrument.getParts().get(0).getChildren().get(0);
		PendulumAnimation ani = new PendulumAnimation(part, Constants.AXIS_Y);
		part.setAnimation(ani);
		ani.strike(10);
		
		light = new Light();
		light.setPosition(new Position(5, 5, 5));
		light.setAmbient(Color.gray);
		
		mainWindow = new JFrame("MIDI Visualizer - by Daryl Van Humbeck");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		drawingCanvas = new GLCanvas();
		drawingCanvas.addGLEventListener(this);
		drawingCanvas.setAutoSwapBufferMode(false);
		Dimension d = new Dimension(640, 480);
		drawingCanvas.setPreferredSize(d);
		drawingCanvas.setMinimumSize(d);
		
		glu = new GLU();
		camera = new Camera();
		camera.move(0, 2, 0);
		
		mainWindow.getContentPane().add(drawingCanvas, BorderLayout.CENTER);
		mainWindow.pack();
		mainWindow.setLocationRelativeTo(null);
		
		lastTickTime = System.currentTimeMillis();
		timer = new Timer(TIMER_TICK, this);
		timer.start();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		
		if (source.equals(timer))
		{
			// This is a timer tick event
			long thisTick = System.currentTimeMillis();
			float dTime = 0.001f*(thisTick - lastTickTime);
			boolean needUpdate = false;
			
			needUpdate = instrument.animate(dTime);
			
			if (needUpdate)
				drawingCanvas.display();
			lastTickTime = thisTick;
		}
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
		
		instrument.draw(gl);
		
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
		drawable.setGL(new DebugGL(drawable.getGL()));
		
		GL gl = drawable.getGL();
		gl.glClearColor(0, 0, 0.3f, 1);
		
		gl.setSwapInterval(0);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_NORMALIZE);
		gl.glShadeModel(GL.GL_SMOOTH);
		
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
