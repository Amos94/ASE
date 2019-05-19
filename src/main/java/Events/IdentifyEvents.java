/**
 * Copyright 2016 Technische Universität Darmstadt
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
package Events;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import Utils.Configuration;
import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import com.advanced.software.engineering.aseproject.RecommenderInitialization;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.utils.io.ReadingArchive;
import cc.kave.commons.utils.io.json.JsonUtils;

import static Utils.Configuration.MAX_EVENTS_CONSIDERED;

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
    private static Logger logger;

    public IdentifyEvents(){
        logger = Logger.getLogger(IdentifyEvents.class.getName());
        aggregatedContexts = readAllEvents();
    }
    /**
     * 1: Find all users in the dataset.
     */
    public static List<String> findAllUsers() {
        // This step is straight forward, as events are grouped by user. Each
        // .zip file in the dataset corresponds to one user.
        int maxEv = MAX_EVENTS_CONSIDERED;
        List<String> zips = Lists.newLinkedList();

        if(maxEv == -1){
            for (File f : FileUtils.listFiles(new File(Configuration.EVENTS_DIR), new String[] { "zip" }, true)) {
                zips.add(f.getAbsolutePath());
                logger.log(Level.INFO, f.getName()+" user folder added");
            }
        }else{
            for (File f : FileUtils.listFiles(new File(Configuration.EVENTS_DIR), new String[] { "zip" }, true)) {
                zips.add(f.getAbsolutePath());

                --maxEv;
                if(maxEv == 0)
                    break;

                logger.log(Level.INFO, f.getName()+" user folder added. Left to add: "+maxEv);
            }
        }

        return zips;
    }

    /**
     * 2: Reading events
     */
    public static List<Context> readAllEvents() {
        // each .zip file corresponds to a user
        List<String> userZips = findAllUsers();
        List<Context> aggregatedContexts = new LinkedList<>();

        for (String user : userZips) {
            // you can use our helper to open a file...
            ReadingArchive ra = new ReadingArchive(new File(user));
            // ...iterate over it...
            while (ra.hasNext()) {
                // ... and desrialize the IDE event.
                IDEEvent e = ra.getNext(IDEEvent.class);
                // afterwards, you can process it as a Java object
                //System.out.println(e.getContext());
                logger.log(Level.INFO, "Processing events for: "+user.toString());
                aggregatedContexts.addAll(process(e));

            }
            ra.close();
        }
        return aggregatedContexts;
    }

    /**
     * 4: Processing events
     */
    public static List<Context> process(IDEEvent event) {
        // once you have access to the instantiated event you can dispatch the
        // type. As the events are not nested, we did not implement the visitor
        // pattern, but resorted to instanceof checks.
        List<Context> contexts = new LinkedList<>();
        if (event instanceof CompletionEvent) {
            // if the correct type is identified, you can cast it...
            CompletionEvent ce = (CompletionEvent) event;
            contexts.add(ce.getContext());
            logger.log(Level.INFO, "Event "+ event.getClass().getName() + " added");
        }

        return contexts;
    }

    public List<Context> getAggregatedContexts(){
        return aggregatedContexts;
    }

    public long getAggregatedContextsSize(){
        return aggregatedContexts.size();
    }
}