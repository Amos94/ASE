package Index;

import com.github.tomtung.jsimhash.SimHashBuilder;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Class representing the context around a given method call and the type of the receiver object
 * in a document as described by the paper {@see <a href="http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.703.9376&rep=rep1&type=pdf">}
 * <p>
 * Important: Due to how the id is created, different IndexDocument objects will have the same {@link IndexDocument#id}
 * if they have the same {@link IndexDocument#type}, {@link IndexDocument#methodCall}, and {@link IndexDocument#overallContext}
 */
public class IndexDocument implements Serializable {

    private transient SimHashBuilder simHashBuilder;
    private String id;
    private String methodCall;
    private String type;
    private Set<String> overallContext;
    private long overallContextSimhash;

    /**
     * Creates a new IndexDocument storing the given information and assigns it an id.
     */
    public IndexDocument(String methodCall, String type, Collection<String> overallContext) {
        if (type == null || type.equals("")) {
            throw new IllegalArgumentException("Parameter 'type' of IndexDocument must not be null or empty!");
        }
        this.methodCall = methodCall;
        this.type = type;

        this.overallContext = new TreeSet<>(overallContext);
        this.simHashBuilder = new SimHashBuilder();
        this.overallContextSimhash = createSimhashFromStrings(setToList(this.overallContext));

        String uniqueDeterministicId = type + "_" + (methodCall == null ? "-" : methodCall) + "_" + concatenate(setToList(this.overallContext));
        this.id = DigestUtils.sha256Hex(uniqueDeterministicId);
    }

    /**
     * Creates a new IndexDocument with the given information.
     * <p>
     * Use this constructor only if you are loading IndexDocument instances from a stored model
     * and you already know their docID and simhashes.
     */
    public IndexDocument(String docId, String methodCall, String type, Collection<String> overallContext, long overallContextSimhash) {
        id = docId;
        this.methodCall = methodCall;
        this.type = type;
        this.overallContext = new TreeSet<>(overallContext);
        this.overallContextSimhash = overallContextSimhash;
    }

    /*
      Getters
     */

    public String getId() {
        return id;
    }
    public String getMethodCall() {
        return methodCall;
    }
    public String getType() {
        return type;
    }
    public List<String> getOverallContext() {
        return setToList(overallContext);
    }
    public long getOverallContextSimhash() {
        return overallContextSimhash;
    }

    /**
     * Generic setToList
     *
     * @param set
     * @param <T>
     * @return
     */
    private <T> List<T> setToList(Set<T> set) {
        List<T> result = new LinkedList<>();
        result.addAll(set);
        return result;
    }

    /**
     * Creates a SimHash from strings
     *
     * @param strings
     * @return
     */
    private long createSimhashFromStrings(List<String> strings) {
        String concatenatedString = concatenate(strings);
        simHashBuilder.reset();
        simHashBuilder.addStringFeature(concatenatedString);
        return (long) simHashBuilder.computeResult();
    }

    /**
     * Helper method to concatenate
     *
     * @param strings
     * @return
     */
    private String concatenate(List<String> strings) {
        StringBuilder concatenatedString = new StringBuilder();
        for (String s : strings) {
            concatenatedString.append(s);
        }
        return concatenatedString.toString();
    }

    @Override
    public String toString() {
        return "IndexDocument{" +
                "id='" + id + '\'' +
                ", methodCall='" + methodCall + '\'' +
                ", type='" + type + '\'' +
                ", overallContext=" + overallContext +
                ", overallContextSimhash=" + overallContextSimhash +
                '}';
    }

    // equals method required for detecting already indexed documents (to avoid duplicate elements in index)
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IndexDocument) {
            IndexDocument other = (IndexDocument) obj;
            return other.getId().equals(this.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
