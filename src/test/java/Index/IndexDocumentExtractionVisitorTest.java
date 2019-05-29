package Index;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.IStatement;

@RunWith(MockitoJUnitRunner.class)
public class IndexDocumentExtractionVisitorTest {


	@Mock private List<IStatement> body;
	@Mock private IInvertedIndex index;
	@Mock private IStatement statement;
	@Mock private IAssignableExpression expression;
	@Mock private IndexDocumentExtractionVisitorNoList op;
	private IndexDocumentExtractionVisitorNoList iV;
	private List<String> test1 = new ArrayList<>();

	@Before
	public void setup(){
		iV = new IndexDocumentExtractionVisitorNoList("testproject");
		MockitoAnnotations.initMocks(this);
	}

    @Test
    public void visit() {
    	List<IStatement> test = new ArrayList<>();
    	test.add(statement);
    	op.visit(test,index);
    	verify(op).visit(test,index);					//not sure what else to test

    }

    @Test
    public void doVisit() {
    	op.doVisit(expression, body, statement, index);
    	verify(op).doVisit(expression, body, statement, index);					//not sure what else to test

    }

    @Test
    public void identifierSanitization() {
		StringBuilder sb = new StringBuilder();

		for(String s : iV.identifierSanitization("IOException")){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("IO,Exception,",sb.toString());
		sb.setLength(0);

		for(String s : iV.identifierSanitization("stemmingCases")){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("stem,Case,",sb.toString());
		sb.setLength(0);

		for(String s : iV.identifierSanitization("youAreGoing")){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("you,Are,Go,",sb.toString());
    }

    @Test
    public void splitCamelCase() {							//is it okay that IO does not get split?
		StringBuilder sb = new StringBuilder();

		for(String s : iV.splitCamelCase("IOException")){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("IO,Exception,",sb.toString());
		sb.setLength(0);

		for(String s : iV.splitCamelCase("getMessage")){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("get,Message,",sb.toString());
		sb.setLength(0);

		for(String s : iV.splitCamelCase("GETMESSAGE")){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("GETMESSAGE,",sb.toString());
    }

    @Test
    public void stemIdentifiers() {
		StringBuilder sb = new StringBuilder();
		test1.add("stemming");

		for(String s : iV.stemIdentifiers(test1)){
			sb.append(s);
		}
		assertEquals("stem",sb.toString());
		sb.setLength(0);

		test1.add("getMessage");

		for(String s : iV.stemIdentifiers(test1)){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("stem,getMessag,",sb.toString());
    }

    @Test
    public void removeStopWords() {
		StringBuilder sb = new StringBuilder();
		test1.add("banana");
		for(String s : iV.removeStopWords(test1)){
			sb.append(s);
		}
		assertEquals("banana",sb.toString());
		sb.setLength(0);

    	test1.add("apple");
		for(String s : iV.removeStopWords(test1)){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("banana,apple,",sb.toString());
		sb.setLength(0);

    	test1.add("are");
    	for(String s : iV.removeStopWords(test1)){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("banana,apple,",sb.toString());
		sb.setLength(0);

    	test1.add("y o u");
		for(String s : iV.removeStopWords(test1)){
			sb.append(s);
			sb.append(",");
		}
		assertEquals("banana,apple,y o u,",sb.toString());
		sb.setLength(0);
    }

    @Test
    public void isStopWord() {
    	String s1 = "you";
    	String s2 = "yourself";
    	String s3 = "apple";
    	String s4 = "y o u";

		assertTrue(iV.isStopWord(s1));
		assertTrue(iV.isStopWord(s2));
		assertFalse(iV.isStopWord(s3));
		assertFalse(iV.isStopWord(s4));
    }
}