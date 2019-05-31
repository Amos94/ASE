package events;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.Configuration;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import context.IoHelper;

public class IdentifyTestContexts {
    private String ctxsDir;
    private Logger logger = Logger.getLogger(IdentifyTestContexts.class.getName());
    private List<Context> aggregatedContexts;
    private String projectName;

    /**
     * Identify all test contexts
     */
    public IdentifyTestContexts() {
        this.ctxsDir = Configuration.CONTEXTS_DIR;
        if (Configuration.isTestingModeOn()){
            this.ctxsDir = Configuration.TEST_CONTEXTS_DIR;
        }
        aggregatedContexts = new LinkedList<>();
        run();
    }

    /**
     * Run
     */
    private void run() {

        Set<String> slnZips = IoHelper.findAllZips(ctxsDir);

        int maxEv = Configuration.getRecommendationZips();

        if(maxEv == -1) {
            for (String slnZip : slnZips) {
                logger.log(Level.INFO,"\n#### processing... ");
                aggregatedContexts.addAll(processZip(slnZip));
            }
        }
        else{
            for (String slnZip : slnZips) {
                logger.log(Level.INFO,"\n######## processing... ");
                aggregatedContexts.addAll(processZip(slnZip));

                --maxEv;
                if(maxEv == 0)
                    break;
            }
        }
    }

    /**
     * Process all zips
     *
     * @param slnZip
     * @return contexts
     */
    private List<Context> processZip(String slnZip) {

        String[] zipName = slnZip.split("/");
        int pnSize = zipName.length;
        this.projectName = zipName[pnSize-1].replace(".zip","");


        List<Context> contexts = new LinkedList<>();
        try (IReadingArchive ra = new ReadingArchive(new File(ctxsDir, slnZip))) {

            while (ra.hasNext()) {

                Context ctx = ra.getNext(Context.class);
                contexts.add(ctx);
            }
        }

        return contexts;
    }

    /**
     * get all aggregated contexts
     * @return
     */
    public List<Context> getAggregatedContexts(){
        return aggregatedContexts;
    }

    /**
     * Get the project name
     *
     * @return project name
     */
    public String getProjectName(){
        return projectName;
    }
}
