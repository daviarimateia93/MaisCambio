package br.com.maiscambio.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

public class FileHelper
{
	public static File[] getFilesFromPath(ServletContext servletContext, String path)
	{
		return new File(servletContext.getRealPath(path)).listFiles();
	}
	
	public static String[] getFilesNamesFromPath(ServletContext servletContext, String path)
	{
		List<String> filesName = new ArrayList<>();
		
		for(File file : getFilesFromPath(servletContext, path))
		{
			filesName.add(file.getName());
		}
		
		return filesName.toArray(new String[filesName.size()]);
	}
}
