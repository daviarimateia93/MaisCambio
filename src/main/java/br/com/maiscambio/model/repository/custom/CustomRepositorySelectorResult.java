package br.com.maiscambio.model.repository.custom;

import java.util.List;

import javax.persistence.criteria.Selection;

public class CustomRepositorySelectorResult
{
	private List<Selection<?>> selections = null;
	private List<Handmade> handmades = null;
	
	public CustomRepositorySelectorResult(List<Selection<?>> selections, List<Handmade> handmades)
	{
		this.selections = selections;
		this.handmades = handmades;
	}
	
	public List<Selection<?>> getSelections()
	{
		return selections;
	}
	
	public List<Handmade> getHandmades()
	{
		return handmades;
	}
	
	public static class Handmade
	{
		private Class<?> type;
		private String fieldName;
		private String filterName;
		private String filterValue;
		
		public Handmade(Class<?> type, String fieldName, String filterName, String filterValue)
		{
			this.type = type;
			this.fieldName = fieldName;
			this.filterName = filterName;
			this.filterValue = filterValue;
		}
		
		public Class<?> getType()
		{
			return type;
		}
		
		public String getFieldName()
		{
			return fieldName;
		}
		
		public String getFilterName()
		{
			return filterName;
		}
		
		public String getFilterValue()
		{
			return filterValue;
		}
	}
}
