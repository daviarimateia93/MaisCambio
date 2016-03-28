package br.com.maiscambio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateHelper
{
	public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	
	public static Date setTimeZone(Date date, String timeZone)
	{
		TimeZone foundTimeZone = TimeZone.getTimeZone(timeZone);
		
		if(foundTimeZone == null)
		{
			return date;
		}
		else
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateHelper.DATE_TIME_FORMAT_PATTERN);
			simpleDateFormat.setTimeZone(foundTimeZone);
			
			return parse(simpleDateFormat.format(date));
		}
	}
	
	public static Date parse(String date)
	{
		return parse(date, DATE_TIME_FORMAT_PATTERN);
	}
	
	public static Date parse(String date, String pattern)
	{
		if(date == null)
		{
			return null;
		}
		
		try
		{
			return new SimpleDateFormat(pattern).parse(date);
		}
		catch(ParseException parseException)
		{
			return null;
		}
	}
	
	public static String format(Date date)
	{
		return format(date, DATE_TIME_FORMAT_PATTERN);
	}
	
	public static String format(Date date, String pattern)
	{
		return date == null ? Constants.TEXT_EMPTY : new SimpleDateFormat(pattern).format(date);
	}
	
	public static Calendar toCalendar(Date date)
	{
		if(date == null)
		{
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar;
	}
	
	public static Integer get(Date date, int type)
	{
		if(date == null)
		{
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return calendar.get(type);
	}
	
	public static Date setFirstForTime(Date date)
	{
		if(date == null)
		{
			return null;
		}
		
		Calendar calendar = toCalendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	public static Date setMiddleForTime(Date date)
	{
		if(date == null)
		{
			return null;
		}
		
		Calendar calendar = toCalendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		
		return calendar.getTime();
	}
	
	public static Date setLastForTime(Date date)
	{
		if(date == null)
		{
			return null;
		}
		
		Calendar calendar = toCalendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		
		return calendar.getTime();
	}
	
	public static Date setLastForYear(Date date)
	{
		if(date == null)
		{
			return null;
		}
		
		Calendar calendar = toCalendar(date);
		calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
		
		return setLastForTime(setLastForMonth(calendar.getTime()));
	}
	
	public static Date setLastForMonth(Date date)
	{
		if(date == null)
		{
			return null;
		}
		
		Calendar calendar = toCalendar(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return setLastForTime(calendar.getTime());
	}
	
	public static Date setLastForWeek(Date date)
	{
		if(date == null)
		{
			return null;
		}
		
		Calendar calendar = toCalendar(date);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		calendar.add(Calendar.DAY_OF_YEAR, 6);
		
		return setLastForTime(calendar.getTime());
	}
	
	public static Date setLastForDay(Date date)
	{
		return setLastForTime(date);
	}
	
	public static List<Date> getYearsBetween(Date startDate, Date endDate)
	{
		if(endDate.compareTo(startDate) == -1)
		{
			return null;
		}
		
		List<Date> years = new ArrayList<>();
		
		for(int year = get(startDate, Calendar.YEAR); year <= get(endDate, Calendar.YEAR); year++)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, calendar.getActualMaximum(Calendar.MONTH), 1);
			
			years.add(setLastForYear(calendar.getTime()));
		}
		
		return years;
	}
	
	public static List<Date> getMonthsBetween(Date startDate, Date endDate)
	{
		if(endDate.compareTo(startDate) == -1)
		{
			return null;
		}
		
		List<Date> years = getYearsBetween(startDate, endDate);
		List<Date> months = new ArrayList<>();
		
		for(int i = 0; i < years.size(); i++)
		{
			for(int month = i == 0 ? get(startDate, Calendar.MONTH) : 0; month <= (i == years.size() - 1 ? get(endDate, Calendar.MONTH) : 11); month++)
			{
				Calendar calendar = Calendar.getInstance();
				calendar.set(get(years.get(i), Calendar.YEAR), month, 1);
				
				months.add(setLastForMonth(calendar.getTime()));
			}
		}
		
		return months;
	}
	
	public static List<Date> getWeeksBetween(Date startDate, Date endDate)
	{
		if(endDate.compareTo(startDate) == -1)
		{
			return null;
		}
		
		List<Date> months = getMonthsBetween(startDate, endDate);
		List<Date> weeks = new ArrayList<>();
		
		for(int i = 0; i < months.size(); i++)
		{
			for(int week = i == 0 ? get(startDate, Calendar.WEEK_OF_MONTH) : 1; week <= (i == months.size() - 1 ? get(endDate, Calendar.WEEK_OF_MONTH) : toCalendar(months.get(i)).getActualMaximum(Calendar.WEEK_OF_MONTH)); week++)
			{
				Calendar calendar = Calendar.getInstance();
				calendar.set(get(months.get(i), Calendar.YEAR), get(months.get(i), Calendar.MONTH), 1);
				calendar.set(Calendar.WEEK_OF_MONTH, week);
				
				weeks.add(setLastForWeek(calendar.getTime()));
			}
		}
		
		return weeks;
	}
	
	public static List<Date> getDaysBetween(Date startDate, Date endDate)
	{
		if(endDate.compareTo(startDate) == -1)
		{
			return null;
		}
		
		List<Date> months = getMonthsBetween(startDate, endDate);
		List<Date> days = new ArrayList<>();
		
		for(int i = 0; i < months.size(); i++)
		{
			for(int day = i == 0 ? get(startDate, Calendar.DAY_OF_MONTH) : 1; day <= (i == months.size() - 1 ? get(endDate, Calendar.DAY_OF_MONTH) : toCalendar(months.get(i)).getActualMaximum(Calendar.DAY_OF_MONTH)); day++)
			{
				Calendar calendar = Calendar.getInstance();
				calendar.set(get(months.get(i), Calendar.YEAR), get(months.get(i), Calendar.MONTH), day);
				
				days.add(setLastForDay(calendar.getTime()));
			}
		}
		
		return days;
	}
	
	public static Long differenceInMilliseconds(Date date1, Date date2)
	{
		return date1.getTime() - date2.getTime();
	}
	
	public static Long differenceInSeconds(Date date1, Date date2)
	{
		return differenceInMilliseconds(date1, date2) / 1000;
	}
	
	public static Long differenceInMinutes(Date date1, Date date2)
	{
		return differenceInSeconds(date1, date2) / 60;
	}
	
	public static Long differenceInHours(Date date1, Date date2)
	{
		return differenceInMinutes(date1, date2) / 60;
	}
}
