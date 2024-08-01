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
package net.revelc.code.formatter;

import java.io.IOException;
import java.nio.file.Path;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.api.errors.GitAPIException;
import net.revelc.code.formatter.git.GitStagedFiles;

/**
 * Created on 01/11/17.
 *
 * @author Reda.Housni-Alaoui
 */
@Mojo(name = "on-pre-commit", defaultPhase = LifecyclePhase.NONE, threadSafe = true)
public class OnPreCommitMojo extends AbstractModuleMavenGitCodeFormatMojo {

  @Override
  protected void doExecute() throws MojoExecutionException {
    try {
      getLog().info("Executing pre-commit hooks");
      onPreCommit();
      getLog().info("Executed pre-commit hooks");
    } catch (Exception e) {
      throw new MojoExecutionException(e.getMessage(), e);
    }
  }

  private void onPreCommit() throws IOException, GitAPIException {
    GitStagedFiles.read(getLog(), gitRepository(), this::isFormattable).format(codeFormatters());
  }

  private boolean isFormattable(Path path) {
    return sourceDirs().stream().anyMatch(path::startsWith);
  }
}
