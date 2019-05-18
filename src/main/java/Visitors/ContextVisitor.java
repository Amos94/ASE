package Visitors;

import cc.kave.commons.model.naming.codeelements.IMemberName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
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

public class ContextVisitor extends AbstractTraversingNodeVisitor<Set<String>, Void> {
    @Override
    public Void visit(IVariableDeclaration stmt, Set<String> overallContext) {
        // Add the identifier of the variable to the overall context
        overallContext.add(stmt.getType().getName());
        return null;
    }

    @Override
    public Void visit(IEventDeclaration stmt, Set<String> overallContext) {
        // Add the identifier of the variable to the overall context
        overallContext.add(stmt.getName().getName());
        return null;
    }

    @Override
    public Void visit(IEventReference stmt, Set<String> overallContext) {
        // Add the identifier of the variable to the overall context
        overallContext.add(stmt.getReference().getIdentifier());
        return null;
    }

//    @Override
//    public Void visit(IAssignment stmt, Set<String> overallContext) {
//        // Add the identifier of the variable to the overall context
//
//        return null;
//    }

//    @Override
//    public Void visit(IExpressionStatement stmt, Set<String> overallContext) {
//        // Add the identifier of the variable to the overall context
//        overallContext.add(stmt.getExpression().toString());
//        return null;
//    }

    @Override
    public Void visit(IMethodDeclaration decl, Set<String> overallContext) {
        // Add the return type to the overall context
        overallContext.add(decl.getName().getReturnType().getName());
        // Add the method name itself to the overall context
        overallContext.add(decl.getName().getName());
//        overallContext.add(decl.getName().getDeclaringType().getName());
//        overallContext.add(decl.getName().getValueType().getName());
//        overallContext.add(""+decl.getName().isStatic());
//        overallContext.add(decl.getName().getName());
//        overallContext.add(decl.getName().getFullName());


        // Add the types of the parameters to the overall context
        for (IParameterName iParameterName : decl.getName().getParameters()) {
            overallContext.add(iParameterName.getValueType().getName());
        }
        return null;
    }

    @Override
    public Void visit(IReturnStatement stmt, Set<String> overallContext) {
        overallContext.add("<return>");
        return null;
    }

    @Override
    public Void visit(ISwitchBlock block, Set<String> overallContext) {
        overallContext.add("<switch>");
        for (ICaseBlock caseBlock : block.getSections()) {
            overallContext.add("<case>");
        }
        overallContext.add("<default>");
        return null;
    }

    @Override
    public Void visit(ICastExpression expr, Set<String> overallContext) {
        // Get name of target type of cast
        overallContext.add(expr.getTargetType().getName());
        return null;
    }

    @Override
    public Void visit(IForLoop block, Set<String> overallContext) {
        overallContext.add("<for>");
        return null;
    }

    @Override
    public Void visit(IContinueStatement stmt, Set<String> overallContext) {
        overallContext.add("<continue>");
        return null;
    }

    @Override
    public Void visit(IGotoStatement stmt, Set<String> overallContext) {
        overallContext.add("<goto>");
        return null;
    }

    @Override
    public Void visit(IBreakStatement stmt, Set<String> overallContext) {
        overallContext.add("<break>");
        return null;
    }

    @Override
    public Void visit(IWhileLoop block, Set<String> overallContext) {
        overallContext.add("<while>");
        return null;
    }

    @Override
    public Void visit(IIfElseBlock block, Set<String> overallContext) {
        overallContext.add("<if>");
        overallContext.add("<else>");
        return null;
    }

    @Override
    public Void visit(IIfElseExpression expr, Set<String> overallContext) {
        overallContext.add("<if>");
        overallContext.add("<else>");
        return null;
    }

    @Override
    public Void visit(IInvocationExpression expr, Set<String> overallContext) {
        // Add the name of invoked the method to the overall context
        overallContext.add(expr.getMethodName().getName());
        return null;
    }

    @Override
    public Void visit(ITypeCheckExpression expr, Set<String> overallContext) {
        // Not sure if a type check also contains a keyword
        overallContext.add(expr.getType().getName());
        return null;
    }

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

    @Override
    public Void visit(IDoLoop block, Set<String> overallContext) {
        overallContext.add("<do>");
        overallContext.add("<while>");
        return null;
    }

    @Override
    public Void visit(IForEachLoop block, Set<String> overallContext) {
        overallContext.add("<foreach>");
        overallContext.add("<in>");
        overallContext.add(block.getDeclaration().getType().getName());
        return null;
    }
}