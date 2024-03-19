# Using Multiple Data Sources in Spring Boot

In this project, I'll explain how to utilize different databases within the same Spring Boot application. This becomes necessary when an application requires accessing data from multiple databases, such as integrating with legacy systems, managing data from different microservices, or handling data partitioning for scalability.
## Table of Contents
1. [Installation](#installation)
2. [Configuration](#configuration)
3. [Usage](#usage)
4. [Contributing](#contributing)
5. [License](#license)
6. [Contact Information](#Contact-Information)
## Installation
To run this project, ensure you have Java 17 and Maven installed.

Clone or fork the project and run the following command in the terminal:
`mvn clean install` or `mvnw clean install`
If you're using IntelliJ IDEA, you can also run it from the interface by navigating to the Maven menu in the right sidebar.
## Configuration

To configure the project, we'll use two entities for simplicity: User and Product, each with its own JPA repository. We'll illustrate the configuration process using the User entity as an example.

First, configure the application.properties file to establish a connection with the database:
``` 
spring.h2.console.enabled=true

spring.user.datasource.jdbc-url=jdbc:h2:mem:user
spring.user.datasource.username=sa
spring.user.datasource.password=password

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```
Next, create a configuration file for each entity. Annotate each configuration file with @Configuration to enable Spring's declarative transaction management and Spring Data JPA repositories:

```
@EnableJpaRepositories(
    entityManagerFactoryRef = "userEntityManagerFactory",
    transactionManagerRef = "userTransactionManager",
    basePackages = {"com.example.springbootmultidatasource.repository.User"}
)
```
Then, declare a DataSource bean for each entity:
```
@Primary
@Bean(name = "userDataSource")
@ConfigurationProperties(prefix = "spring.user.datasource")
public DataSource dataSource() {
    return DataSourceBuilder.create().build();
}
```
Create the userEntityManagerFactory bean to configure the entity manager and specify the package containing the entity:
```
@Primary
@Bean(name = "userEntityManagerFactory")
public LocalContainerEntityManagerFactoryBean entityManagerFactory(
    @Qualifier("userEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder,
    @Qualifier("userDataSource") DataSource dataSource) {

    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", "update");
    properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");

    return builder.dataSource(dataSource)
                  .properties(properties)
                  .packages("com.example.springbootmultidatasource.entity.User")
                  .persistenceUnit("User")
                  .build();
}
```
Finally, create the userTransactionManager bean:
```
@Primary
@Bean(name = "userTransactionManager")
public PlatformTransactionManager transactionManager(
    @Qualifier("userEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
    
    return new JpaTransactionManager(entityManagerFactory);
}
```
With this configuration, the database is set up, and you're ready to start working with it.
## Usage

To keep things simple, I've created a Thymeleaf template for testing. The project also prints the name of each table based on the method used.
## Troubleshooting

If you encounter a null pointer error related to EntityManagerFactoryBuilder, ensure you've created this bean. Though some people may not declare it, it might be necessary, especially with newer Java versions.

Also, remember to add the necessary annotations to your classes (@Entity, @Repository, @Service, @Controller, @Configuration). For entities, don't forget @Data, and use @Autowired when one class calls another. Ensure consistency between property names in your code and the application.properties file for successful connection.
## Contributing

The main goal of this repository is to provide clear guidance without delving too deeply into the details. If you find any issues or have additional information to contribute, feel free to create an issue or fork the project and create you own branch. Submit a pull request, and I'll gladly review it.
## License

This project is licensed under the MIT License.
## Contact Information

For any inquiries or feedback, you can contact me via email at antarwael@ieee.org or connect with me on [Linkedin](https://www.linkedin.com/in/wael-antar/).
