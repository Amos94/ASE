package events;


import helper.TestHelper;
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
    	List<String> result = new ArrayList<>();
    	//TODO not hardcode it
    	result.add(TestHelper.TEST_EVENTS_DIR);

    	assertEquals(result.size(), IdentifyEvents.findAllUsers(TestHelper.TEST_EVENTS_DIR).size());
    }

    //public static List<context> readAllEvents()
    @Test
    public void readAllEvents() {
    	assertNotNull(IdentifyEvents.readAllEvents(TestHelper.TEST_EVENTS_DIR));
    }

    // public static List<context> process(IDEEvent event)
    @Test
    public void process() {
    	IdentifyEvents.process(event);
    }

    // public List<context> getAggregatedContexts()
    @Test
    public void getAggregatedContexts() {
    	List<Context> test;
    	test = iETest.getAggregatedContexts();
        assertNotNull(test);
    }
}