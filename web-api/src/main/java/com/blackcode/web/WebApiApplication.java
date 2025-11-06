package com.blackcode.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.blackcode")
@EnableJpaRepositories(basePackages = "com.blackcode")
@EntityScan(basePackages = "com.blackcode")
public class WebApiApplication {
    public static void main( String[] args ){
        SpringApplication.run(WebApiApplication.class, args);
    }
}
