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
/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package net.revelc.code.formatter.git;

import static java.util.Objects.requireNonNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheEditor;
import org.eclipse.jgit.dircache.DirCacheIterator;
import org.eclipse.jgit.lib.CoreConfig.AutoCRLF;
import org.eclipse.jgit.lib.CoreConfig.EolStreamType;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.WorkingTreeOptions;
import net.revelc.code.formatter.TemporaryFile;
import net.revelc.code.formatter.exception.MavenGitCodeFormatException;
import net.revelc.code.formatter.formatter.CodeFormatters;

/** @author Réda Housni Alaoui */
public class GitStagedFiles {

  private final Log log;
  private final Repository repository;
  private final Set<String> filePaths;
  private final EolStreamType eolStreamType;

  private GitStagedFiles(Log log, Repository repository, Set<String> filePaths) {
    this.log = requireNonNull(log);
    this.repository = requireNonNull(repository);
    this.filePaths = Collections.unmodifiableSet(filePaths);

    WorkingTreeOptions workingTreeOptions = repository.getConfig().get(WorkingTreeOptions.KEY);
    if (workingTreeOptions.getAutoCRLF() == AutoCRLF.TRUE) {
      eolStreamType = EolStreamType.AUTO_CRLF;
    } else {
      eolStreamType = EolStreamType.DIRECT;
    }
    log.debug("eolStreamType is '" + eolStreamType + "'");
  }

  public static GitStagedFiles read(Log log, Repository repository, Predicate<Path> fileFilter) throws GitAPIException {
    try (Git git = new Git(repository)) {
      Status gitStatus = git.status().call();
      Path workTree = repository.getWorkTree().toPath();
      Set<String> filePaths = Stream.concat(gitStatus.getChanged().stream(), gitStatus.getAdded().stream())
          .filter(relativePath -> fileFilter.test(workTree.resolve(relativePath))).collect(Collectors.toSet());
      log.debug("Staged files: " + filePaths.toString());
      return new GitStagedFiles(log, repository, filePaths);
    }
  }

  public void format(CodeFormatters formatters) throws IOException {

    try (Git git = new Git(repository);
        Index index = Index.lock(repository);
        TemporaryFile temporaryDiffFile = TemporaryFile.create(log, "diff-between-unformatted-and-formatted-files")) {
      DirCacheEditor dirCacheEditor = index.editor();
      filePaths.stream().map(path -> new GitIndexEntry(log, repository, path))
          .map(indexEntry -> indexEntry.entryFormatter(formatters)).forEach(dirCacheEditor::add);
      dirCacheEditor.finish();

      index.write();

      try (Repository autoCRLFRepository = new AutoCRLFRepository(git.getRepository().getDirectory(), eolStreamType);
          OutputStream diffOutput = temporaryDiffFile.newOutputStream();
          Git diffGit = new Git(autoCRLFRepository)) {
        diffGit.diff().setOutputStream(diffOutput).setOldTree(treeIterator(repository.readDirCache()))
            .setNewTree(index.treeIterator()).call();
      }

      try (InputStream diffInput = temporaryDiffFile.newInputStream()) {
        git.apply().setPatch(diffInput).call();
      }

      index.commit();
    } catch (GitAPIException e) {
      throw new MavenGitCodeFormatException(e);
    }
  }

  private AbstractTreeIterator treeIterator(DirCache dirCache) {
    return new DirCacheIterator(dirCache);
  }
}
