package org.example.databasework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DatabaseWorkApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatabaseWorkApplication.class, args);
    }

}
