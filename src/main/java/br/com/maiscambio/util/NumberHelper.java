package br.com.maiscambio.util;

import org.springframework.util.StringUtils;

public class NumberHelper
{
	public static Number valueOf(String string)
	{
		if(!StringUtils.isEmpty(string))
		{
			if(string.contains(String.valueOf(Constants.CHAR_DOT)))
			{
				return Double.valueOf(string);
			}
			else
			{
				return Long.valueOf(string);
			}
		}
		
		return null;
	}
}
