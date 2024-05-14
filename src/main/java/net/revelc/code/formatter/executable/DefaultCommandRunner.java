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
package net.revelc.code.formatter.executable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Supplier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import net.revelc.code.formatter.exception.MavenGitCodeFormatException;

/** @author Réda Housni Alaoui */
public class DefaultCommandRunner implements CommandRunner {
    private final Supplier<Log> log;

    public DefaultCommandRunner(Supplier<Log> log) {
        this.log = log;
    }

    @Override
    public String run(Path workingDir, String... command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            if (workingDir != null) {
                processBuilder.directory(workingDir.toFile());
            }
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);

            log.get().debug("Executing '" + StringUtils.join(command, StringUtils.SPACE) + "'");
            Process process = processBuilder.start();

            String output = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8).trim()
                    + IOUtils.toString(process.getErrorStream(), StandardCharsets.UTF_8).trim();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new CommandRunException(exitCode, output, command);
            }

            log.get().debug(output);
            return StringUtils.defaultIfBlank(output, null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MavenGitCodeFormatException(e);
        } catch (IOException e) {
            throw new MavenGitCodeFormatException(e);
        }
    }
}
