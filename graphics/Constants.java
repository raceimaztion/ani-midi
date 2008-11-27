package graphics;

import com.sun.opengl.util.GLUT;

public interface Constants
{
	public static final int AXIS_X = 0x1001;
	public static final int AXIS_Y = 0x1002;
	public static final int AXIS_Z = 0x1004;
	
	public static final String REGEX_FLOAT = "-?[0-9]+(\\.[0-9]+)?";
	
	public static final GLUT GL_UT = new GLUT();
	
	public static final int TEST_NOTE = 63;
	
	public static final int MIDI_TYPE_COMMENT = 1;
	public static final int MIDI_TYPE_COPYRIGHT = 2;
	public static final int MIDI_TYPE_TITLE = 3;
	public static final int MIDI_TYPE_INSTRUMENT = 4;
	public static final int MIDI_TYPE_LYRIC = 5;
	public static final int MIDI_TYPE_MARKER = 6;
	public static final int MIDI_TYPE_CUE = 7;
}
