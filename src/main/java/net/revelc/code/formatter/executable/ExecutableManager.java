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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;
import org.apache.maven.plugin.logging.Log;

/**
 * Created on 02/11/17.
 *
 * @author Reda.Housni-Alaoui
 */
public class ExecutableManager {

    private final Supplier<Log> log;

    public ExecutableManager(Supplier<Log> log) {
        requireNonNull(log);
        this.log = log;
    }

    /**
     * Get or creates a file then mark it as executable.
     *
     * @param file
     *            The file
     */
    public Executable getOrCreateExecutableScript(Path file) throws IOException {
        return new DefaulExecutable(log, file);
    }
}
