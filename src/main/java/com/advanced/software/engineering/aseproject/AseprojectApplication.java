package com.advanced.software.engineering.aseproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Context.IdentifyContext;

@SpringBootApplication
public class AseprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AseprojectApplication.class, args);

        IdentifyContext ctx = new IdentifyContext("/Users/amosneculau/Downloads/Contexts-170503");
        ctx.run();

    }

}
