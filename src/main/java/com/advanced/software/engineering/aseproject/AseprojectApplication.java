package com.advanced.software.engineering.aseproject;

import Events.IdentifyEvents;
import Index.InvertedIndex;
import cc.kave.commons.model.events.completionevents.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Utils.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
@SpringBootApplication
public class AseprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AseprojectApplication.class, args);

        //ContextHelper ctx = new ContextHelper(Configuration.CONTEXTS_DIR);
        //ctx.run();
        //IdentifyEvents ev = new IdentifyEvents();

//        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(Configuration.CONTEXTS_DIR, Configuration.EVENTS_DIR);
//        //recommenderInitialization.createIndex();
//        //recommenderInitialization.queryIndex();
//
//        Recommender recommender = new Recommender(new InvertedIndex(Configuration.INDEX_STORAGE));
//
//        for(Context ctx:recommenderInitialization.queryIndex())
//            recommender.query(ctx);

        IdentifyEvents e = new IdentifyEvents();


    }
}
