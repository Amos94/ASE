package Events;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import Utils.Configuration;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import Context.IoHelper;

public class IdentifyTestContexts {
    private String ctxsDir;
    private Logger logger = Logger.getLogger(IdentifyTestContexts.class.getName());
    private List<Context> aggregatedContexts;
    private String projectName;

    public IdentifyTestContexts() {
        this.ctxsDir = Configuration.CONTEXTS_DIR;
        aggregatedContexts = new LinkedList<>();
        run();
    }

    public void run() {

//        System.out.printf("looking (recursively) for solution zips in folder %s\n",
//                new File(ctxsDir).getAbsolutePath());

        Set<String> slnZips = IoHelper.findAllZips(ctxsDir);

        int maxEv = Configuration.RECOMMENDATION_ZIPS;

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

    public List<Context> processZip(String slnZip) {
        
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

    public List<Context> getAggregatedContexts(){
        return aggregatedContexts;
    }

    public long getAggregatedContextsSize(){
        return aggregatedContexts.size();
    }

    public String getProjectName(){
        return projectName;
    }
}
