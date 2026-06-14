package com.gestra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point aplikasi backend Gestra.
 * Menjalankan Spring Boot REST API pada port 8080.
 */
@SpringBootApplication
public class GestraBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestraBackendApplication.class, args);
    }
}
