package com.belong.telecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class TelecomApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelecomApplication.class, args);
    }
}
