package graphics;

import java.io.*;
import java.util.*;
import graphics.file.*;

public class MaterialLibrary
{
	protected String materialFolder;
	protected Map<String, Material> materialsList;
	
	public MaterialLibrary(String materialFolder)
	{
		this.materialFolder = materialFolder;
		materialsList = new HashMap<String,Material>();
	}
	
	public void loadAllMaterials()
	{
		File root = new File(materialFolder);
		
		File[] list = root.listFiles(MaterialFile.getSingleton());
		Material m;
		for (File f : list)
		{
			m = Material.loadMaterial(f.getPath());
			materialsList.put(m.getName(), m);
		}
	}
	
	public Material getMaterial(String name)
	{
		Material result = materialsList.get(name);
		
		if (result == null)
		{
			result = Material.loadMaterial(materialFolder + "/" + name + ".mtl");
			if (result != null)
				materialsList.put(result.getName(), result);
		}
		
		return result;
	}
}
