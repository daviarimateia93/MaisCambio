package br.com.maiscambio.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import br.com.maiscambio.WebMvcConfig;

public class FileMemoryReader
{
	public static byte[] readAsByteArray(String name)
	{
		try
		{
			InputStream inputStream = new FileInputStream(WebMvcConfig.getServletContext().getRealPath(name));
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			
			int read;
			
			while((read = inputStream.read()) != -1)
			{
				byteArrayOutputStream.write(read);
			}
			
			inputStream.close();
			
			return byteArrayOutputStream.toByteArray();
		}
		catch(IOException exception)
		{
			return null;
		}
	}
	
	public static String readAsString(String name)
	{
		byte[] bytes = readAsByteArray(name);
		
		return bytes != null ? new String(bytes) : null;
	}
}
