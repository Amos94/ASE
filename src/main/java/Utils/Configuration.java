package Utils;

import java.util.Arrays;
import java.util.List;

/**
 * Static variables
 */
public class Configuration {

    // final Configuration
    // Location of the KaVE dataset
    public static final String EVENTS_DIR = "Data/Events";
    public static final String CONTEXTS_DIR = "Data/Contexts";

    // Location where the index should be stored
    public static final String INDEX_STORAGE = "IndexStorage";

    //Maximum number of candidates
    public static final int MAX_CANDIDATES = 10;

    //English stopwords added for making the experiments described in the paper
    public static final List<String> STOP_WORDS = Arrays.asList(
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
            "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just",
            "don", "should", "now"
    );

    // Overwritable configuration
    //Maximum number if queries for the demo
    private static int RECOMMENDATION_ZIPS = 3; //to disable this set it to -1

    //Experiments (non final)
    //Do you want to remove the stopwords from the stemmed identifiers?
    private static boolean REMOVE_STOP_WORDS = false;

    // ReIndex the Database
    private static boolean REINDEX_DATABASE = false;

    // Should there be an evaluation of the events? => often used if you only want to reindex the db for experiments
    private static boolean EVALUATION = true;

    // Number of statements to consider for overall context
    private static int LAST_N_CONSIDERED_STATEMENTS = 3;

    //use events and query against the whole indexed database for recommendations
    private static boolean USE_EVENTS = false;

    //use contexts and query against the filtered by project indexes for recommendations => as described in the paper
    //Due to we could not fetch project info from Events and match it to Projects in Contexts
    private static boolean USE_TEST_CONTEXTS = true;

    private static String DELIMITER = "*********************************************************";


    // Getter & Setter
    public static int getRecommendationZips() {
        return RECOMMENDATION_ZIPS;
    }

    public static void setRecommendationZips(int recommendationZips) {
        RECOMMENDATION_ZIPS = recommendationZips;
    }

    public static boolean getRemoveStopWords() {
        return REMOVE_STOP_WORDS;
    }

    public static void setRemoveStopWords(boolean removeStopWords) {
        REMOVE_STOP_WORDS = removeStopWords;
    }

    public static boolean getReindexDatabase() {
        return REINDEX_DATABASE;
    }

    public static void setReindexDatabase(boolean reindexDatabase) {
        REINDEX_DATABASE = reindexDatabase;
    }

    public static boolean getEVALUATION() {
        return EVALUATION;
    }

    public static void setEVALUATION(boolean EVALUATION) {
        Configuration.EVALUATION = EVALUATION;
    }

    public static int getLastNConsideredStatements() {
        return LAST_N_CONSIDERED_STATEMENTS;
    }

    public static void setLastNConsideredStatements(int lastNConsideredStatements) {
        LAST_N_CONSIDERED_STATEMENTS = lastNConsideredStatements;
    }
    

    public static boolean getUseEvents() {
        return USE_EVENTS;
    }

    public static void setUseEvents(boolean useEvents) {
        USE_EVENTS = useEvents;
    }

    public static boolean getUseTestContexts() {
        return USE_TEST_CONTEXTS;
    }

    public static void setUseTestContexts(boolean useTestContexts) {
        USE_TEST_CONTEXTS = useTestContexts;
    }

    public static String getDELIMITER() {
        return DELIMITER;
    }

    public static void setDELIMITER(String DELIMITER) {
        Configuration.DELIMITER = DELIMITER;
    }
}