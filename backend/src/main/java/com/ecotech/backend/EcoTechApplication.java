package com.ecotech.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcoTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoTechApplication.class, args);
    }
}
