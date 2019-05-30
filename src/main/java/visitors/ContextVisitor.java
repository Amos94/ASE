package visitors;

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
     * @param stmt - statement
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(IEventDeclaration stmt, Set<String> overallContext) {
        // Add the identifier of the variable to the overall context
        overallContext.add(stmt.getName().getName());
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param stmt - statement
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(IEventReference stmt, Set<String> overallContext) {
        // Add the identifier of the variable to the overall context
        overallContext.add(stmt.getReference().getIdentifier());
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param decl - declaration
     * @param overallContext - overall context
     * @return null
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
     * Visit method adds the name
     *
     * @param stmt - statement
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(IReturnStatement stmt, Set<String> overallContext) {
        overallContext.add("<return>");
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param block - switch block
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(ISwitchBlock block, Set<String> overallContext) {
        overallContext.add("<switch>");
        for (ICaseBlock ignored : block.getSections()) {
            overallContext.add("<case>");
        }
        overallContext.add("<default>");
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param expr - expression
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(ICastExpression expr, Set<String> overallContext) {
        // Get name of target type of cast
        overallContext.add(expr.getTargetType().getName());
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param block - for block
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(IForLoop block, Set<String> overallContext) {
        overallContext.add("<for>");
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param stmt - statement
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(IContinueStatement stmt, Set<String> overallContext) {
        overallContext.add("<continue>");
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param stmt - statement
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(IGotoStatement stmt, Set<String> overallContext) {
        overallContext.add("<goto>");
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param stmt - statement
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(IBreakStatement stmt, Set<String> overallContext) {
        overallContext.add("<break>");
        return null;
    }


    /**
     * Visit method adds the name
     *
     * @param block - while block
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(IWhileLoop block, Set<String> overallContext) {
        overallContext.add("<while>");
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param block - if else
     * @param overallContext - overall context
     * @return null
     */
    @Override
    public Void visit(IIfElseBlock block, Set<String> overallContext) {
        overallContext.add("<if>");
        overallContext.add("<else>");
        return null;
    }

    /**
     * Visit method adds the name
     *
     * @param expr - expression
     * @param overallContext - overall context
     * @return null
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
     * @param expr expression
     * @param overallContext context
     * @return null
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
     * @param expr expression
     * @param overallContext context
     * @return null
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
     * @param block try block
     * @param overallContext context
     * @return null
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
     * @param block do loop
     * @param overallContext context
     * @return null
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
     * @param block foreach loop
     * @param overallContext contest
     * @return null
     */
    @Override
    public Void visit(IForEachLoop block, Set<String> overallContext) {
        overallContext.add("<foreach>");
        overallContext.add("<in>");
        overallContext.add(block.getDeclaration().getType().getName());
        return null;
    }
}