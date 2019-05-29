package Context;

import Utils.Configuration;

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


import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class IoHelperTest {
	

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

    @Test
    public void readFirstContext() {
    	Context res;

        String firstZip = IoHelper.findAllZips(Configuration.CONTEXTS_DIR).stream().findFirst().get();

        try (IReadingArchive ra = new ReadingArchive(new File(Configuration.CONTEXTS_DIR, firstZip))) {
            res = ra.getNext(Context.class);

        }

    	assertNotNull(res);
    }

    @Test
    public void readAll() { 
    	//public static List<Context> readAll(String dir)
    	String zipFile = "\\ASE\\Data\\Contexts\\01org\\acat\\src";
    	List<Context> res;
    	LinkedList expected = new LinkedList();
    	res = IoHelper.readAll(zipFile);
    	assertEquals(expected, res);
    }

    @Test
    public void read() {
    	//public static List<Context> read(String zipFile)
    	String zipFile = "\\ASE\\Data\\Contexts\\01org\\acat\\src\\ACAT.sln-contexts.zip";
    	List<Context> res;
    	LinkedList expected = new LinkedList();
    	res = IoHelper.read(zipFile);
    	assertEquals(expected, res);
    }

    @Test
    public void findAllZips() {
        Set<String> allZips = new Directory("Data/").findFiles(s -> {
            assert s != null;
            return s.endsWith(".zip");
        });

        for(String s : allZips){
            assertEquals(".zip", s.substring(s.length() - 4));
        }
    }
}