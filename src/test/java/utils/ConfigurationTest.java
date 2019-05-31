package utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationTest {

    @Test
    public void testAllEnums()
    {
        assertEquals("Data/events", Configuration.EVENTS_DIR);
        assertEquals("Data/Contexts", Configuration.CONTEXTS_DIR);
        assertEquals("IndexStorage", Configuration.INDEX_STORAGE);
        assertEquals("src/test/java/testdata/TestEvents", Configuration.TEST_EVENTS_DIR);
        assertEquals("src/test/java/testdata/TestContexts", Configuration.TEST_CONTEXTS_DIR);
    }

    @Test
    public void getRecommendationZips() {
        Configuration.setRecommendationZips(1);
        assertEquals(1,Configuration.getRecommendationZips());
    }

    @Test
    public void setRecommendationZips() {
        Configuration.setRecommendationZips(10);
        assertEquals(10,Configuration.getRecommendationZips());
    }

    @Test
    public void getRemoveStopWords() {
        Configuration.setRemoveStopWords(false);
        assertFalse(Configuration.getRemoveStopWords());
    }

    @Test
    public void setRemoveStopWords() {
        Configuration.setRemoveStopWords(true);
        Assert.assertTrue(Configuration.getRemoveStopWords());
    }

    @Test
    public void getReindexDatabase() {
        Configuration.setReindexDatabase(false);
        assertFalse(Configuration.getReindexDatabase());
    }

    @Test
    public void setReindexDatabase() {
        Configuration.setReindexDatabase(true);
        Assert.assertTrue(Configuration.getReindexDatabase());
    }

    @Test
    public void getEVALUATION() {
        Configuration.setEVALUATION(false);
        assertFalse(Configuration.getEVALUATION());
    }

    @Test
    public void setEVALUATION() {
        Configuration.setEVALUATION(true);
        Assert.assertTrue(Configuration.getEVALUATION());
    }

    @Test
    public void getLastNConsideredStatements() {
        Configuration.setLastNConsideredStatements(3);
        assertEquals(3, Configuration.getLastNConsideredStatements());
    }

    @Test
    public void setLastNConsideredStatements() {
        Configuration.setLastNConsideredStatements(1);
        assertEquals(1, Configuration.getLastNConsideredStatements());
    }

    @Test
    public void getUseEvents() {
        Configuration.setUseEvents(true);
        assertTrue(Configuration.getUseEvents());
    }

    @Test
    public void setUseEvents() {
        Configuration.setUseEvents(false);
        assertFalse(Configuration.getUseEvents());
    }

    @Test
    public void getUseTestContexts() {
        Configuration.setUseTestContexts(true);
        assertTrue(Configuration.getUseTestContexts());
    }

    @Test
    public void setUseTestContexts() {
        Configuration.setUseTestContexts(false);
        assertFalse(Configuration.getUseTestContexts());
    }

    @Test
    public void getDELIMITER() {
        Configuration.setDELIMITER("**");
        assertEquals("**", Configuration.getDELIMITER());
    }

    @Test
    public void setDELIMITER() {
        Configuration.setDELIMITER("***");
        assertEquals("***", Configuration.getDELIMITER());
    }

    @Test
    public void getTESTINGMODEON() {
        Configuration.setTestingModeOn(false);
        assertEquals(false, Configuration.isTestingModeOn());
    }
}
