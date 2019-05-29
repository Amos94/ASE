package com.advanced.software.engineering.aseproject;

import Context.ContextHelper;
import Context.IoHelper;
import Index.InvertedIndex;
import Index.IInvertedIndex;
import Utils.Configuration;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

import java.io.File;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

class RecommenderInitialization {

    private String contextsPath;
    private static final Logger LOGGER = Logger.getLogger( IoHelper.class.getName() );

    RecommenderInitialization(String contextsPath){

        // process start
        LOGGER.log(Level.INFO, "\nRecommenderInitialization initializing..." );

        // set context and events path
        this.contextsPath = contextsPath;

        // log the set paths
        LOGGER.log(Level.INFO,
                "\nContexts and Events directories were set."
                        .concat("\nContexts: "+contextsPath));
    }

    void createIndex() {
        // finds all given zips by context path
        Set<String> zips = IoHelper.findAllZips(contextsPath);
        int numberOfZips = zips.size();

        // instantiates a new contextHelper
        ContextHelper ctxHelper = new ContextHelper(contextsPath);

        // persist start time
        long startTime = System.currentTimeMillis();

        // create inverted index
        IInvertedIndex invertedIndex = new InvertedIndex(Configuration.INDEX_STORAGE);

        SSTProcessor sstProcessor = new SSTProcessor(invertedIndex);

        // log info about the start of the process
        LOGGER.log(Level.INFO, "\nStart to create the index out of the given contexts."
                .concat("\nFound " + numberOfZips + " zips in the context directory."));

      // start the SSTs process
        sstProcessor.startProcessSSTs();

        for (String zip : zips) {
            LOGGER.log(Level.INFO, "\n Starting to create index for "+zip);
            //read data in the zip
            // open the .zip file ...
            try (IReadingArchive ra = new ReadingArchive(new File(contextsPath, zip))) {
                // ... and iterate over content.
                // the iteration will stop after 10 contexts to speed things up in the example.
                while (ra.hasNext()) {

                    //Project name for index
                    String[] zipName = zip.split("/");
                    int pnSize = zipName.length;
                    String projectName = zipName[pnSize-1].replace(".zip","");

                    /*
                     * within the slnZip, each stored context is contained as a single file that
                     * contains the Json representation of a {@see Context}.
                     */

                    Context ctx = ra.getNext(Context.class);
                    sstProcessor.processSST(ctx, projectName);
                }
            }
            LOGGER.log(Level.INFO, "\n Finishing to create index for "+zip);
        }

        sstProcessor.finishProcessSSTs();

        long endTime = System.currentTimeMillis();

        long timeElapsed = (endTime - startTime)/1000;
        LOGGER.log(Level.INFO, "\nThe index was created in "+ timeElapsed + " seconds.");
    }
}