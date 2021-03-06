package br.com.maiscambio.model.repository.custom;

import java.util.List;

import javax.persistence.criteria.Selection;

public class CustomRepositorySelectorResult
{
	private List<SelectionWrapper<?>> selectionWrappers = null;
	private List<Handmade> handmades = null;
	
	public CustomRepositorySelectorResult(List<SelectionWrapper<?>> selectionWrappers, List<Handmade> handmades)
	{
		this.selectionWrappers = selectionWrappers;
		this.handmades = handmades;
	}
	
	public List<SelectionWrapper<?>> getSelectionWrappers()
	{
		return selectionWrappers;
	}
	
	public List<Handmade> getHandmades()
	{
		return handmades;
	}
	
	public static class SelectionWrapper<T>
	{
		private String customAlias;
		private Selection<T> selection;
		
		public SelectionWrapper(String customAlias, Selection<T> selection)
		{
			this.customAlias = customAlias;
			this.selection = selection;
		}
		
		public String getCustomAlias()
		{
			return customAlias;
		}
		
		public Selection<T> getSelection()
		{
			return selection;
		}
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
