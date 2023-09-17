package com.example.lab4_cc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class Lab4CcApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab4CcApplication.class, args);
    }

}
