package com.homework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.homework.config.DBWrapper;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages={"com.homework.model.repositories"})
@ComponentScan(basePackages ={ "com.homework", "com.homework.services" }, excludeFilters = { 
		@Filter(type = FilterType.ANNOTATION, value = Configuration.class) })
@PropertySource({"classpath:application.properties"})
public class DBConfig {
	
	@Autowired
	private Environment env;
	
	/**
	 * Инициализация pool'a на основе apache dbcp
	 * */
	@Bean(name = "dataSource")
	public DataSource dataSource() {
	   
	    BasicDataSource ds = new BasicDataSource();
	    ds.setDriverClassName(env.getProperty("jdbc.driverClassName"));
	    ds.setUrl(env.getProperty("jdbc.url"));
	    ds.setUsername(env.getProperty("jdbc.username"));
	    ds.setPassword(env.getProperty("jdbc.password"));
	    
	    ds.setInitialSize(Integer.parseInt(env.getProperty("dbcp.initialSize")));
	    ds.setMaxActive(Integer.parseInt(env.getProperty("dbcp.maxActive")));
	    ds.setMaxWait(Integer.parseInt(env.getProperty("dbcp.maxWait")));
	    ds.setDefaultAutoCommit(false);
	    
	    return ds;
	}
	
	/***
	 * Менеджер транзакций
	 * */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager()
    {       
    	JpaTransactionManager jtm = new JpaTransactionManager();
    	
        jtm.setEntityManagerFactory(entityManagerFactory().getObject());
        jtm.setDataSource(dataSource());
                
        return jtm;
    }
 
	/***
	 * Менеджер сущностей
	 * */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
 
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(Boolean.FALSE);
        vendorAdapter.setShowSql(Boolean.TRUE);
 
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(new String[] { "com.homework.model.entities" });
        
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "none");
        jpaProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));        
     
        //jpaProperties.put("hibernate.generate_statistics", "true");
        
        factory.setJpaProperties(jpaProperties);
 		
        factory.afterPropertiesSet();
        factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        return factory;
    }
 
    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator()
    {
        return new HibernateExceptionTranslator();
    }
	
    @Bean(name = "jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
    
    
    @Bean(name = "DB")
    public FDBWrapper DB() {
    	
    	return new FDBWrapper(dataSource());    	
    }
    
    /**
     * Данные класс нужен для передачи менеджера сущностей в обертку, чтобы можно было использовать 
     * hibernate запросы JPA или native в "классическом" виде
     * */
    public class FDBWrapper extends DBWrapper {
    	
    	@PersistenceContext(unitName = "entityManagerFactory")    	  
		public void setEntityManager(EntityManager entityManager) {
    		this.entityManager = entityManager;
		}

		public FDBWrapper(DataSource ds) {
			super(ds);			
		}
    	
    }
	
}
