package index;

import cc.kave.commons.model.naming.codeelements.IMemberName;
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

    private String projectName;
    private String id;
    private String methodCall;
    private IMemberName method;
    private String type;
    private Set<String> overallContext;

    // Getters
    String getId() {
        return id;
    }

    public String getMethodCall() {
        return methodCall;
    }

    public String getProjectName() {
        return projectName;
    }

    public IMemberName getMethod() {
        return method;
    }

    public String getType() {
        return type;
    }

    public List<String> getOverallContext() {
        return setToList(overallContext);
    }


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

        String uniqueDeterministicId = type + "_" + (methodCall == null ? "-" : methodCall) + "_" + concatenate(setToList(this.overallContext));
        this.id = DigestUtils.sha256Hex(uniqueDeterministicId);
    }

    /**
     * Creates a new IndexDocument without any information about the docId
     *
     * @param methodCall - method name
     * @param method - method details - IMemberName
     * @param type - method type
     * @param overallContext - context considered
     */
    public IndexDocument(String methodCall, IMemberName method, String type, Collection<String> overallContext, String projectName) {
        if (type == null || type.equals("")) {
            throw new IllegalArgumentException("Parameter 'type' of IndexDocument must not be null or empty!");
        }

        this.projectName = projectName;

        this.method = method;
        this.type = type;

        this.overallContext = new TreeSet<>(overallContext);

        String uniqueDeterministicId = type + "_" + (methodCall == null ? "-" : methodCall) + "_" + concatenate(setToList(this.overallContext));
        this.id = DigestUtils.sha256Hex(uniqueDeterministicId);
    }

    /**
     * Creates a new IndexDocument with the given information.
     * <p>
     * Use this constructor only if you are loading IndexDocument instances from a stored model
     *
     * @param docId - document id
     * @param methodCall - method name
     * @param type - method type
     * @param overallContext - context
     */
    public IndexDocument(String docId, String methodCall, String type, Collection<String> overallContext) {
        id = docId;
        this.methodCall = methodCall;
        this.type = type;
        this.overallContext = new TreeSet<>(overallContext);
    }

    /**
     * Creates a new IndexDocument with the given information.
     * <p>
     * Use this constructor only if you are loading IndexDocument instances from a stored model
     *
     * @param docId - document id
     * @param method - method - IMemberName
     * @param overallContext - context
     */
    public IndexDocument(String docId, IMemberName method, Collection<String> overallContext) {
        id = docId;
        this.method = method;
        this.methodCall = methodCall;
        this.type = type;
        this.overallContext = new TreeSet<>(overallContext);
    }

    public IndexDocument(String docId, IMemberName method, Collection<String> overallContext, String projectName) {
        id = docId;
        this.method = method;
        this.methodCall = methodCall;
        this.type = type;
        this.overallContext = new TreeSet<>(overallContext);
        this.projectName = projectName;
    }


    /**
     * Generic setToList
     *
     * @param set - set
     * @param <T> - generic
     * @return - a list from a set
     */
    private <T> List<T> setToList(Set<T> set) {
        return new LinkedList<>(set);
    }

    /**
     * Helper method to concatenate
     *
     * @param strings - strings to concatenate
     * @return - concatenated strings
     */
    private String concatenate(List<String> strings) {
        StringBuilder concatenatedString = new StringBuilder();
        for (String s : strings) {
            concatenatedString.append(s);
        }
        return concatenatedString.toString();
    }

    /**
     * Method to enhance toString
     *
     * @return string
     */
    @Override
    public String toString() {
        return "IndexDocument{" +
                "id='" + id + '\'' +
                ", methodCall='" + methodCall + '\'' +
                ", type='" + type + '\'' +
                ", overallContext=" + overallContext +
                ", projectname=" + projectName +
                '}';
    }

    /**
     * Equals method required for detecting already indexed documents (to avoid duplicate elements in index)
     *
     * @param obj - obj
     * @return - boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IndexDocument) {
            IndexDocument other = (IndexDocument) obj;
            return other.getId().equals(this.getId());
        }
        return false;
    }

    /**
     * HashCode Method to distinguish equality
     *
     * @return - hashcode a.k.a. guid
     */
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }
}
