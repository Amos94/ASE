/**
 * Copyright 2016 Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package Context;

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
     * @param dir
     * @return Context
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
     * @param dir
     * @return Context
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
     * @param zipFile
     * @return Context
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