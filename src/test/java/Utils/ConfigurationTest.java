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
        assertEquals(4, Configuration.LAST_N_CONSIDERED_STATEMENTS);
        assertEquals(false, Configuration.REINDEX_DATABASE);
        assertEquals(false, Configuration.VERBOSE_OUTPUT);
    }
}
