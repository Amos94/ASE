package Events;

import Utils.Configuration;

import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.IDEEvent;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IdentifyEventsTest {

	@Mock private IdentifyEvents iETest;
	@Mock private IDEEvent event;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
    @Test
    public void findAllUsers() {
    	iETest = new IdentifyEvents();
    	List<String> result = new ArrayList<>();
    	//TODO not hardcode it
    	result.add(Configuration.EVENTS_DIR);
//Returns a list of the zip files
//    	assertEquals(result, iETest.findAllUsers());
    }
    
    //public static List<Context> readAllEvents() 
    @Test
    public void readAllEvents() {
    	iETest = new IdentifyEvents();
    	//TODO check with context
    	assertNotNull(iETest.readAllEvents());
    }

    //public static void readPlainEvents()
    @Test
    public void readPlainEvents() {
//    	iETest.readPlainEvents();
//    	verify(iETest).readPlainEvents();
    }
    
    // public static List<Context> process(IDEEvent event) 
    @Test
    public void process() {
    	List<Context> test = new ArrayList<>();
    	iETest.process(event);
    }

    // public List<Context> getAggregatedContexts()
    @Test
    public void getAggregatedContexts() {
    	List<Context> test = new ArrayList<>();
    	test = iETest.getAggregatedContexts();
        assertNotNull(test);
    }
}