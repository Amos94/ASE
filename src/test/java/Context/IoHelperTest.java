package Context;

import Utils.Configuration;

import org.junit.Test;
import org.junit.Before;

import java.util.Set;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class IoHelperTest {
	
	@Mock private IoHelper ioHelper;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

    @Test
    public void readFirstContext() { //-> TODO 
    	//public static Context readFirstContext(String dir)
		//TODO: add file to git
    	String zipFile = "\\ASE\\Data\\Contexts\\01org\\acat\\src\\ACAT.sln-contexts.zip";
    	Context res;
    	Context expected = new Context();
//    	res = ioHelper.readFirstContext(Configuration.CONTEXTS_DIR);
//    	assertEquals(expected, res);
    }

    @Test
    public void readAll() { 
    	//public static List<Context> readAll(String dir)
    	String zipFile = "\\ASE\\Data\\Contexts\\01org\\acat\\src";
    	List<Context> res;
    	List<Context> expected = new LinkedList();
    	res = ioHelper.readAll(zipFile);
    	assertEquals(expected, res);
    }

    @Test
    public void read() {
    	//public static List<Context> read(String zipFile)
    	String zipFile = "\\ASE\\Data\\Contexts\\01org\\acat\\src\\ACAT.sln-contexts.zip";
    	List<Context> res;
    	List<Context> expected = new LinkedList();
    	res = ioHelper.read(zipFile);
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