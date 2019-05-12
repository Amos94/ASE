package Utils;

/**
 * Class holding various configuration variables
 */
public class Configuration {

    // Location of the KaVE dataset
    public static final String EVENTS_DIR = "Data/Events";
    public static final String CONTEXTS_DIR = "Data/Contexts";

    // Location where the index should be stored
    public static final String PERSISTENCE_LOCATION = "Data/Model";
    public static final String PERSISTENCE_LOCATION_TEST = "/tmp";

    // Number of statements to consider for overall context
    public static final int LAST_N_CONSIDERED_STATEMENTS = 6;

    // Print progress about which zip file is currently being processed
    public static final boolean PRINT_PROGRESS = false;
    // Print progress about contexts being processed within a zip file
    public static final boolean PRINT_PROGRESS_CONTEXTS = true;
    // Limit the amount of training data, 0 (or smaller) for all data
    public static final int LIMIT_ZIPS = 20;


    /*
      DO NOT CONFIGURE ANYTHING BELOW THIS POINT
     */

    // global variable which is set to false when an interrupt (Ctrl + C) is recognized,
    // used to make a graceful shutdown during training phase
    public static volatile boolean keepRunning = true;

}