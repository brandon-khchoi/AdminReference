package com.example.adminreference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@SpringBootApplication
public class AdminReferenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminReferenceApplication.class, args);
    }

}
