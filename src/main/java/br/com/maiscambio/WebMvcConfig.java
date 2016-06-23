package br.com.maiscambio;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import me.gerenciar.util.Constants;
import me.gerenciar.util.CustomFilter;
import me.gerenciar.util.JsonHelper;
import me.gerenciar.util.WebApp;

@Configuration
@ComponentScan(basePackages = "${scan.controllerPackage}")
public class WebMvcConfig extends WebMvcConfigurationSupport implements WebApp
{
    @Autowired
    private ServletContext servletContext;
    
    @Autowired
    private Environment environment;
    
    private static final String APP_NAME = "MaisCambio";
    private static final String APP_VERSION = "1.4.3";
    private static final String VIEW_RESOLVER_PREFIX = "/WEB-INF/view/";
    private static final String VIEW_RESOLVER_SUFFIX = ".jsp";
    private static final String LAYOUT_RESOLVER_PREFIX = "/WEB-INF/layout/";
    private static final String LAYOUT_RESOLVER_SUFFIX = ".jsp";
    
    private static WebMvcConfig instance;
    
    public static final WebMvcConfig getInstance()
    {
        return instance;
    }
    
    @Override
    public ServletContext getServletContext()
    {
        return servletContext;
    }
    
    @Override
    public Environment getEnvironment()
    {
        return environment;
    }
    
    @Override
    public String getName()
    {
        return APP_NAME;
    }
    
    @Override
    public String getVersion()
    {
        return APP_VERSION;
    }
    
    @Override
    public String getLayoutResolverPrefix()
    {
        return LAYOUT_RESOLVER_PREFIX;
    }
    
    @Override
    public String getLayoutResolverSuffix()
    {
        return LAYOUT_RESOLVER_SUFFIX;
    }
    
    @Override
    public String getViewResolverPrefix()
    {
        return VIEW_RESOLVER_PREFIX;
    }
    
    @Override
    public String getViewResolverSuffix()
    {
        return VIEW_RESOLVER_SUFFIX;
    }
    
    @PostConstruct
    public void contextInitialized()
    {
        instance = this;
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = JsonHelper.getJackson2ObjectMapperBuilder();
        
        converters.add(new MappingJackson2HttpMessageConverter(jackson2ObjectMapperBuilder.build()));
        converters.add(new MappingJackson2XmlHttpMessageConverter(jackson2ObjectMapperBuilder.createXmlMapper(true).build()));
        converters.add(new BufferedImageHttpMessageConverter());
    }
    
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer)
    {
        asyncSupportConfigurer.setDefaultTimeout(30 * 1000L);
    }
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer)
    {
        defaultServletHandlerConfigurer.enable();
    }
    
    @Bean
    public OncePerRequestFilter oncePerRequestFilter()
    {
        return new CustomFilter();
    }
    
    @Bean
    public MultipartResolver multipartResolver()
    {
        return new CommonsMultipartResolver();
    }
    
    @Bean
    public MessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:messages");
        reloadableResourceBundleMessageSource.setCacheSeconds(5);
        reloadableResourceBundleMessageSource.setDefaultEncoding(Constants.TEXT_CHARSET_UTF_8);
        
        return reloadableResourceBundleMessageSource;
    }
    
    @Bean
    public ViewResolver viewResolver()
    {
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        
        return internalResourceViewResolver;
    }
    
    @Bean
    public DispatcherServlet dispatcherServlet()
    {
        return new DispatcherServlet();
    }
    
    @Bean
    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping()
    {
        List<Object> interceptors = getInterceptorsAsList();
        interceptors.add(new CustomHandlerInterceptorAdapter());
        
        RequestMappingHandlerMapping requestMappingHandlerMapping = super.requestMappingHandlerMapping();
        requestMappingHandlerMapping.setInterceptors(interceptors.toArray(new Object[interceptors.size()]));
        
        return requestMappingHandlerMapping;
    }
    
    private List<Object> getInterceptorsAsList()
    {
        List<Object> interceptorsList = new ArrayList<>();
        
        Object[] interceptors = getInterceptors();
        
        if(interceptors != null)
        {
            for(Object interceptor : interceptors)
            {
                interceptorsList.add(interceptor);
            }
        }
        
        return interceptorsList;
    }
}
