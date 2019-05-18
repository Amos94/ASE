package com.advanced.software.engineering.aseproject;

import Context.IoHelper;
import Index.InvertedIndex;
import Index.IInvertedIndex;
import Utils.Configuration;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecommenderInitialization {

    private String contextsPath;
    private String eventsPath;
    private Logger logger;

    public RecommenderInitialization(String contextsPath, String eventsPath){
        logger = Logger.getLogger(RecommenderInitialization.class.getName());

        logger.log(Level.INFO, "\nRecommenderInitialization initializing..." );

        this.contextsPath = contextsPath;
        this.eventsPath = eventsPath;

        logger.log(Level.INFO,
                "\nContexts and Events directories were set."
                        .concat("\nContexts: "+contextsPath)
                        .concat("\nEvents: "+eventsPath));
    }

    public void createIndex() {
        Set<String> zips = IoHelper.findAllZips(contextsPath);
        int numberOfZips = zips.size();
        long startTime = System.currentTimeMillis();
        IInvertedIndex invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);
        SSTProcessor sstProcessor = new SSTProcessor(invertedIndex);

        logger.log(Level.INFO, "\nStart to create the index out of the given contexts."
                .concat("\nFound " + numberOfZips + " zips in the context directory."));

        sstProcessor.startProcessSSTs();

        for (String zip : zips) {
            logger.log(Level.INFO, "\n Starting to create index for "+zip);
            //read data in the zip
            // open the .zip file ...
            try (IReadingArchive ra = new ReadingArchive(new File(contextsPath, zip))) {
                // ... and iterate over content.
                // the iteration will stop after 10 contexts to speed things up in the example.
                while (ra.hasNext()) {
                    /*
                     * within the slnZip, each stored context is contained as a single file that
                     * contains the Json representation of a {@see Context}.
                     */
                    Context ctx = ra.getNext(Context.class);

                    sstProcessor.processSST(ctx);
                }
            }
            logger.log(Level.INFO, "\n Finishing to create index for "+zip);
        }

        sstProcessor.finishProcessSSTs();

        long endTime = System.currentTimeMillis();

        long timeElapsed = (endTime - startTime)/1000;
        logger.log(Level.INFO, "\nThe index was created in "+ timeElapsed + " seconds.");
    }

}