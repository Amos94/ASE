package com.advanced.software.engineering.aseproject;

import Events.IdentifyEvents;
import Index.InvertedIndex;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Utils.Configuration;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class AseprojectApplication {

    /**
     * Main class for starting the application which boots the recommenderInitalization and creates the index
     * Caution: Creating the index can take a while (about 1h)
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(AseprojectApplication.class, args);
        Logger logger = Logger.getLogger(AseprojectApplication.class.getName());

        // Initalize the recommender
        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(Configuration.CONTEXTS_DIR, Configuration.EVENTS_DIR);
        Recommender recommender = new Recommender(new InvertedIndex(Configuration.INDEX_STORAGE));

        // If you want to reindex the database, this is where the time flies away
        if(Configuration.REINDEX_DATABASE == true) {
            recommenderInitialization.createIndex();
        }

        if(Configuration.EVALUATION == true) {
            // New EventIndentifier
            IdentifyEvents e = new IdentifyEvents();

            // Aggregate through all events (Currently only jaccard)
            for(Context ctx:e.getAggregatedContexts()) {
                logger.log(Level.INFO, "\nCreating recommendations for "+e.getAggregatedContextsSize()+" methods");
                recommender.query(ctx);
            }
        }

        System.out.println("The program has ended gracefully - thanks for using :) ");

    }
}
