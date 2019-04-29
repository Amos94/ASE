package Index;

import java.util.Set;
import Context.Context;

public interface IndexSharedMethods {

    String splitIdentifier(String identifier);
    String stemIdentifier(String identifier);
    Set<String> extractIdentifiers();
    Context determineContext();

}
