package com.example.springbootmultidatasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class SpringBootMultiDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMultiDatasourceApplication.class, args);
    }

}
