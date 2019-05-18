package com.advanced.software.engineering.aseproject;

import Events.IdentifyEvents;
import Index.InvertedIndex;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import org.apache.commons.lang3.tuple.Pair;
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
//        recommenderInitialization.createIndex();
        //recommenderInitialization.queryIndex();
//
        Recommender recommender = new Recommender(new InvertedIndex(Configuration.INDEX_STORAGE));
//
//        for(Context ctx:recommenderInitialization.queryIndex())
//            recommender.query(ctx);

        IdentifyEvents e = new IdentifyEvents();

        for(Context ctx:e.getAggregatedContexts()) {
            Set<Pair<IMemberName, Double>> output = recommender.query(ctx);
            for(Pair<IMemberName, Double> mr : output) {
                //System.out.println(mr);
                System.out.println("Recommended: " +mr.getLeft().getFullName() + ". Jaccard Similarity measure: "+ mr.getRight() + "\nIdentifier: " + mr.getLeft().getIdentifier());
            }
        }


    }
}
