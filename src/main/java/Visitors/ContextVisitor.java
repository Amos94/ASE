package Visitors;

import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.blocks.*;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.statements.*;

import java.util.Set;

/**
 * Class contextVisitor
 */
public class ContextVisitor extends AbstractTraversingNodeVisitor<Set<String>, Void> {
    @Override
    public Void visit(IVariableDeclaration stmt, Set<String> overallContext) {
        // Add the identifier of the variable to the overall context
        overallContext.add(stmt.getType().getName());
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param stmt
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IEventDeclaration stmt, Set<String> overallContext) {
        // Add the identifier of the variable to the overall context
        overallContext.add(stmt.getName().getName());
        return null;
    }

    /**
     * Visit method adds the reference identifier
     *
     * @param stmt
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IEventReference stmt, Set<String> overallContext) {
        // Add the identifier of the variable to the overall context
        overallContext.add(stmt.getReference().getIdentifier());
        return null;
    }

    /**
     * Visit method with the overal context type and method name
     *
     * @param decl
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IMethodDeclaration decl, Set<String> overallContext) {
        // Add the return type to the overall context
        overallContext.add(decl.getName().getReturnType().getName());
        // Add the method name itself to the overall context
        overallContext.add(decl.getName().getName());

        // Add the types of the parameters to the overall context
        for (IParameterName iParameterName : decl.getName().getParameters()) {
            overallContext.add(iParameterName.getValueType().getName());
        }
        return null;
    }

    /**
     * Visit method to add <return>
     *
     * @param stmt
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IReturnStatement stmt, Set<String> overallContext) {
        overallContext.add("<return>");
        return null;
    }

    /**
     * Visit method to add switch
     *
     * @param block
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(ISwitchBlock block, Set<String> overallContext) {
        overallContext.add("<switch>");
        for (ICaseBlock caseBlock : block.getSections()) {
            overallContext.add("<case>");
        }
        overallContext.add("<default>");
        return null;
    }

    /**
     * Visit method to get the name casted
     *
     * @param expr
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(ICastExpression expr, Set<String> overallContext) {
        // Get name of target type of cast
        overallContext.add(expr.getTargetType().getName());
        return null;
    }

    /**
     * Visit method for for-loop start
     *
     * @param block
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IForLoop block, Set<String> overallContext) {
        overallContext.add("<for>");
        return null;
    }

    /**
     * Visit method for <continue>
     *
     * @param stmt
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IContinueStatement stmt, Set<String> overallContext) {
        overallContext.add("<continue>");
        return null;
    }

    /**
     * Visit method for <goto>
     *
     * @param stmt
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IGotoStatement stmt, Set<String> overallContext) {
        overallContext.add("<goto>");
        return null;
    }

    /**
     * Visit method for <break>
     *
     * @param stmt
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IBreakStatement stmt, Set<String> overallContext) {
        overallContext.add("<break>");
        return null;
    }


    /**
     * Visit method for <while>
     *
     * @param block
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IWhileLoop block, Set<String> overallContext) {
        overallContext.add("<while>");
        return null;
    }

    /**
     * Visit method for if and else blocks
     * @param block
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IIfElseBlock block, Set<String> overallContext) {
        overallContext.add("<if>");
        overallContext.add("<else>");
        return null;
    }

    /**
     * Visit method for if and else Expression (not block)
     * @param expr
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IIfElseExpression expr, Set<String> overallContext) {
        overallContext.add("<if>");
        overallContext.add("<else>");
        return null;
    }

    /**
     * Method for the invocation expression
     *
     * @param expr
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IInvocationExpression expr, Set<String> overallContext) {
        // Add the name of invoked the method to the overall context
        overallContext.add(expr.getMethodName().getName());
        return null;
    }

    /**
     * Type check method
     *
     * @param expr
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(ITypeCheckExpression expr, Set<String> overallContext) {
        // Not sure if a type check also contains a keyword
        overallContext.add(expr.getType().getName());
        return null;
    }

    /**
     * Method for try blocks
     *
     * @param block
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(ITryBlock block, Set<String> overallContext) {
        overallContext.add("<try>");
        overallContext.add("<catch>");
        overallContext.add("<finally>");
        // Add the types of the parameters to the context
        for (ICatchBlock catchBlock : block.getCatchBlocks()) {
            overallContext.add(catchBlock.getParameter().getValueType().getName());
        }
        return null;
    }

    /**
     * Method for do and while
     *
     * @param block
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IDoLoop block, Set<String> overallContext) {
        overallContext.add("<do>");
        overallContext.add("<while>");
        return null;
    }

    /**
     * Method for foreach and in
     *
     * @param block
     * @param overallContext
     * @return
     */
    @Override
    public Void visit(IForEachLoop block, Set<String> overallContext) {
        overallContext.add("<foreach>");
        overallContext.add("<in>");
        overallContext.add(block.getDeclaration().getType().getName());
        return null;
    }
}