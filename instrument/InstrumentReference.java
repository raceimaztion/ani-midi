package instrument;

import java.io.*;
import java.util.Vector;

public class InstrumentReference
{
	private static InstrumentReference singleton = null;
	private Vector<Reference> references;
	
	private InstrumentReference()
	{
		references = new Vector<Reference>();
		
		try
		{
			File dir = new File("models/");
			for (File f : dir.listFiles(InstrumentFilter.getSingleton()))
			{
				references.add(new Reference(f));
			}
		}
		catch (IOException er)
		{
			
		}
	}
	
	public static Vector<Reference> getInstrumentsSupporting(int patch)
	{
		if (singleton == null)
			singleton = new InstrumentReference();
		
		Vector<Reference> result = new Vector<Reference>();
		
		for (Reference r : singleton.references)
			if (r.isPatchSupported(patch))
				result.add(r);
		
		return result;
	}
	
	public static class Reference
	{
		private File source;
		private Vector<Integer> supportedPatches;
		
		public Reference(File file) throws IOException
		{
			supportedPatches = new Vector<Integer>();
			source = file;
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = in.readLine();
			while (line != null)
			{
				line = line.trim();
				if (line.startsWith("patches "))
				{
					if (line.contains("all"))
					{
						supportedPatches = null;
						return;
					}
					else
					{
						for (String s : line.substring(line.indexOf(" ") + 1).split(" "))
							supportedPatches.add(Integer.parseInt(s));
					}
				}
				else if (line.startsWith("instrument "))
				{
					System.out.printf("Found instrument called %s.\n", line.substring(line.indexOf(" ")).trim());
				}
				
				line = in.readLine();
			}
		}
		
		public File getSource()
		{
			return source;
		}
		
		public VirtualInstrument getInstrument()
		{
			return VirtualInstrument.loadVirtualInstrument(source.getPath());
		}
		
		public boolean isPatchSupported(int patch)
		{
			if (supportedPatches == null)
				return true;
			else
				return supportedPatches.contains((Integer)patch);
		}
	}
	
	public static class InstrumentFilter implements FileFilter
	{
		private static InstrumentFilter singleton;
		
		private InstrumentFilter() { }
		
		public boolean accept(File pathname)
		{
			return pathname.getName().endsWith(".ins");
		}
		
		public static InstrumentFilter getSingleton()
		{
			if (singleton == null)
				singleton = new InstrumentFilter();
			return singleton;
		}
	}
}
