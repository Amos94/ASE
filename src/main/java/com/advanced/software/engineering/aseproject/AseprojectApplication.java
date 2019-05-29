/**
 * Copyright 2019 Universität Zürich
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.advanced.software.engineering.aseproject;

import Events.IdentifyEvents;
import Events.IdentifyTestContexts;
import Index.InvertedIndex;
import Utils.Configuration;
import cc.kave.commons.model.events.completionevents.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Level;
import java.util.logging.Logger;


@SpringBootApplication
public class AseprojectApplication {

    /**
     * Main class for starting the application which boots the recommenderInitalization and creates the index
     * Caution: Creating the index can take a while (about 1h)
     *
     * @param args - args
     */
    public static void main(String[] args) {
        double recommendationRate;
        SpringApplication.run(AseprojectApplication.class, args);
        Logger logger = Logger.getLogger(AseprojectApplication.class.getName());

        if (args.length != 0) {
            try {
                Configuration.setRemoveStopWords(Boolean.parseBoolean(args[0]));
                Configuration.setReindexDatabase(Boolean.parseBoolean(args[1]));
                Configuration.setEVALUATION(Boolean.parseBoolean(args[2]));

                if (args.length > 3) {
                    Configuration.setLastNConsideredStatements(Integer.parseInt(args[3]));
                    Configuration.setRecommendationZips(Integer.parseInt(args[4]));
                    System.out.println("Non default configuration is used, 5 parameters expected");
                    System.out.println("REMOVE_STOP_WORDS set to " + Configuration.getRemoveStopWords() + " REINDEX_DATABASE set to " + Configuration.getReindexDatabase() + " EVALUATION set to " + Configuration.getEVALUATION());
                    System.out.println("LAST_N_CONSIDERED_STATEMENTS: " + Configuration.getLastNConsideredStatements() + " MAX_EVENTS_CONSIDERED: " + Configuration.getRecommendationZips());
                } else {
                    System.out.println("Non default configuration is used, 3 parameters expected:");
                    System.out.println("REMOVE_STOP_WORDS set to " + Configuration.getRemoveStopWords() + " REINDEX_DATABASE set to " + Configuration.getReindexDatabase() + " EVALUATION set to " + Configuration.getEVALUATION());
                }
                System.out.println(Configuration.getDELIMITER());

            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("ArrayIndexOutOfBoundsException caught");
            }
        }


        // Initalize the recommender
        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(Configuration.CONTEXTS_DIR);
        //Recommender recommender = new Recommender(new InvertedIndex(Configuration.INDEX_STORAGE));

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
                logger.log(Level.INFO, "\nCreating recommendations for " + e.getAggregatedContextsSize() + " methods");
                recommender.query(ctx);
            }

            recommendationRate = ((double)recommender.getNumberOfCorrectRecommendations() / e.getAggregatedContextsSize());
            logger.log(Level.INFO, "The recommendation rate is: " + recommendationRate);
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

                logger.log(Level.INFO, "\n" + Configuration.getDELIMITER() + "\nRecommendations within project: " + projectName + "\n" + Configuration.getDELIMITER());
                recommender.query(ctx);

                noMethodsToMakeRecomenationsFor += recommender.getNumberMethodCalls();
                numberOfCorrectRecommendations += recommender.getNumberOfCorrectRecommendations();

            }

            // Calculate final percentage
            if (noMethodsToMakeRecomenationsFor == 0) {
                logger.log(Level.WARNING, "No Methods to make recommendations for!");
            } else {
                recommendationRate = (((double) numberOfCorrectRecommendations * 100) / (noMethodsToMakeRecomenationsFor));
                logger.log(Level.INFO, "The recommendation rate is: " + recommendationRate + "%");
            }

        }

        System.out.println("The program has ended gracefully - thanks for using :) ");
        System.out.println(Configuration.getDELIMITER());
    }
}
