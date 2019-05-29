package Index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;


import cc.kave.commons.model.naming.codeelements.IMemberName;

import static org.junit.Assert.*;

public class IndexDocumentTest {
	
	private Set<String> context = new HashSet<>();
	private List<String> contextList = new ArrayList<>();
	private IMemberName method;
	private IndexDocument i1;
	private IndexDocument i2;
	private IndexDocument i3;
	private IndexDocument i4;
	private String resultID;
	private String resultMethodCall;
	private String resultType;
	private String resultContext;
	private String projectName;

	@Before
	public void setup(){

		resultID = "String_getName_ContextmoreContext";
		resultMethodCall = "getName";
		resultType = "String";
		resultContext = "ContextmoreContext";
		projectName = "Test-Proj";
		method = null;
		
		context.add("Context");
		context.add("moreContext");
		contextList.add("Context");
		contextList.add("moreContext");
		
		//IndexDocument(String methodCall, String type, Collection<String> overallContext)
		i1 = new IndexDocument(resultMethodCall, resultType, context);
		//IndexDocument(String methodCall, IMemberName method, String type, Collection<String> overallContext)
		i2 = new IndexDocument(resultMethodCall, method , resultType, context,projectName);
		//IndexDocument(String docId, String methodCall, String type, Collection<String> overallContext, long overallContextSimhash)
		i3 = new IndexDocument(resultID, resultMethodCall, resultType, context);
		//IndexDocument(String docId, IMemberName method, Collection<String> overallContext, long overallContextSimhash)
		i4 = new IndexDocument(resultID, method , context, projectName);
	}
	
    @Test
    public void getId() {    	
    	assertEquals(DigestUtils.sha256Hex(resultID), i1.getId());
    	assertEquals(DigestUtils.sha256Hex(resultID), i2.getId());
    	assertEquals(resultID, i3.getId());
    	assertEquals(resultID, i4.getId());
    }

	@Test
    public void getMethodCall() {
    	assertEquals(resultMethodCall, i1.getMethodCall());
    	assertEquals(resultMethodCall, i3.getMethodCall());
    }

    @Test
    public void getMethod() {
    	assertEquals(method, i1.getMethod());
    	assertEquals(method, i2.getMethod());
    	assertEquals(method, i3.getMethod());
    	assertEquals(method, i4.getMethod());
    }

    @Test
    public void getType() {
    	assertEquals(resultType, i1.getType());
    	assertEquals(resultType, i2.getType());
    	assertEquals(resultType, i3.getType());
    }

    @Test
    public void getOverallContext() {
		StringBuilder sb = new StringBuilder();
		for(String s : i1.getOverallContext()){
			sb.append(s);
		}
		assertEquals(resultContext,sb.toString());
		sb.setLength(0);
		
		for(String s : i2.getOverallContext()){
			sb.append(s);
		}
		assertEquals(resultContext,sb.toString());
		sb.setLength(0);	
		
		for(String s : i3.getOverallContext()){
			sb.append(s);
		}
		assertEquals(resultContext,sb.toString());
		sb.setLength(0);
		
		for(String s : i4.getOverallContext()){
			sb.append(s);
		}
		assertEquals(resultContext,sb.toString());
	}

    @Test
    public void getProject() {
    	String projectName = "Test-Proj";

		assertNull(i1.getProjectName());
    	assertEquals(projectName, i2.getProjectName());
		assertNull(i3.getProjectName());
    	assertEquals(projectName, i4.getProjectName());
    }

    @Test
    public void toString1() {
    	String result = "IndexDocument{" +
              "id='" + i1.getId() + '\'' +
              ", methodCall='" + i1.getMethodCall() + '\'' +
              ", type='" + i1.getType() + '\'' +
              ", overallContext=" + i1.getOverallContext() +
              ", projectname=" + i1.getProjectName() +
              '}';
    	assertEquals(result, i1.toString());
    }
    @Test
    public void toString2(){
    	String result = "IndexDocument{" +
                "id='" + i2.getId() + '\'' +
                ", methodCall='" + i2.getMethodCall() + '\'' +
                ", type='" + i2.getType() + '\'' +
                ", overallContext=" + i2.getOverallContext() +
                ", projectname=" + i2.getProjectName() +
                '}';
      	assertEquals(result, i2.toString());
    }
    @Test
    public void toString3(){
    	String result = "IndexDocument{" +
                "id='" + i3.getId() + '\'' +
                ", methodCall='" + i3.getMethodCall() + '\'' +
                ", type='" + i3.getType() + '\'' +
                ", overallContext=" + i3.getOverallContext() +
                ", projectname=" + i3.getProjectName() +
                '}';
      	assertEquals(result, i3.toString());
    }
    @Test
    public void toString4(){
    	String result = "IndexDocument{" +
                "id='" + i4.getId() + '\'' +
                ", methodCall='" + i4.getMethodCall() + '\'' +
                ", type='" + i4.getType() + '\'' +
                ", overallContext=" + i4.getOverallContext() +
                ", projectname=" + i4.getProjectName() +
                '}';
      	assertEquals(result, i4.toString());
    }
    
    @Test
    public void equals1() {
    	IndexDocument t1 = new IndexDocument(resultMethodCall, resultType, context);
		assertEquals(t1, i1);
    	IndexDocument t2 = new IndexDocument(resultMethodCall, method , resultType, context, projectName);
		assertEquals(t2, i2);
    	IndexDocument t3 = new IndexDocument(resultID, resultMethodCall, resultType, context);
		assertEquals(t3, i3);
    	IndexDocument t4 = new IndexDocument(resultID, method , context, projectName);
    	assertEquals(t4, i4);
    }
    
    @Test
    public void hashCode1() {
    	assertEquals(resultID.hashCode(), i3.hashCode());
    	assertEquals(resultID.hashCode(), i4.hashCode());
    	
    }

	@Test
	public void testContextList(){
		assertFalse(context.isEmpty());

		assertEquals(contextList.size(),i1.getOverallContext().size());
		assertEquals(contextList.size(),i2.getOverallContext().size());
		assertEquals(contextList.size(),i3.getOverallContext().size());
		assertEquals(contextList.size(),i4.getOverallContext().size());

		assertEquals(contextList.get(0), i1.getOverallContext().get(0));
		assertEquals(contextList.get(1), i1.getOverallContext().get(1));

		assertEquals(contextList.get(0), i2.getOverallContext().get(0));
		assertEquals(contextList.get(1), i2.getOverallContext().get(1));

		assertEquals(contextList.get(0), i3.getOverallContext().get(0));
		assertEquals(contextList.get(1), i3.getOverallContext().get(1));

		assertEquals(contextList.get(0), i4.getOverallContext().get(0));
		assertEquals(contextList.get(1), i4.getOverallContext().get(1));
	}
}