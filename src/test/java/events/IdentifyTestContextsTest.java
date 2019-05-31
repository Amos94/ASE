package events;

import org.junit.Test;
import cc.kave.commons.model.events.completionevents.Context;
import utils.Configuration;

import java.util.List;

import static org.junit.Assert.*;

public class IdentifyTestContextsTest {
    @Test
    public void runTest(){
        Configuration.setRecommendationZips(3);
        Configuration.setTestingModeOn(true);
        IdentifyTestContexts identifyTestContexts = new IdentifyTestContexts();
        List<Context> aggregatedContexts = identifyTestContexts.getAggregatedContexts();
        assertNotNull(aggregatedContexts);

        String projectName = identifyTestContexts.getProjectName();
        assertNotNull(projectName);
        Configuration.setTestingModeOn(false);
    }

    @Test
    public void runTestMaxEvMinusOne(){
        Configuration.setTestingModeOn(true);
        Configuration.setRecommendationZips(-1);
        IdentifyTestContexts identifyTestContexts = new IdentifyTestContexts();
        assertNotNull(identifyTestContexts);
        Configuration.setRecommendationZips(3);
        Configuration.setTestingModeOn(false);
    }

}