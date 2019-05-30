package context;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

/**
 * this class explains how contexts can be read from the file system
 */
public class IoHelper {

    private static final Logger LOGGER = Logger.getLogger( IoHelper.class.getName() );

    /**
     * Read only the first context
     * s
     * @param dir - directory
     * @return context
     */
    public static Context readFirstContext(String dir) {
        for (String zip : findAllZips(dir)) {
            List<Context> ctxs = read(zip);
            if(!ctxs.isEmpty()) {
                return ctxs.get(0);
            }
        }
        return null;
    }

    /**
     * Read all Contexts
     *
     * @param dir - directory
     * @return context
     */
    public static List<Context> readAll(String dir) {
        LinkedList<Context> res = Lists.newLinkedList();

        for (String zip : findAllZips(dir)) {
            res.addAll(read(zip));
        }
        return res;
    }

    /**
     * Read list of Contexts
     *
     * @param zipFile - zip file
     * @return context
     */
    public static List<Context> read(String zipFile) {
        LinkedList<Context> res = Lists.newLinkedList();
        try (IReadingArchive ra = new ReadingArchive(new File(zipFile))) {
            while (ra.hasNext()) {
                res.add(ra.getNext(Context.class));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while reading zipFiles ", e);
        }
        return res;
    }

    /*
     * will recursively search for all .zip files in the "dir". The paths that are
     * returned are relative to "dir".
     */
    public static Set<String> findAllZips(String dir) {
        return new Directory(dir).findFiles(s -> s.endsWith(".zip"));
    }
}