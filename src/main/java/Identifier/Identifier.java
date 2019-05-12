package Identifier;

import Identifier.ProcessIdentifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Identifier {

    private ProcessIdentifier processor;
    private List<String> identifierSplit;
    private String identifierName;
    private Set<String> stemmedIdentifiers;

    public Identifier(String identifierName){

        processor = new ProcessIdentifier();
        stemmedIdentifiers = new HashSet<String>();

        if(identifierName.toCharArray().length > 1){
            identifierSplit = processor.SplitIdentifier(identifierName);
            for(String word : identifierSplit){
                stemmedIdentifiers.add(word);
            }
        }
    }

    public Set<String> getProcessedIdentifiers(){
        return stemmedIdentifiers;
    }


}
