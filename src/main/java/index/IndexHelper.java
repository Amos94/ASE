package index;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.*;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.statements.*;
import opennlp.tools.stemmer.PorterStemmer;
import utils.Configuration;

import java.util.*;

class IndexHelper  extends AbstractTraversingNodeVisitor<IInvertedIndex, Void>{

    /**
     * If the list doesn't contain enough elements, i.e (indexOfStatement - lastNConsideredStatements < 0),
     * then the return value is just a list statements up to the start of the list.
     *
     * @param statements - statements
     * @param indexOfStatement - index of statement
     * @param lastNConsideredStatements - lookback
     * @return array of statements
     */
    List<IStatement> getLastNStatementsBeforeStatement(List<IStatement> statements, int indexOfStatement, int lastNConsideredStatements) {

        if (indexOfStatement - lastNConsideredStatements < 0) {
            lastNConsideredStatements = indexOfStatement;
        }
        int startIndex = 0;
        IStatement lastStatement = statements.get(statements.size() - 1);
        if (
                !(
                        (lastStatement instanceof IBreakStatement)
                                && (lastStatement instanceof IForLoop)
                                && (lastStatement instanceof IForEachLoop)
                                && (lastStatement instanceof IIfElseExpression)
                                && (lastStatement instanceof IIfElseBlock)
                                && (lastStatement instanceof IContinueStatement)
                                && (lastStatement instanceof IGotoStatement)
                                && (lastStatement instanceof IWhileLoop)
                                && (lastStatement instanceof ITryBlock)
                                && (lastStatement instanceof IDoLoop)
                                && (lastStatement instanceof IReturnStatement)
                                && (lastStatement instanceof ISwitchBlock)
                )
        ) {
            startIndex = indexOfStatement - lastNConsideredStatements;
        }

        IStatement[] smts = new IStatement[statements.size()];
        statements.toArray(smts);

        IStatement[] res = new IStatement[lastNConsideredStatements];
        System.arraycopy(smts, startIndex, res, 0, lastNConsideredStatements);
        return Arrays.asList(res);
    }

    /**
     * Sanitize the identifier
     *
     * @param identifier - identifier
     * @return - return serialization
     */
    List<String> identifierSanitization(String identifier) {
        if (Configuration.getRemoveStopWords()) {
            if (identifier.length() != 1) {
                return removeStopWords(stemIdentifiers(splitCamelCase(identifier)));
            }
        } else {
            return stemIdentifiers(splitCamelCase(identifier));
        }
        return null;
    }

    /**
     * Method to split in CamelCase
     *
     * @param identifier - identifier
     * @return identifierSplitList
     */
    List<String> splitCamelCase(String identifier) {

        return new LinkedList<>(Arrays.asList(identifier.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])|_")));
    }

    /**
     * Method to stem the identifiers
     *
     * @param identifiers - identifier
     * @return stemmedIdentifiers
     */
    List<String> stemIdentifiers(List<String> identifiers) {
        PorterStemmer stemmer = new PorterStemmer();
        List<String> stemmedIdentifiers = new LinkedList<>();

        for (String identifier : identifiers) {
            stemmedIdentifiers.add(stemmer.stem(identifier));
        }

        return stemmedIdentifiers;
    }

    /**
     * Method to remove stopwords
     *
     * @param identifiers - identifier
     * @return identifiers
     */
    List<String> removeStopWords(List<String> identifiers) {


        for(String identifier : identifiers) {
            if(isStopWord(identifier)){
                identifiers.remove(identifier);
            }
        }

        return identifiers;
    }

    /**
     * Method to destinguish if it is a stopword or not
     *
     * @param identifier - identifier
     * @return boolean
     */
    boolean isStopWord(String identifier) {
        return Configuration.STOP_WORDS.contains(identifier);
    }

    /**
     * Removes the generic part of generic parts, which contains a lot of special symbols.
     *
     * @param type - type
     * @return normalized version
     */
    String normalizeType(String type) {
        return type.split("`")[0];
    }
}
