package com.advanced.software.engineering.aseproject;

import helper.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AseprojectApplicationTest {

    @Test
    public void contextLoads() {
        RecommenderInitialization recommenderInitialization = new RecommenderInitialization(TestHelper.TEST_CONTEXTS_DIR);
//        String contextsPath = recommenderInitialization.getContextsPath();
//        String eventsPath = recommenderInitialization.getEventsPath();
//        assertEquals(Configuration.CONTEXTS_DIR, contextsPath);
//        assertEquals(Configuration.EVENTS_DIR, eventsPath);
    }
}