package Visitors;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import org.apache.commons.lang.mutable.MutableInt;

/**
 * Visits all InvocationExpressions (= method calls). Call it on an SST with
 * {@code sst.accept(new InvocationExpressionVisitor()}.
 * This is an easy way to traverse all InvocationExpressions in an SST, however it is
 * not possible to get the context of each InvocationExpression (e.g. lines before the
 * expression).
 */
public class InvocationExpressionVisitor extends AbstractTraversingNodeVisitor<MutableInt, Void> {

    @Override
    public Void visit(IInvocationExpression expr, MutableInt count) {
        final IMethodName methodName = expr.getMethodName();
        final String methodNameStr = methodName.getName();

        if (!methodNameStr.equals("???") || !methodNameStr.equals("???")) {
            if (!methodName.isConstructor()) {
                // increase the number of InvocationExpressions we have encountered
                count.increment();
//                System.out.println("current count: " + count);
                // print out some debug information about this method invocation
                System.out.println("Method Name:                  " + methodName);
//                System.out.println("Method Name (FullName):       " + methodName.getFullName());
                System.out.println("Method Name (getName):        " + methodName.getName());
//                System.out.println("Method Name (ReturnType):     " + methodName.getReturnType());
//                System.out.println("Method Name (ValueType):      " + methodName.getValueType());
                System.out.println("Method Name (DeclaringType):  " + methodName.getDeclaringType().getFullName());
//                System.out.println("Method Name (Parameters):     " + expr.getMethodName().getParameters());
//                System.out.println("Method Name (TypeParameters): " + expr.getMethodName().getTypeParameters());
//                System.out.println("Method Name (Identifier):     " + expr.getMethodName().getIdentifier());
//                System.out.println("Method Name (isInit):         " + methodName.isInit()); // Check for class initializers (!= constructors!)
//                System.out.println("Method Name (Constructor):    " + methodName.isConstructor());
//                System.out.println("Reference:                    " + expr.getReference().getIdentifier());
                System.out.println("-");
            }
        }
        return super.visit(expr, count);
    }

}