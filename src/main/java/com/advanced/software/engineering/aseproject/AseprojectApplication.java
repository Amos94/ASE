package com.advanced.software.engineering.aseproject;

import Events.IdentifyEvents;
import Events.IdentifyTestContexts;
import Index.InvertedIndex;
import cc.kave.commons.model.events.completionevents.Context;
//import cc.kave.commons.model.ssts.IStatement;
//import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
//import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
//import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
//import cc.kave.commons.model.ssts.statements.IAssignment;
//import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Utils.Configuration;

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

        if(args.length != 0) {
            try {
                Configuration.REMOVE_STOP_WORDS = Boolean.parseBoolean(args[0]);
                Configuration.REINDEX_DATABASE = Boolean.parseBoolean(args[1]);
                Configuration.EVALUATION = Boolean.parseBoolean(args[2]);

                if (args.length > 3) {
                    Configuration.LAST_N_CONSIDERED_STATEMENTS = Integer.parseInt(args[3]);
                    Configuration.RECOMMENDATION_ZIPS = Integer.parseInt(args[4]);
                    System.out.println("Non default configuration is used, 5 parameters expected");
                    System.out.println("REMOVE_STOP_WORDS set to " + Configuration.REMOVE_STOP_WORDS + " REINDEX_DATABASE set to " + Configuration.REINDEX_DATABASE + " EVALUATION set to " + Configuration.EVALUATION);
                    System.out.println("LAST_N_CONSIDERED_STATEMENTS: " + Configuration.LAST_N_CONSIDERED_STATEMENTS + " MAX_EVENTS_CONSIDERED: " + Configuration.RECOMMENDATION_ZIPS);
                } else {
                    System.out.println("Non default configuration is used, 3 parameters expected:");
                    System.out.println("REMOVE_STOP_WORDS set to " + Configuration.REMOVE_STOP_WORDS + " REINDEX_DATABASE set to " + Configuration.REINDEX_DATABASE + " EVALUATION set to " + Configuration.EVALUATION);
                }
                System.out.println(Configuration.DELIMITER);

            }
            catch (ArrayIndexOutOfBoundsException e){
                System.out.println("ArrayIndexOutOfBoundsException caught");
            }
        }



        // Initalize the recommender
        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(Configuration.CONTEXTS_DIR, Configuration.EVENTS_DIR);
        //Recommender recommender = new Recommender(new InvertedIndex(Configuration.INDEX_STORAGE));

        // If you want to reindex the database, this is where the time flies away
        if(Configuration.REINDEX_DATABASE) {
            recommenderInitialization.createIndex();
        }

        if(Configuration.EVALUATION && Configuration.USE_EVENTS) {
            // New EventIndentifier
            IdentifyEvents e = new IdentifyEvents();
            Recommender recommender = new Recommender(new InvertedIndex(Configuration.INDEX_STORAGE));

            // Aggregate through all events (Currently only jaccard)
            for(Context ctx:e.getAggregatedContexts()) {
                logger.log(Level.INFO, "\nCreating recommendations for "+e.getAggregatedContextsSize()+" methods");
                recommender.query(ctx);
            }

            recommendationRate = (double)(recommender.getNumberOfCorrectRecommendations()/e.getAggregatedContextsSize());
            logger.log(Level.INFO, "The recommendation rate is: "+recommendationRate);
        }

        if(Configuration.EVALUATION && Configuration.USE_TEST_CONTEXTS) {
            // New ContextIdentifier
            IdentifyTestContexts tc = new IdentifyTestContexts();

            int noMethodsToMakeRecomenationsFor = 0;
            int numberOfCorrectRecommendations = 0;
            // Aggregate through all events (Currently only jaccard)
            for(Context ctx:tc.getAggregatedContexts()){

                String projectName = tc.getProjectName();
                Recommender recommender = new Recommender(new InvertedIndex(Configuration.INDEX_STORAGE), projectName);

                logger.log(Level.INFO, "\n"+Configuration.DELIMITER+"\nRecommendations within project: "+projectName +"\n"+Configuration.DELIMITER);
                recommender.query(ctx);

                noMethodsToMakeRecomenationsFor += recommender.getNumberMethodCalls();
                numberOfCorrectRecommendations += recommender.getNumberOfCorrectRecommendations();

            }
//            System.out.println(noMethodsToMakeRecomenationsFor);
//            System.out.println(numberOfCorrectRecommendations);

            recommendationRate = (double)((numberOfCorrectRecommendations*100)/(noMethodsToMakeRecomenationsFor));
            //System.out.println(numberOfCorrectRecommendations);
            logger.log(Level.INFO, "The recommendation rate is: "+recommendationRate+"%");
        }

        System.out.println("The program has ended gracefully - thanks for using :) ");
        System.out.println(Configuration.DELIMITER);
    }
//    public static int getNoOfInvocations(Context ctx) {
//        int noMethodsToMaleRecomenationsFor = 0;
//
//        for (IMethodDeclaration method : ctx.getSST().getMethods()) {
//            for (IStatement statement : method.getBody()) {
//                if (statement instanceof IExpressionStatement || statement instanceof IAssignment) {
//                    IAssignableExpression expression;
//                    if (statement instanceof IExpressionStatement) {
//                        expression = ((IExpressionStatement) statement).getExpression();
//                    } else {
//                        expression = ((IAssignment) statement).getExpression();
//                    }
//                    if (expression instanceof IInvocationExpression) {
//                        noMethodsToMaleRecomenationsFor++;
//                    }
//                }
//            }
//
//        }
//        return noMethodsToMaleRecomenationsFor;
//    }
}
