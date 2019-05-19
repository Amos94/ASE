package Utils;

import java.util.Arrays;
import java.util.List;

/**
 * Static variables
 */
public class Configuration {

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

    //Experiments (non final)
    //Do you want to remove the stopwords from the stemmed identifiers?
    public static boolean REMOVE_STOP_WORDS = true;

    // ReIndex the Database
    public static boolean REINDEX_DATABASE = false;

    // Should there be an evaluation of the events? => often used if you only want to reindex the db for experiments
    public static boolean EVALUATION = true;

    // Number of statements to consider for overall context
    public static int LAST_N_CONSIDERED_STATEMENTS = 4;

    //Maximum number if queries for the demo
    public static int MAX_EVENTS_CONSIDERED = -1; //to disable this set it to -1

}