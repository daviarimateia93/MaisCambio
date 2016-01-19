package br.com.maiscambio.util;

import org.springframework.web.servlet.ModelAndView;

import br.com.maiscambio.WebMvcConfig;

public class View extends ModelAndView
{
	private String layoutName;
	private String partialViewName;
	private String title;
	
	public static View redirect(String path)
	{
		return new View("redirect:" + path);
	}
	
	public View(String viewName)
	{
		this(viewName, null);
	}
	
	public View(String viewName, String title)
	{
		this(null, viewName, title);
	}
	
	public View(String layoutName, String partialViewName, String title)
	{
		boolean isRedirect = false;
		
		if(layoutName == null)
		{
			if((isRedirect = partialViewName.toUpperCase().startsWith("REDIRECT:")))
			{
				setViewName(partialViewName);
			}
			else
			{
				setViewName(WebMvcConfig.VIEW_RESOLVER_PREFIX + partialViewName + WebMvcConfig.VIEW_RESOLVER_SUFFIX);
			}
		}
		else
		{
			setViewName(WebMvcConfig.LAYOUT_RESOLVER_PREFIX + layoutName + WebMvcConfig.LAYOUT_RESOLVER_SUFFIX);
		}
		
		if(!isRedirect)
		{
			setPartialViewName(partialViewName);
			setTitle(title);
			
			addObject("__contextPath__", WebMvcConfig.getServletContext().getContextPath());
			addObject("__appName__", WebMvcConfig.APP_NAME);
			addObject("__appVersion__", WebMvcConfig.APP_VERSION);
		}
	}
	
	public String getLayoutName()
	{
		return layoutName;
	}
	
	public String getPartialViewName()
	{
		return partialViewName;
	}
	
	public void setPartialViewName(String partialViewName)
	{
		this.partialViewName = partialViewName;
		
		addObject("__partialViewFullName__", WebMvcConfig.VIEW_RESOLVER_PREFIX + partialViewName + WebMvcConfig.VIEW_RESOLVER_SUFFIX);
		addObject("__partialViewSimpleName__", partialViewName);
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
		
		addObject("__title__", title != null ? title : "");
	}
}
