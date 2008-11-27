import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.*;

import javax.sound.midi.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import graphics.*;
import instrument.*;


public class MidiWindow implements GLEventListener, ActionListener, Constants
{
	public static final int TIMER_TICK = 20;
	
	protected JFrame mainWindow;
	protected GLCanvas drawingCanvas;
	protected GLU glu;
	protected Camera camera;
	
	// Animation and timing:
	protected Timer timer;
	protected long lastTickTime;
	protected float totalTime;
	
	// Scene objects:
	protected VirtualInstrument instrument;
	protected Light light;
	protected MaterialLibrary materialLibrary;
	
	public MidiWindow(File midiFile)
	{
		this();
		
		// TODO: Load midi data from the file
		try
		{
			Sequence midiSequence = MidiSystem.getSequence(midiFile);
			Track[] midiTracks = midiSequence.getTracks();
			
			// Handle track 0
			MidiMessage msg;
			for (int num=0; num < midiTracks[0].size(); num ++)
			{
				msg = midiTracks[0].get(num).getMessage();
				if (msg instanceof MetaMessage)
				{
					MetaMessage meta = (MetaMessage)msg;
					if (meta.getType() == MIDI_TYPE_COMMENT)
						System.out.printf("MIDI file comment: '%s'\n", new String(meta.getData()));
					else if (meta.getType() == MIDI_TYPE_COPYRIGHT)
						System.out.printf("MIDI file copyright: '%s'\n", new String(meta.getData()));
					else if (meta.getType() == MIDI_TYPE_CUE)
						System.out.printf("MIDI file cue '%s'.\n", new String(meta.getData()));
					else if (meta.getType() == MIDI_TYPE_INSTRUMENT)
						System.out.printf("MIDI instrument %s.\n", new String(meta.getData()));
					else if (meta.getType() == MIDI_TYPE_LYRIC)
						System.out.printf("MIDI lyric: %s\n", new String(meta.getData()));
					else if (meta.getType() == MIDI_TYPE_MARKER)
						System.out.printf("MIDI marker: '%s'\n", new String(meta.getData()));
					else if (meta.getType() == MIDI_TYPE_TITLE)
						System.out.printf("MIDI file title: '%s'\n", new String(meta.getData()));
				}
			}
			
			for (int track=1; track < midiTracks.length; track ++)
			{
				if (track != 9)
				{
					
				}
				else // Drum track
				{
					
				}
			}
		}
		catch (InvalidMidiDataException er)
		{
			JOptionPane.showMessageDialog(null, "File does not contain proper MIDI data!", "AniMidi v0.5beta - Daryl Van Humbeck", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		catch (IOException er)
		{
			JOptionPane.showMessageDialog(null, "Error reading from file!", "AniMidi v0.5beta - Daryl Van Humbeck", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	public MidiWindow()
	{
		System.out.println("Loading all materials...");
		materialLibrary = new MaterialLibrary("models/instruments");
		materialLibrary.loadAllMaterials();
		System.out.println("Done loading materials.");
		
		System.out.println("Loading instruments...");
		instrument = VirtualInstrument.loadVirtualInstrument("models/tubular-bells.ins");
		instrument.assignTextures(materialLibrary);
		
		OscilationAnimation ani;
//		ani = new VibrationAnimation(new Position(0, 0, 0.02f));
		ani = new PendulumAnimation(AXIS_Y);
		ani.strike(10);
		
		Vector<InstrumentPart> parts = instrument.getAllByRoll("swings");
		for (InstrumentPart part : parts)
			part.setAnimation(ani.duplicate(), TEST_NOTE);
		
		light = new Light();
		light.setPosition(new Position(5, 5, 5));
		light.setAmbient(Color.gray.brighter());
		
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
		
		totalTime = 0;
		lastTickTime = System.currentTimeMillis();
		
		timer = new Timer(TIMER_TICK, this);
		timer.setCoalesce(true);
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
			
			needUpdate = instrument.animate(dTime, totalTime);
			
			if (needUpdate)
			{
				
			}
				drawingCanvas.display();
			
			lastTickTime = thisTick;
			totalTime += dTime;
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
		// Not sure if we need to do anything in here
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
		
//		JFileChooser fileChooser = new JFileChooser();
//		if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
//			return;
//		File midiFile = fileChooser.getSelectedFile();
		
//		MidiWindow window = new MidiWindow(midiFile);
		MidiWindow window = new MidiWindow();
		window.show();
	}
}
