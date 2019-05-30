package Utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class ConfigurationTest {

    @Test
    public void testAllEnums()
    {
        public static final List<String> stoppers = Arrays.asList(
                "i", "me", "my", "myself", "we", "our", "ours", "ourselves",
                "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she",
                "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs",
                "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am",
                "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do",
                "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as",
                "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into",
                "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in",
                "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when",
                "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such",
                "no", "nor", "not", "only",)

        // finalized
        assertEquals("Data/Events", Configuration.EVENTS_DIR);
        assertEquals("Data/Contexts", Configuration.CONTEXTS_DIR);
        assertEquals("IndexStorage", Configuration.INDEX_STORAGE);
        assertEquals(10, Configuration.MAX_CANDIDATES);
        assertEquals(stoppers, Configuration.STOP_WORDS);

        // By setters
        assertEquals(3, Configuration.setRecommendationZips(3);

        Configuration.setRemoveStopWords(false);
        assertEquals(false, Configuration.getRemoveStopWords();

        Configuration.setReindexDatabase(true);
        assertEquals(true, Configuration.getReindexDatabase());

        Configuration.setEVALUATION(true);
        assertEquals(true, Configuration.getEVALUATION());

        Configuration.setLastNConsideredStatements(10);
        assertEquals(10, Configuration.getLastNConsideredStatements());

        Configuration.setUseEvents(true);
        assertEquals(true, Configuration.getUseEvents());

        Configuration.setUseTestContexts(true);
        assertEquals(true, Configuration.getUseTestContexts());

        Configuration.setDELIMITER("***");
        assertEquals("***", Configuration.getDELIMITER());

    }
}
