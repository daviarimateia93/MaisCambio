package br.com.maiscambio;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import br.com.maiscambio.util.Constants;

public class SpringWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
	private static final int MAX_UPLOAD_SIZE_IN_MB = 10 * 1024 * 1024; // 10MB
	
	@Override
	protected Class<?>[] getRootConfigClasses()
	{
		return new Class<?>[] { AppConfig.class };
	}
	
	@Override
	protected Class<?>[] getServletConfigClasses()
	{
		return new Class<?>[] { WebMvcConfig.class };
	}
	
	@Override
	protected String[] getServletMappings()
	{
		return new String[] { String.valueOf(Constants.CHAR_SLASH) };
	}
	
	@Override
	protected Filter[] getServletFilters()
	{
		return new Filter[] { new MultipartFilter(), new OpenEntityManagerInViewFilter() };
	}
	
	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration)
	{
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"), MAX_UPLOAD_SIZE_IN_MB, MAX_UPLOAD_SIZE_IN_MB, MAX_UPLOAD_SIZE_IN_MB);
		
		registration.setMultipartConfig(multipartConfigElement);
	}
}