package com.advanced.software.engineering.aseproject;

import events.IdentifyEvents;
import events.IdentifyTestContexts;
import index.InvertedIndex;
import utils.Configuration;
import cc.kave.commons.model.events.completionevents.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Level;
import java.util.logging.Logger;


@SpringBootApplication
public class AseprojectApplication {

    private static final Logger LOGGER = Logger.getLogger( AseprojectApplication.class.getName() );


    /**
     * Main class for starting the application which boots the recommenderInitalization and creates the index
     * Caution: Creating the index can take a while (about 1h)
     *
     * @param args - args
     */
    public static void main(String[] args) {
        double recommendationRate;
        SpringApplication.run(AseprojectApplication.class, args);

        if (args.length != 0) {
            try {
                Configuration.setRemoveStopWords(Boolean.parseBoolean(args[0]));
                Configuration.setReindexDatabase(Boolean.parseBoolean(args[1]));
                Configuration.setEVALUATION(Boolean.parseBoolean(args[2]));

                if (args.length > 3) {
                    Configuration.setLastNConsideredStatements(Integer.parseInt(args[3]));
                    Configuration.setRecommendationZips(Integer.parseInt(args[4]));
                    LOGGER.log(Level.INFO, "Non default configuration is used, 5 parameters expected");
                    LOGGER.log(Level.INFO, "REMOVE_STOP_WORDS set to " + Configuration.getRemoveStopWords() + " REINDEX_DATABASE set to " + Configuration.getReindexDatabase() + " EVALUATION set to " + Configuration.getEVALUATION());
                    LOGGER.log(Level.INFO, "LAST_N_CONSIDERED_STATEMENTS: " + Configuration.getLastNConsideredStatements() + " MAX_EVENTS_CONSIDERED: " + Configuration.getRecommendationZips());
                } else {
                    LOGGER.log(Level.INFO, "Non default configuration is used, 3 parameters expected:");
                    LOGGER.log(Level.INFO, "REMOVE_STOP_WORDS set to " + Configuration.getRemoveStopWords() + " REINDEX_DATABASE set to " + Configuration.getReindexDatabase() + " EVALUATION set to " + Configuration.getEVALUATION());
                }
                LOGGER.log(Level.INFO, Configuration.getDELIMITER());

            } catch (ArrayIndexOutOfBoundsException e) {
                LOGGER.log(Level.INFO, "ArrayIndexOutOfBoundsException caught");
            }
        }


        // Initalize the recommender
        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(Configuration.CONTEXTS_DIR);

        // If you want to reindex the database, this is where the time flies away
        if (Configuration.getReindexDatabase()) {
            recommenderInitialization.createIndex();
        }

        if (Configuration.getEVALUATION() && Configuration.getUseEvents()) {
            // New EventIndentifier
            IdentifyEvents e = new IdentifyEvents();
            Recommender recommender = new Recommender(new InvertedIndex(Configuration.INDEX_STORAGE));

            // Aggregate through all events (Currently only jaccard)
            for (Context ctx : e.getAggregatedContexts()) {
                LOGGER.log(Level.INFO, "\nCreating recommendations for " + e.getAggregatedContextsSize() + " methods");
                recommender.query(ctx);
            }

            recommendationRate = ((double)recommender.getNumberOfCorrectRecommendations() / e.getAggregatedContextsSize());
            LOGGER.log(Level.INFO, "The recommendation rate is: " + recommendationRate);
        }

        if (Configuration.getEVALUATION() && Configuration.getUseTestContexts()) {
            // New ContextIdentifier
            IdentifyTestContexts tc = new IdentifyTestContexts();

            int noMethodsToMakeRecomenationsFor = 0;
            int numberOfCorrectRecommendations = 0;
            // Aggregate through all events (Currently only jaccard)
            for (Context ctx : tc.getAggregatedContexts()) {

                String projectName = tc.getProjectName();
                Recommender recommender = new Recommender(new InvertedIndex(Configuration.INDEX_STORAGE), projectName);

                LOGGER.log(Level.INFO, "\n" + Configuration.getDELIMITER() + "\nRecommendations within project: " + projectName + "\n" + Configuration.getDELIMITER());
                recommender.query(ctx);

                noMethodsToMakeRecomenationsFor += recommender.getNumberMethodCalls();
                numberOfCorrectRecommendations += recommender.getNumberOfCorrectRecommendations();

            }

            // Calculate final percentage
            if (noMethodsToMakeRecomenationsFor == 0) {
                LOGGER.log(Level.WARNING, "No Methods to make recommendations for!");
            } else {
                recommendationRate = (((double) numberOfCorrectRecommendations * 100) / (noMethodsToMakeRecomenationsFor));
                LOGGER.log(Level.INFO, "The recommendation rate is: " + recommendationRate + "%");
            }

        }

        LOGGER.log(Level.INFO, "The program has ended gracefully - thanks for using :) ");
        LOGGER.log(Level.INFO, Configuration.getDELIMITER());
    }
}
