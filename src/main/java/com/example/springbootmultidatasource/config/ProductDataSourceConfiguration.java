package com.example.springbootmultidatasource.config;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

// Declares this class as a Spring configuration class, providing bean definitions and setup
@Configuration
// Enables Spring's declarative transaction management for managing database interactions
@EnableTransactionManagement
// Enables Spring Data JPA repositories for accessing data
@EnableJpaRepositories(
        // Specifies the EntityManagerFactory bean to be used by these repositories
        entityManagerFactoryRef = "productEntityManagerFactory",transactionManagerRef = "productTransactionManager",
        // Specifies the base package where Spring should scan for repository interfaces
        basePackages = {"com.example.springbootmultidatasource.repository.Product"})
public class ProductDataSourceConfiguration {
    @Bean
    public EntityManagerFactoryBuilder productEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }
        // Declare this bean as primary if multiple data sources exist
        @Primary

        // Register a bean named "dataSource" managed by Spring
        @Bean(name = "productDataSource")

        // Bind configuration properties from the application's properties file
        @ConfigurationProperties(prefix = "spring.product.datasource")

        // Create and return a DataSource object
        public DataSource dataSource() {
            return DataSourceBuilder.create().build();  // Build the DataSource using configuration properties
        }

    @Primary
    @Bean(name = "productEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean productEntityManagerFactory(@Qualifier("productEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
                                                                              @Qualifier("productDataSource") DataSource productDataSource) {
        // Creating properties for configuring Hibernate
        HashMap<String, Object> properties = new HashMap<>();

        // Setting Hibernate property to update the database schema automatically
        properties.put("hibernate.hbm2ddl.auto", "update");

        // Setting Hibernate dialect to H2
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

        // Returning configured entity manager factory bean
        return builder.dataSource(productDataSource).properties(properties)
                .packages("com.example.springbootmultidatasource.entity.Product").persistenceUnit("Product").build();
    }
        // Configuring a transaction manager bean
        @Primary
        @Bean(name = "productTransactionManager")
        public PlatformTransactionManager productTransactionManager(
                @Qualifier("productEntityManagerFactory") EntityManagerFactory productEntityManagerFactory) {
            // Creating a JPA transaction manager with the provided entity manager factory
            return new JpaTransactionManager(productEntityManagerFactory);
        }
}
