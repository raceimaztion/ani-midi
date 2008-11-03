package graphics.file;

import java.io.File;
import java.io.FileFilter;

public class ObjectFile implements FileFilter
{
	protected static ObjectFile singleton = null;
	
	public boolean accept(File pathname)
	{
		return pathname.getName().endsWith(".obj");
	}
	
	public static ObjectFile getSingleton()
	{
		if (singleton == null)
			singleton = new ObjectFile();
		return singleton;
	}
}
