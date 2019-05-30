package context;

import helper.TestHelper;
import org.junit.Test;
import org.junit.Before;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.typeshapes.ITypeShape;

import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ContextHelperTest {

	@Mock private ContextHelper contextHelper;
	@Mock private Context ctx;
	@Mock private ISST sst;
	@Mock private ITypeShape ts;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
    @Test
    public void run() {
    	contextHelper.run();
    	verify(contextHelper).run();
    }

    @Test
    public void processZip() {
    	String zip = TestHelper.TEST_CONTEXTS_DIR;
    	contextHelper.processZip(zip);
    	verify(contextHelper).processZip(zip);
    }

    @Test
    public void getSST() {
    	ContextHelper test = new ContextHelper(TestHelper.TEST_CONTEXTS_DIR);
    	Context conTest = new Context();
    	assertNotNull(test.getSST(conTest));
    }

    @Test
    public void getTypeShape() {
    	ContextHelper test = new ContextHelper(TestHelper.TEST_CONTEXTS_DIR);
    	Context conTest = new Context();
    	assertNotNull(test.getTypeShape(conTest));
    }

    @Test
    public void processContext() {
    	contextHelper.processContext(ctx);
    	verify(contextHelper).processContext(ctx);
    }

    @Test
    public void process() {
    	contextHelper.process(sst);
    	verify(contextHelper).process(sst);
    }

    @Test
    public void process1() {
    	contextHelper.process(ts);
    	verify(contextHelper).process(ts);
    }
}