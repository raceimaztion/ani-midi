import java.io.*;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.sound.midi.*;

import midi_tools.*;

public class MidiTest
{
	public static final String FILE_2001 = "midi_files/2001.mid";
	public static final String FILE_GOOD_BAD_UGLY = "midi_files/Good_Bad_Ugly.mid";
	public static final String FILE_PINK_PANTHER = "midi_files/Pink_Panther.mid";
	public static final String FILE_FIRST_CONTACT = "midi_files/Star_Trek_First_Contact.mid";
	public static final String FILE_STAR_WARS = "midi_files/Star_Wars.mid";
	public static final String FILE_ECSTASY_OF_GOLD = "midi_files/The_Ecstasy_Of_Gold.mid";
	
	public static final String FILE_NAME = FILE_2001;
	
	public static void displayMidiData(Sequence sequence, String fileName)
	{
		Track[] trackList = sequence.getTracks();
		
		System.out.printf("In file %s:\n", FILE_NAME);
		System.out.printf("Number of tracks: %d\n", trackList.length);
		for (int i=0; i < trackList.length; i++)
		{
			System.out.printf("\tTrack #%2d:\n", i+1);
			System.out.printf("\t\tContains %7d events and lasts for %7d MIDI ticks.\n", trackList[i].size(), trackList[i].ticks());
			int numShort = 0, numSysex = 0, numMeta = 0;
			for (int j=0; j < trackList[i].size(); j++)
			{
				MidiMessage msg = trackList[i].get(j).getMessage();
				if (msg instanceof ShortMessage)
					numShort ++;
				else if (msg instanceof SysexMessage)
					numSysex ++;
				else if (msg instanceof MetaMessage)
					numMeta ++;
			}
			System.out.printf("\t\tNumber of messages: %6d short messages, %4d sysex messages, and %4d meta messages.\n", numShort, numSysex, numMeta);
		}
		System.out.printf("Information in the first track:\n");
		Track header = trackList[0];
		for (int i=0; i < header.size(); i++)
		{
			MidiMessage m = header.get(i).getMessage();
			System.out.printf("Message #%3d:\n", i+1);
			System.out.printf("\tLength: %d bytes\n", m.getLength());
			System.out.print("\t0x");
			for (byte b : m.getMessage())
				System.out.printf("%02x", b);
			System.out.println();
			int type = m.getMessage()[1];
			if (type == 0)
			{
				System.out.println("\tSequence number event.");
			}
			else if (type > 0 && type < 16)
			{
				System.out.printf("\tText event: \"%s\"\n", new String(m.getMessage(), 3, m.getLength()-3));
			}
			else if (type == 47)
				System.out.println("\tEnd of track marker.");
			else if (type == 81)
				System.out.println("\tTempo information event.");
			else if (type == 88)
				System.out.println("\tTime signature event.");
			else if (type == 89)
				System.out.println("\tKey signature event.");
			else if (type == 127)
				System.out.println("\tSequencer-specific event.");
			else
				System.out.println("\tUnknown event type.");
		}
	}
	
	public static void main(String[] args)
	{
		try
		{
			Sequence sequence = MidiSystem.getSequence(new File(FILE_NAME));
			
			displayMidiData(sequence, FILE_NAME);
			
//			BufferedImage image = MidiUtils.createImageFromMidiFile(sequence);
//			
//			JFrame frame = new JFrame("Midi Data");
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.setBackground(Color.white);
//			
//			JScrollPane scroller = new JScrollPane(new JLabel(new ImageIcon(image)));
//			Dimension d = new Dimension(640, 480);
//			scroller.setPreferredSize(d);
//			scroller.setMinimumSize(d);
//			scroller.getHorizontalScrollBar().setUnitIncrement(8);
//			scroller.getVerticalScrollBar().setUnitIncrement(8);
//			frame.getContentPane().add(scroller, BorderLayout.CENTER);
//			
//			frame.pack();
//			frame.setLocationRelativeTo(null);
//			frame.setVisible(true);
		}
		catch (IOException er)
		{
			System.out.println(er);
			er.printStackTrace();
		}
		catch (InvalidMidiDataException er)
		{
			System.out.println(er);
			er.printStackTrace();
		}
		catch (Throwable er)
		{
			System.out.println(er);
			er.printStackTrace();
		}
	}
}
