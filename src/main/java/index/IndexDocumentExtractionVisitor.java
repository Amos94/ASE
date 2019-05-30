package index;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import utils.Configuration;
import visitors.ContextVisitor;

import java.util.*;


/**
 * Visitor that takes a body of statements into the visit method and returns a list of IndexDocuments
 */
public class IndexDocumentExtractionVisitor extends AbstractTraversingNodeVisitor<List<IndexDocument>, Void> {

    private final ContextVisitor CONTEXT_VISITOR = new ContextVisitor();
    private String projectName;

    public IndexDocumentExtractionVisitor() {
    }

    public IndexDocumentExtractionVisitor(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Create a list to visit
     *
     * @param body - method body
     * @param index - index
     * @return - visit
     */
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
     * @param expression - expression
     * @param body - body
     * @param statement - statement
     * @param indexDocuments - indexed documents
     */
    void doVisit(IAssignableExpression expression, List<IStatement> body, IStatement statement, List<IndexDocument> indexDocuments) {
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
        type = new IndexHelper().normalizeType(type);

        // Don't index constructors
        if (!methodNameStr.equals("???")) {
            if (!methodName.isConstructor()) {

                // create contexts
                List<IStatement> lastNStatements = new IndexHelper().getLastNStatementsBeforeStatement(body, body.indexOf(statement), Configuration.getLastNConsideredStatements());
                Set<String> overallContextSet = new HashSet<>();
                lastNStatements.forEach(iStatement -> iStatement.accept(CONTEXT_VISITOR, overallContextSet));


                // create a new IndexDocument
                List<String> overallContext = new LinkedList<>();
                for (String identifier : overallContextSet) {
                    if (new IndexHelper().identifierSanitization(identifier) != null)
                        overallContext.addAll(Objects.requireNonNull(new IndexHelper().identifierSanitization(identifier)));
                }
                IndexDocument indexDocument = new IndexDocument(methodNameStr, method, type, overallContext, projectName);
                indexDocuments.add(indexDocument);
            }
        }
    }



}

