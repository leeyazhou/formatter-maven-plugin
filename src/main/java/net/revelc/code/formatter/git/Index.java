/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter.git;

import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEditor;
import org.eclipse.jgit.dircache.DirCacheIterator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;

import java.io.IOException;

/** @author Réda Housni Alaoui */
public class Index implements AutoCloseable {

    private final DirCache dirCache;

    private Index(Repository repository) throws IOException {
        dirCache = repository.lockDirCache();
    }

    public static Index lock(Repository repository) throws IOException {
        return new Index(repository);
    }

    public DirCacheEditor editor() {
        return dirCache.editor();
    }

    public void write() throws IOException {
        dirCache.write();
    }

    public void commit() {
        dirCache.commit();
    }

    public AbstractTreeIterator treeIterator() {
        return new DirCacheIterator(dirCache);
    }

    @Override
    public void close() {
        dirCache.unlock();
    }
}
