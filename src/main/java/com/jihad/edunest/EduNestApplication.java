package com.jihad.edunest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jihad.edunest", "com.jihad.edunest.web.vms.mapper"})
public class EduNestApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduNestApplication.class, args);
    }

}
