package Index;

import Utils.Configuration;
import Visitors.ContextVisitor;
import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.*;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.statements.*;
import opennlp.tools.stemmer.PorterStemmer;

import java.util.*;


/**
 * Visitor that takes a body of statements into the visit method and returns a list of IndexDocuments
 */
public class IndexDocumentExtractionVisitor extends AbstractTraversingNodeVisitor<List<IndexDocument>, Void> {

    private final ContextVisitor CONTEXT_VISITOR = new ContextVisitor();

    /**
     * Create a list to visit
     *
     * @param body
     * @param index
     * @return
     */
    @Override
    protected List<Void> visit(List<IStatement> body, List<IndexDocument> index) {
        for (IStatement statement : body) {
            if (statement instanceof IExpressionStatement || statement instanceof IAssignment) {
                IAssignableExpression expression;
                if (statement instanceof IExpressionStatement) {
                    expression = ((IExpressionStatement) statement).getExpression();
                } else {
                    expression = ((IAssignment) statement).getExpression();
                }
                if (expression instanceof IInvocationExpression) {

                    doVisit(expression, body, statement, index);
                }
            }
        }

        return super.visit(body, index);
    }

    /**
     * Main Code for the Visitor, which overrides all method identifiers
     *
     * @param expression
     * @param body
     * @param statement
     * @param indexDocuments
     */
    public void doVisit(IAssignableExpression expression, List<IStatement> body, IStatement statement, List<IndexDocument> indexDocuments) {
        final IMemberName method = new IMemberName() {
            @Override
            public String getIdentifier() {
                return ((IInvocationExpression) expression).getMethodName().getIdentifier();
            }

            @Override
            public boolean isUnknown() {
                return ((IInvocationExpression) expression).getMethodName().isUnknown();
            }

            @Override
            public boolean isHashed() {
                return ((IInvocationExpression) expression).getMethodName().isHashed();
            }

            @Override
            public ITypeName getDeclaringType() {
                return ((IInvocationExpression) expression).getMethodName().getDeclaringType();
            }

            @Override
            public ITypeName getValueType() {
                return ((IInvocationExpression) expression).getMethodName().getValueType();
            }

            @Override
            public boolean isStatic() {
                return ((IInvocationExpression) expression).getMethodName().isStatic();
            }

            @Override
            public String getName() {
                return ((IInvocationExpression) expression).getMethodName().getName();
            }

            @Override
            public String getFullName() {
                return ((IInvocationExpression) expression).getMethodName().getFullName();
            }
        };

        final IMethodName methodName = ((IInvocationExpression) expression).getMethodName();
        final String methodNameStr = methodName.getName();
        String type = methodName.getDeclaringType().getFullName();
        type = normalizeType(type);

        // Don't index constructors
        if (!methodNameStr.equals("???") || !methodNameStr.equals("???")) {
            if (!methodName.isConstructor()) {

                // create contexts
                List<IStatement> lastNStatements = getLastNStatementsBeforeStatement(body, body.indexOf(statement), Configuration.LAST_N_CONSIDERED_STATEMENTS);
                Set<String> overallContextSet = new HashSet<>();
                lastNStatements.forEach(iStatement -> iStatement.accept(CONTEXT_VISITOR, overallContextSet));


                // create a new IndexDocument
                List<String> overallContext = new LinkedList<>();
                for (String identifier : overallContextSet) {
                    if (identifierSanitization(identifier) != null)
                        overallContext.addAll(identifierSanitization(identifier));
                    //System.out.println(identifierSanitization(identifier));
                }
                IndexDocument indexDocument = new IndexDocument(methodNameStr, method, type, overallContext);
                indexDocuments.add(indexDocument);
            }
        }
    }

    /**
     * Removes the generic part of generic parts, which contains a lot of special symbols.
     *
     * @param type
     * @return
     */
    private String normalizeType(String type) {
        return type.split("`")[0];
    }

    /**
     * If the list doesn't contain enough elements, i.e (indexOfStatement - lastNConsideredStatements < 0),
     * then the return value is just a list statements up to the start of the list.
     *
     * @param statements
     * @param indexOfStatement
     * @param lastNConsideredStatements
     * @return array of statements
     */
    private List<IStatement> getLastNStatementsBeforeStatement(List<IStatement> statements, int indexOfStatement, int lastNConsideredStatements) {

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
     * @param identifier
     * @return
     */
    public List<String> identifierSanitization(String identifier) {
        if (Configuration.REMOVE_STOP_WORDS) {
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
     * @param identifier
     * @return identifierSplitList
     */
    public List<String> splitCamelCase(String identifier) {
        List<String> identifierSplitList = new LinkedList<>();

        for (String idf : identifier.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])|_")) {
            identifierSplitList.add(idf);
        }

        return identifierSplitList;
    }

    /**
     * Method to stem the identifiers
     *
     * @param identifiers
     * @return stemmedIdentifiers
     */
    public List<String> stemIdentifiers(List<String> identifiers) {
        PorterStemmer stemmer = new PorterStemmer();
        List<String> stemmedIdentifiers = new LinkedList<>();

        for (String identifier : identifiers) {
            //System.out.println("Unstemmed: " + identifier + "\nStemmed: " + stemmer.stem(identifier)); //Making sure it stemms well
            stemmedIdentifiers.add(stemmer.stem(identifier));
        }

        return stemmedIdentifiers;
    }

    /**
     * Method to remove stopwords
     *
     * @param identifiers
     * @return identifiers
     */
    public List<String> removeStopWords(List<String> identifiers) {
        identifiers.removeIf(i -> isStopWord(i));

        return identifiers;
    }

    /**
     * Method to destinguish if it is a stopword or not
     *
     * @param identifier
     * @return boolean
     */
    public boolean isStopWord(String identifier) {
        if (Configuration.STOP_WORDS.contains(identifier))
            return true;
        return false;
    }
}

