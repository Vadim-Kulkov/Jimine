package com.jimine.jiminebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories
@EnableTransactionManagement
@SpringBootApplication
public class JimineBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JimineBackendApplication.class, args);
    }
}
