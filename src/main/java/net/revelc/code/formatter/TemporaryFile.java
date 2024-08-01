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
package net.revelc.code.formatter;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.maven.plugin.logging.Log;
import net.revelc.code.formatter.exception.MavenGitCodeFormatException;

/**
 * @author Réda Housni Alaoui
 * @author leeyazhou
 */
public class TemporaryFile implements Closeable {
  private final Log log;

  private final Path file;

  private TemporaryFile(Log log, String virtualName) {
    this.log = log;
    try {
      this.file = Files.createTempFile(null, null);
    } catch (IOException e) {
      throw new MavenGitCodeFormatException(e);
    }
    log.debug("Temporary file virtually named '" + virtualName + "' is viewable at '" + file + "'");
  }

  public static TemporaryFile create(Log log, String virtualName) {
    return new TemporaryFile(log, virtualName);
  }

  public OutputStream newOutputStream() throws IOException {
    return Files.newOutputStream(file);
  }

  public InputStream newInputStream() throws IOException {
    return Files.newInputStream(file);
  }

  public long size() throws IOException {
    return Files.size(file);
  }

  @Override
  public void close() throws IOException {
    if (log.isDebugEnabled()) {
      return;
    }
    Files.delete(file);
  }
}
