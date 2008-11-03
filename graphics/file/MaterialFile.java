package graphics.file;

import java.io.File;
import java.io.FileFilter;

public class MaterialFile implements FileFilter
{
	protected static MaterialFile singleton = null;
	
	public boolean accept(File pathname)
	{
		return pathname.getName().endsWith(".mtl");
	}
	
	public static MaterialFile getSingleton()
	{
		if (singleton == null)
			singleton = new MaterialFile();
		return singleton;
	}
}
