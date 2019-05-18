package Utils;

/**
 * Static variables
 */
public class Configuration {

    // Location of the KaVE dataset
    public static final String EVENTS_DIR = "Data/Events";
    public static final String CONTEXTS_DIR = "Data/Contexts";

    // Location where the index should be stored
    public static final String INDEX_STORAGE = "IndexStorage";

    // Number of statements to consider for overall context
    public static final int LAST_N_CONSIDERED_STATEMENTS = 4;

    //Maximum number of candidates
    public static final int MAX_CANDIDATES = 10;

}