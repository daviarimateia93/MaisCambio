package br.com.maiscambio.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

public class JsonHelper
{
	public static Jackson2ObjectMapperBuilder getJackson2ObjectMapperBuilder()
	{
		Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
		jackson2ObjectMapperBuilder.simpleDateFormat(DateHelper.DATE_TIME_FORMAT_PATTERN);
		jackson2ObjectMapperBuilder.modules(new Hibernate4Module());
		
		return jackson2ObjectMapperBuilder;
	}
	
	public static String toString(Object object)
	{
		try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
		{
			getJackson2ObjectMapperBuilder().build().writeValue(byteArrayOutputStream, object);
			
			return byteArrayOutputStream.toString(Constants.TEXT_CHARSET_UTF_8);
		}
		catch(IOException ioException)
		{
			return null;
		}
	}
	
	public static <T> T toObject(String string, Class<T> type)
	{
		try
		{
			return getJackson2ObjectMapperBuilder().build().readValue(string, type);
		}
		catch(IOException ioException)
		{
			return null;
		}
	}
}
