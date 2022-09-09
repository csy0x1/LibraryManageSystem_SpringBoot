package com.example.vuetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
public class VuetestApplication {

    public static void main(String[] args) {
        SpringApplication.run(VuetestApplication.class, args);
    }

}
