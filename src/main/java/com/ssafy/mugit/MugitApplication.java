package com.ssafy.mugit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MugitApplication {

    public static void main(String[] args) {
        SpringApplication.run(MugitApplication.class, args);
    }

}
