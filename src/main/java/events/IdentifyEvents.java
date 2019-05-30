package events;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Configuration;
import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.utils.io.ReadingArchive;


/**
 * This class contains several code examples that explain how to read enriched
 * event streams with the CARET platform. It cannot be run, the code snippets
 * serve as documentation.
 */
public class IdentifyEvents {

    /**
     * this variable should point to a folder that contains a bunch of .zip
     * files that may be nested in subfolders. If you have downloaded the event
     * dataset from our website, please unzip the archive and point to the
     * containing folder here.
     */
    private List<Context> aggregatedContexts;

    private static final Logger LOGGER = Logger.getLogger( IdentifyEvents.class.getName() );
    private static final Boolean muteLogger = true;


    /**
     * Identify all events
     */
    public IdentifyEvents(){
        aggregatedContexts = readAllEvents(Configuration.EVENTS_DIR);
    }

    /**
     * 1: Find all users in the dataset.
     *
     * @param eventsDir
     * @return all users in the dataset
     */
    public static List<String> findAllUsers(String eventsDir) {
        // This step is straight forward, as events are grouped by user. Each
        // .zip file in the dataset corresponds to one user.
        int maxEv = Configuration.getRecommendationZips();
        List<String> zips = Lists.newLinkedList();

        if(maxEv == -1){
            for (File f : FileUtils.listFiles(new File(eventsDir), new String[] { "zip" }, true)) {
                zips.add(f.getAbsolutePath());
                if(!muteLogger) {
                    LOGGER.log(Level.INFO, f.getName()+" user folder added");
                }
            }
        }else{
            for (File f : FileUtils.listFiles(new File(eventsDir), new String[] { "zip" }, true)) {
                zips.add(f.getAbsolutePath());

                --maxEv;
                if(maxEv == 0)
                    break;
                if(!muteLogger) {
                    LOGGER.log(Level.INFO, f.getName() + " user folder added. Left to add: " + maxEv);
                }
            }
        }

        return zips;
    }

    /**
     * 2: Reading events
     *
     * @param eventsDir
     * @return aggregated Contexts
     */
    public static List<Context> readAllEvents(String eventsDir) {
        // each .zip file corresponds to a user
        List<String> userZips = findAllUsers(eventsDir);
        List<Context> aggregatedContexts = new LinkedList<>();

        for (String user : userZips) {
            // you can use our helper to open a file...
            try (ReadingArchive ra = new ReadingArchive(new File(user))) {
                // ...iterate over it...
                while (ra.hasNext()) {
                    // ... and desrialize the IDE event.
                    IDEEvent e = ra.getNext(IDEEvent.class);
                    // afterwards, you can process it as a Java object
                    if(!muteLogger) {
                        LOGGER.log(Level.INFO, "Processing events for: " + user.toString());
                    }
                    aggregatedContexts.addAll(process(e));

                }
            }
        }
        return aggregatedContexts;
    }

    /**
     * 4: Processing events
     *
     * @param event
     * @return contexts
     */
    public static List<Context> process(IDEEvent event) {
        // once you have access to the instantiated event you can dispatch the
        // type. As the events are not nested, we did not implement the visitor
        // pattern, but resorted to instanceof checks.
        List<Context> contexts = new LinkedList<>();

        if (event instanceof CompletionEvent) {
            // if the correct type is identified, you can cast it...
            CompletionEvent ce = (CompletionEvent) event;
            //ce.ActiveDocument.getFileName();
            contexts.add(ce.getContext());
            if(!muteLogger) {
                LOGGER.log(Level.INFO, "Event "+ event.getClass().getName() + " added");
            }
        }

        return contexts;
    }

    /**
     * Get aggregated contexts
     *
     * @return aggregated contexts
     */
    public List<Context> getAggregatedContexts(){
        return aggregatedContexts;
    }

    /**
     * Size of aggregated contexts
     *
     * @return size of aggregated contexts
     */
    public long getAggregatedContextsSize(){
        return aggregatedContexts.size();
    }
}