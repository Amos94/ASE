package Identifier;

import java.util.LinkedList;
import java.util.List;
import opennlp.tools.stemmer.PorterStemmer;

public class ProcessIdentifier implements ProcessIdentifierInterface {


    @Override
    public String StemIdentifier(String identifier) {
        PorterStemmer stemmer = new PorterStemmer();

        return stemmer.stem(identifier);
    }

    @Override
    public List<String> SplitIdentifier(String identifier) {
        List<String> identifierSplitList = new LinkedList<>();

        for (String idf : identifier.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            identifierSplitList.add(idf);
        }

        return identifierSplitList;
    }
}
