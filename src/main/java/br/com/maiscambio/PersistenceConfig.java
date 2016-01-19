package br.com.maiscambio;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.com.maiscambio.model.repository.custom.CustomRepositoryFactoryBean;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "br.com.maiscambio.model.repository", repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
public class PersistenceConfig
{
	@Autowired
	private Environment environment;
	
	@Value("${init-db:false}")
	private String initDatabase;
	
	@Bean
	public PlatformTransactionManager transactionManager()
	{
		return new JpaTransactionManager(entityManagerFactory().getObject());
	}
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	{
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setGenerateDdl(Boolean.parseBoolean(environment.getProperty("hibernate.generate_ddl")));
		hibernateJpaVendorAdapter.setShowSql(Boolean.parseBoolean(environment.getProperty("hibernate.show_sql")));
		
		Properties properties = new Properties();
		properties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));
		properties.put("hibernate.use_sql_comments", environment.getProperty("hibernate.use_sql_comments"));
		
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean.setDataSource(dataSource());
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
		localContainerEntityManagerFactoryBean.setPackagesToScan("br.com.maiscambio.model.entity");
		localContainerEntityManagerFactoryBean.setJpaProperties(properties);
		localContainerEntityManagerFactoryBean.afterPropertiesSet();
		localContainerEntityManagerFactoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		
		return localContainerEntityManagerFactoryBean;
	}
	
	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator()
	{
		return new HibernateExceptionTranslator();
	}
	
	@Bean
	public DataSource dataSource()
	{
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
		basicDataSource.setUrl(environment.getProperty("jdbc.url"));
		basicDataSource.setUsername(environment.getProperty("jdbc.username"));
		basicDataSource.setPassword(environment.getProperty("jdbc.password"));
		basicDataSource.setTestOnBorrow(true);
		basicDataSource.setValidationQuery("SELECT 1");
		
		return basicDataSource;
	}
	
	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource)
	{
		ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
		resourceDatabasePopulator.addScript(new ClassPathResource("db.sql"));
		
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
		dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));
		
		return dataSourceInitializer;
	}
}