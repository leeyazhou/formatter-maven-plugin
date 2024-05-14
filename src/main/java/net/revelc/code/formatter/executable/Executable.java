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
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Created on 08/11/17.
 *
 * @author Reda.Housni-Alaoui
 */
public interface Executable {

    /** Erase the executable content */
    Executable truncate() throws IOException;

    /**
     * @param template
     *            The template to truncate with
     * @param sourceEncoding
     *            The source encoding
     * @param values
     *            The values to use for the template interpolations
     */
    Executable truncateWithTemplate(Supplier<InputStream> template, String sourceEncoding, Object... values)
            throws IOException;

    /**
     * Appends a command call to the executable
     *
     * @param commandCall
     *            The command call to append to the executable
     */
    Executable appendCommandCall(String commandCall) throws IOException;

    /**
     * Remove a command call from the executable
     *
     * @param commandCall
     *            The command call to remove
     */
    Executable removeCommandCall(String commandCall);
}
