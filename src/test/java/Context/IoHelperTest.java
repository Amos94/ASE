package Context;

import cc.kave.commons.utils.io.Directory;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class IoHelperTest {

    @Test
    public void readFirstContext() {
    }

    @Test
    public void readAll() {
    }

    @Test
    public void read() {
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