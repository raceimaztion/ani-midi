import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import javax.sound.midi.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import graphics.*;
import instrument.*;

public class MidiWindow implements GLEventListener, ActionListener, Constants
{
	public static final int TIMER_TICK = 100;
	
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
	protected Vector<VirtualInstrument> allInstruments;
	
	// File-specific
	protected Sequence songSequence;
	protected String songTitle;
	protected float songDuration;
	protected boolean maskTop = false;
	
	private boolean drawing = false, animating = false;
	
	public MidiWindow(File midiFile)
	{
		this();
		timer.stop();
		
		allInstruments = new Vector<VirtualInstrument>();
		if (instrument != null)
			allInstruments.add(instrument);
		
		try
		{
			System.out.println("Starting to read MIDI file...");
			
			songSequence = MidiSystem.getSequence(midiFile);
			
			songDuration = 0.001f * songSequence.getMicrosecondLength();
			
			Track[] midiTracks = songSequence.getTracks();
			
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
					{
						songTitle = new String(meta.getData());
						System.out.printf("MIDI file title: '%s'\n", songTitle);
					}
					else if (meta.getData().length >= 3 && meta.getData()[0] == 127)
					{
						byte[] data = meta.getData();
						if (data[1] == 0 && data[2] == 0 && data[3] == 'A')
							maskTop = true;
					}
				}
			} // end for all events in the first track
			
			float secondsPerTick;
			secondsPerTick = 10.0f / songSequence.getResolution();
				
			
			Track curTrack;
			int curPatch = -1;
			for (int track=1; track < midiTracks.length; track ++)
			{
				// If this is a Microsoft MPC MIDI file, we skip the top half
				if (maskTop && track > 9)
					break;
				
				curTrack = midiTracks[track];
				if (track != 9)
				{
					for (int num=0; num < curTrack.size(); num++)
					{
						msg = curTrack.get(num).getMessage();
						if (msg instanceof ShortMessage)
						{
							ShortMessage sm = (ShortMessage)msg;
							if (sm.getCommand() == ShortMessage.NOTE_ON)
							{
								if (curPatch < 0)
									continue;
								
								instrument.addNote(new MidiNoteEvent(secondsPerTick*curTrack.get(num).getTick(), sm.getData1(), true, sm.getData2()));
							}
							else if (sm.getCommand() == ShortMessage.NOTE_OFF)
							{
								if (curPatch < 0)
									continue;
								
								instrument.addNote(new MidiNoteEvent(secondsPerTick*curTrack.get(num).getTick(), sm.getData1(), false, sm.getData2()));
							}
							else if (sm.getCommand() == ShortMessage.PROGRAM_CHANGE)
							{
								curPatch = sm.getData1();
								Vector<InstrumentReference.Reference> references = InstrumentReference.getInstrumentsSupporting(curPatch);
								if (references.size() < 1)
									curPatch = -1;
								else
								{
									instrument = references.get(0).getInstrument();
									allInstruments.add(instrument);
								}
							}
						}
					} // end for each message
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
		finally
		{
			System.out.println("Done reading MIDI file.");
			
			for (VirtualInstrument vi : allInstruments)
				vi.assignTextures(materialLibrary);
			
			OscilationAnimation ani = new PendulumAnimation(AXIS_Y);
			ani.strike(3);
			Vector<InstrumentPart> list = instrument.getAllByRoll("swings");
			if (list != null)
				for (InstrumentPart p : list)
				{
					p.setAnimation(ani);
					
					for (int note=0; note < 128; note++)
					{
						for (int n=0; n < 5; n++)
							((OscilationAnimation)p.getAnimationForNote(note)).animate(0.02f*note, 0.02f*note);
					}
				}
			
			lastTickTime = System.currentTimeMillis();
			timer.start();
		}
	}
	
	public MidiWindow()
	{
		System.out.println("Loading all materials...");
		materialLibrary = new MaterialLibrary("models/instruments");
		materialLibrary.loadAllMaterials();
		System.out.println("Done loading materials.");
		
		System.out.println("Loading instruments...");
//		instrument = VirtualInstrument.loadVirtualInstrument("models/celesta.ins");
//		instrument.assignTextures(materialLibrary);
		
//		OscilationAnimation ani;
//		ani = new VibrationAnimation(new Position(0, 0, 0.02f));
//		ani = new PendulumAnimation(AXIS_Y);
//		ani.strike(10);
		
//		Vector<InstrumentPart> parts = instrument.getAllByRoll("swings");
//		for (InstrumentPart part : parts)
//			part.setAnimation(ani.duplicate(), TEST_NOTE);
//			((OscilationAnimation)part.getAnimationForNote(TEST_NOTE)).strike(10);
		
		System.out.println("Done loading instruments.");
		
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
		camera.move(-10, 0, 1);
		camera.rotate(0, -90);
		
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
			if (drawing) return;
			animating = true;
			
			// This is a timer tick event
			long thisTick = System.currentTimeMillis();
			float dTime = 0.001f*(thisTick - lastTickTime);
			boolean needUpdate = false;
			
//			for (VirtualInstrument ins : allInstruments)
//				if (ins.animate(dTime, totalTime))
//					needUpdate = true;
			needUpdate = instrument.animate(dTime, totalTime);
			animating = false;
			
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
		if (animating) return;
		drawing = true;
		
		GL gl = drawable.getGL();
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);
		
		gl.glPushMatrix();
//		for (VirtualInstrument ins : allInstruments)
//		{
//			ins.draw(gl);
//			gl.glTranslatef(-2, 0, 0);
//		}
		allInstruments.get(allInstruments.size()-1).draw(gl);
		gl.glPopMatrix();
		
		gl.glFlush();
		if (!drawable.getAutoSwapBufferMode())
			drawable.swapBuffers();
		
		drawing = false;
	}
	
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged)
	{
		// Not sure if we need to do anything in here
	}
	
	public void init(GLAutoDrawable drawable)
	{
		drawable.setGL(new DebugGL(drawable.getGL()));
		
		GL gl = drawable.getGL();
		gl.glClearColor(0.1f, 0.4f, 0.3f, 1);
		
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
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File f)
			{
				return f.isDirectory() || f.getName().endsWith(".mid");
			}
			public String getDescription()
			{
				return "MIDI files (*.mid)";
			}
			});
		if (fileChooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;
		File midiFile = fileChooser.getSelectedFile();
		
		MidiWindow window = new MidiWindow(midiFile);
//		MidiWindow window = new MidiWindow();
		window.show();
	}
}
