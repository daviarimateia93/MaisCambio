package br.com.maiscambio.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;

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
	
	public static String loadFileFromClassPathAsString(String path)
	{
		InputStream sqlCreationInputStream = FileHelper.class.getClassLoader().getResourceAsStream(path);
		
		StringWriter sqlCreationStringWriter = new StringWriter();
		
		try
		{
			IOUtils.copy(sqlCreationInputStream, sqlCreationStringWriter, Constants.TEXT_CHARSET_UTF_8);
		}
		catch(IOException ioException)
		{
			return null;
		}
		
		return sqlCreationStringWriter.toString();
	}
}
