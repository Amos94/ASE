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
}
