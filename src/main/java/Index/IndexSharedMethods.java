package Index;

import java.util.Set;
import Context.IdentifyContext;

public interface IndexSharedMethods {

    String splitIdentifier(String identifier);
    String stemIdentifier(String identifier);
    Set<String> extractIdentifiers();
    IdentifyContext determineContext();

}
