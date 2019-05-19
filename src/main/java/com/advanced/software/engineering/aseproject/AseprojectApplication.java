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

        if(args.length != 0) {
            try {
                Configuration.REMOVE_STOP_WORDS = Boolean.parseBoolean(args[0]);
                Configuration.REINDEX_DATABASE = Boolean.parseBoolean(args[1]);
                Configuration.EVALUATION = Boolean.parseBoolean(args[2]);

                if (args.length > 3) {
                    Configuration.LAST_N_CONSIDERED_STATEMENTS = Integer.parseInt(args[3]);
                    Configuration.MAX_EVENTS_CONSIDERED = Integer.parseInt(args[4]);
                    System.out.println("Non default configuration is used, 5 parameters expected");
                    System.out.println("REMOVE_STOP_WORDS set to " + Configuration.REMOVE_STOP_WORDS + " REINDEX_DATABASE set to " + Configuration.REINDEX_DATABASE + " EVALUATION set to " + Configuration.EVALUATION);
                    System.out.println("LAST_N_CONSIDERED_STATEMENTS: " + Configuration.LAST_N_CONSIDERED_STATEMENTS + " MAX_EVENTS_CONSIDERED: " + Configuration.MAX_EVENTS_CONSIDERED);
                } else {
                    System.out.println("Non default configuration is used, 3 parameters expected:");
                    System.out.println("REMOVE_STOP_WORDS set to " + Configuration.REMOVE_STOP_WORDS + " REINDEX_DATABASE set to " + Configuration.REINDEX_DATABASE + " EVALUATION set to " + Configuration.EVALUATION);
                }
                System.out.println("*********************************************************");

            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("ArrayIndexOutOfBoundsException caught");
            }
        }



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
                Set<Pair<IMemberName, Double>> output = recommender.query(ctx);
                for(Pair<IMemberName, Double> mr : output) {
                    System.out.println("Recommended: " +mr.getLeft().getFullName() + ". Jaccard Similarity measure: "+ mr.getRight() + "\nIdentifier: " + mr.getLeft().getIdentifier());
                }
            }
        }

        System.out.println("The program has ended gracefully - thanks for using :) ");
        System.out.println("*********************************************************");
    }
}
