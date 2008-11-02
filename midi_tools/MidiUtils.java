package midi_tools;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import javax.sound.midi.*;

public class MidiUtils
{
	public static BufferedImage createImageFromMidiFile(Sequence midi)
	{
		int width = (int)midi.getTickLength() * 2;
		int height = 2 * 128;
		int scale = 2;
		
		long noteOn = 0, noteOff = 0;
		
		BufferedImage result = null;
		
		do
		{
			try
			{
				result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			}
			catch (OutOfMemoryError er)
			{
				width /= 2;
				scale = 1;
				continue;
			}
		}
		while (result == null);
		
		System.out.printf("Image size: %dx%d\n", width, height);
		System.out.printf("Number of tracks: %d\n", midi.getTracks().length);
		
		Graphics2D g = (Graphics2D)result.getGraphics();
		
		// Fill the background with white
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		
		// For each track, fill in where the notes are being played
		long[] noteList = new long[128];
		
		Track cur;
		MidiMessage msg;
		ShortMessage noteCmd;
		long oldestTime = 0;
		for (int i=1; i < midi.getTracks().length; i++)
		{
			Arrays.fill(noteList, -1);
			oldestTime = 0;
			
			if (i != 9)
				g.setColor(Color.black);
			else
				g.setColor(Color.red);
			
			cur = midi.getTracks()[i];
			for (int j=0; j < cur.size(); j++)
			{
				msg = cur.get(j).getMessage();
				if (!(msg instanceof ShortMessage)) continue;
				
				noteCmd = (ShortMessage)msg;
				int note = noteCmd.getData1();
				
				if (noteCmd.getCommand() == ShortMessage.NOTE_ON)
				{
					noteList[note] = cur.get(j).getTick();
					noteOn ++;
					
					drawLine(g, scale*(int)noteList[note], scale*((int)noteList[note]+1), note);
				}
				else if (noteCmd.getCommand() == ShortMessage.NOTE_OFF)
				{
					drawLine(g, scale*(int)noteList[note], scale*(int)cur.get(j).getTick(), note);
					noteList[note] = -1;
					noteOff ++;
				}
				else continue;
				
				if (oldestTime < cur.get(j).getTick())
					oldestTime = cur.get(j).getTick();
			} // end for each message
			
			/*for (int note=0; note < 128; note++)
			{
				if (noteList[note] < 0)  continue;
				
				drawLine(g, (int)noteList[note], (int)oldestTime, note);
			}*/
		} // end for each track
		
		result.flush();
		
		System.out.printf("Number of note-on messages: %d\nNumber of note-off messages: %d\n", noteOn, noteOff);
		
		return result;
	}
	
	public static void drawLine(Graphics2D g, int start, int end, int y)
	{
		if (start < 0) return;
		
		g.fillRect(start, y*2, (end - start + 1), 2);
	}
}
