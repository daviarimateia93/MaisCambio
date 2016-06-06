package br.com.maiscambio;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
@ComponentScan(basePackages = "${scan.basePackage}", excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class))
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableCaching
@EnableAsync
public class AppConfig
{
	@Autowired
	private Environment environment;
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
	{
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public JavaMailSenderImpl javaMailSenderImpl()
	{
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setHost(environment.getProperty("smtp.host"));
		javaMailSenderImpl.setPort(environment.getProperty("smtp.port", Integer.class));
		javaMailSenderImpl.setProtocol(environment.getProperty("smtp.protocol"));
		javaMailSenderImpl.setUsername(environment.getProperty("smtp.username"));
		javaMailSenderImpl.setPassword(environment.getProperty("smtp.password"));
		
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable", true);
		
		javaMailSenderImpl.setJavaMailProperties(properties);
		
		return javaMailSenderImpl;
	}
	
	@Bean
	public CacheManager cacheManager()
	{
		return new ConcurrentMapCacheManager();
	}
}
