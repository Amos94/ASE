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

import static org.junit.Assert.*;

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

    	assertEquals(result.size(), IdentifyEvents.findAllUsers().size());
    }
    
    //public static List<Context> readAllEvents() 
    @Test
    public void readAllEvents() {
    	iETest = new IdentifyEvents();
    	//TODO check with context
    	assertNotNull(IdentifyEvents.readAllEvents());
    }
    
    // public static List<Context> process(IDEEvent event) 
    @Test
    public void process() {
    	IdentifyEvents.process(event);
    }

    // public List<Context> getAggregatedContexts()
    @Test
    public void getAggregatedContexts() {
    	List<Context> test;
    	test = iETest.getAggregatedContexts();
        assertNotNull(test);
    }
}