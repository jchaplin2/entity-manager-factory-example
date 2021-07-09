package com.example.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceSchemaCreatedEvent;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@ContextConfiguration()
@EnableJpaRepositories(basePackages = { "com.example.jpa" })
@EnableTransactionManagement
public class H2JpaConfig {

    @Autowired
    private Environment env;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource ds) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ds);
        em.setPackagesToScan(new String[] { "com.example.jpa" });
        em.setJpaVendorAdapter(jpaVendorAdapter());
        em.setJpaProperties(additionalProperties());

        this.applicationContext.publishEvent(new DataSourceSchemaCreatedEvent(ds));

        return em;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();

        //tried create too.
        hibernateProperties.setProperty(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create-drop");
//      hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        hibernateProperties.setProperty(org.hibernate.cfg.Environment.HBM2DDL_IMPORT_FILES, "resources/data-it.sql");
        hibernateProperties.setProperty("javax.persistence.schema-generation.scripts.create-target", "resources/schema-it.sql");
        hibernateProperties.setProperty("javax.persistence.schema-generation.create-database-schemas", "true");
        hibernateProperties.setProperty("javax.persistence.schema-generation.scripts.action", "create");

        return hibernateProperties;
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabasePlatform(env.getRequiredProperty("spring.jpa.database-platform"));
        return adapter;
    }

}
