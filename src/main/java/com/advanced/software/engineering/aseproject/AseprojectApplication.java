package com.advanced.software.engineering.aseproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Utils.Configuration;

@SpringBootApplication
public class AseprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AseprojectApplication.class, args);

        //ContextHelper ctx = new ContextHelper(Configuration.CONTEXTS_DIR);
        //ctx.run();
        //IdentifyEvents ev = new IdentifyEvents();

        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(Configuration.CONTEXTS_DIR, Configuration.EVENTS_DIR);
        recommenderInitialization.createIndex();
        recommenderInitialization.queryIndex();
    }
}
