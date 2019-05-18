package com.advanced.software.engineering.aseproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Utils.Configuration;

@SpringBootApplication
public class AseprojectApplication {

    /**
     * Main class for starting the application which boots the recommenderInitalization and creates the index
     * Caution: Creating the index can take a while
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(AseprojectApplication.class, args);

        //ContextHelper ctx = new ContextHelper(Configuration.CONTEXTS_DIR);
        //ctx.run();
        //IdentifyEvents ev = new IdentifyEvents();

        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(Configuration.CONTEXTS_DIR, Configuration.EVENTS_DIR);
        if(Configuration.REINDEX_DATABASE == true) {
            recommenderInitialization.createIndex();
        }

        recommenderInitialization.queryIndex();
    }
}
