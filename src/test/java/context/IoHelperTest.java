package context;

import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;
import org.junit.Test;
import org.junit.Before;

import java.io.File;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.model.events.completionevents.Context;
import utils.Configuration;


import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class IoHelperTest {


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

    @Test
    public void readFirstContext() {

        String firstZip = IoHelper.findAllZips(Configuration.TEST_CONTEXTS_DIR).stream().findFirst().get();
        try (IReadingArchive ra = new ReadingArchive(new File(Configuration.TEST_CONTEXTS_DIR, firstZip))) {
            assertTrue(ra.hasNext());
        }

    }

    @Test
    public void findAllZips() {
        Set<String> allZips = new Directory("src/test/java/data").findFiles(s -> {
            assert s != null;
            return s.endsWith(".zip");
        });

        for(String s : allZips){
            assertEquals(".zip", s.substring(s.length() - 4));
        }
    }
}