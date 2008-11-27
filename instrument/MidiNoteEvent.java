package instrument;

public class MidiNoteEvent
{
	/**
	 * The time at which this event occurs
	 */
	private float time;
	
	/**
	 * The note this event affects
	 */
	private int note;
	
	/**
	 * If this is a note on message
	 */
	private boolean noteOn;
	
	/**
	 * The velocity at which this note is struck or released
	 */
	private float velocity;
	
	public MidiNoteEvent(float time, int note, boolean noteOn, int velocity)
	{
		this.time = time;
		this.note = note;
		this.noteOn = noteOn;
		this.velocity = (float)velocity/255;
	}

	public int getNote()
	{
		return note;
	}

	public boolean isNoteOn()
	{
		return noteOn;
	}

	public float getTime()
	{
		return time;
	}

	public float getVelocity()
	{
		return velocity;
	}
}
