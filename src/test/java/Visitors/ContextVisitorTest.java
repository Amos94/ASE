package Visitors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.class)
public class ContextVisitorTest {
	
	@Mock private ContextVisitor visitor;
	@Mock private Set<String> overallContext;
	
	@Mock private IVariableDeclaration varDec;
	@Mock private IEventDeclaration eventDec;
	@Mock private IEventReference eventRef;
	@Mock private IMethodDeclaration methodDec;
	@Mock private IReturnStatement returnStmt;
	@Mock private ISwitchBlock switchBlock;
	@Mock private ICastExpression exp;
	@Mock private IForLoop forLoop;
	@Mock private IContinueStatement contStmt;
	@Mock private IGotoStatement gotoStmt;
	@Mock private IBreakStatement breakStmt;
	@Mock private IWhileLoop whileLoop;
	@Mock private IIfElseBlock ifBlock;
	@Mock private IIfElseExpression ifExp;
	@Mock private IInvocationExpression invoExp;
	@Mock private ITypeCheckExpression checkExp;
	@Mock private ITryBlock tryBlock;
	@Mock private IDoLoop doLoop;
	@Mock private IForEachLoop eachLoop;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
    
    @Test
    public void visit() {
    	visitor.visit(varDec, overallContext);
    	verify(visitor).visit(varDec, overallContext);
    }

    @Test
    public void visit1() {
    	visitor.visit(eventDec,overallContext);
    	verify(visitor).visit(eventDec, overallContext);
    }

    @Test
    public void visit2() {
    	visitor.visit(eventRef,overallContext);
    	verify(visitor).visit(eventRef, overallContext);
    }

    @Test
    public void visit3() {
    	visitor.visit(methodDec,overallContext);
    	verify(visitor).visit(methodDec, overallContext);
    }

    @Test
    public void visit4() {
    	visitor.visit(returnStmt,overallContext);
    	verify(visitor).visit(returnStmt, overallContext);
    }

    @Test
    public void visit5() {
    	visitor.visit(switchBlock,overallContext);
    	verify(visitor).visit(switchBlock, overallContext);
    }

    @Test
    public void visit6() {
    	visitor.visit(exp,overallContext);
    	verify(visitor).visit(exp, overallContext);
    }

    @Test
    public void visit7() {
    	visitor.visit(forLoop,overallContext);
    	verify(visitor).visit(forLoop, overallContext);
    }

    @Test
    public void visit8() {
    	visitor.visit(contStmt,overallContext);
    	verify(visitor).visit(contStmt, overallContext);
    }

    @Test
    public void visit9() {
    	visitor.visit(gotoStmt,overallContext);
    	verify(visitor).visit(gotoStmt, overallContext);
    }

    @Test
    public void visit10() {
    	visitor.visit(breakStmt,overallContext);
    	verify(visitor).visit(breakStmt, overallContext);
    }

    @Test
    public void visit11() {
    	visitor.visit(whileLoop,overallContext);
    	verify(visitor).visit(whileLoop, overallContext);
    }

    @Test
    public void visit12() {
    	visitor.visit(ifBlock,overallContext);
    	verify(visitor).visit(ifBlock, overallContext);
    }

    @Test
    public void visit13() {
    	visitor.visit(ifExp,overallContext);
    	verify(visitor).visit(ifExp, overallContext);
    }

    @Test
    public void visit14() {
    	visitor.visit(invoExp,overallContext);
    	verify(visitor).visit(invoExp, overallContext);
    }

    @Test
    public void visit15() {
    	visitor.visit(checkExp,overallContext);
    	verify(visitor).visit(checkExp, overallContext);
    }

    @Test
    public void visit16() {
    	visitor.visit(tryBlock,overallContext);
    	verify(visitor).visit(tryBlock, overallContext);
    }

    @Test
    public void visit17() {
    	visitor.visit(doLoop,overallContext);
    	verify(visitor).visit(doLoop, overallContext);
    }

    @Test
    public void visit18() {
    	visitor.visit(eachLoop,overallContext);
    	verify(visitor).visit(eachLoop, overallContext);
    }
}