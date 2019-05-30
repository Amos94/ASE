package Utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void testAllEnums()
    {
        assertEquals("Data/Events", Configuration.EVENTS_DIR);
        assertEquals("Data/Contexts", Configuration.CONTEXTS_DIR);
        assertEquals("IndexStorage", Configuration.INDEX_STORAGE);
    }

    @Test
    public void testSettersAndGetters(){
        Configuration config = new Configuration();
        config.setRemoveStopWords(false);
        assertEquals(false,config.getRemoveStopWords());

        config.setReindexDatabase(false);
        assertEquals(false, config.getReindexDatabase());

        config.setEVALUATION(true);
        assertEquals(true, config.getEVALUATION());

        config.setLastNConsideredStatements(3);
        assertEquals(3, config.getLastNConsideredStatements());

        config.setUseEvents(false);
        assertEquals(false, config.getUseEvents());

        config.setUseTestContexts(true);
        assertEquals(true, config.getUseTestContexts());

        config.setDELIMITER("*********************************************************");
        assertEquals("*********************************************************", config.getDELIMITER());

    }
}
