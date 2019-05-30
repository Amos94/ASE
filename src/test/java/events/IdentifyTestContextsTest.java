package events;

import org.junit.Test;
import cc.kave.commons.model.events.completionevents.Context;
import utils.Configuration;

import java.util.List;

import static org.junit.Assert.*;

public class IdentifyTestContextsTest {
    @Test
    public void runTest(){
        IdentifyTestContexts identifyTestContexts = new IdentifyTestContexts();
        Configuration.setRecommendationZips(3);
        List<Context> aggregatedContexts = identifyTestContexts.getAggregatedContexts();
        assertNotNull(aggregatedContexts);

        String projectName = identifyTestContexts.getProjectName();
        assertNotNull(projectName);
    }

    @Test
    public void runTestMaxEvMinusOne(){
        Configuration.setRecommendationZips(-1);
        IdentifyTestContexts identifyTestContexts = new IdentifyTestContexts();
        assertNotNull(identifyTestContexts);
    }

}