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

import org.eclipse.jgit.lib.CoreConfig.EolStreamType;
import org.eclipse.jgit.lib.ObjectStream;
import org.eclipse.jgit.util.io.EolStreamTypeUtil;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

/** @author Réda Housni Alaoui */
public class AutoCRLFObjectStream extends ObjectStream {

    private final ObjectStream delegate;
    private final InputStream autoCRLFInputStream;

    public AutoCRLFObjectStream(ObjectStream delegate, EolStreamType eolStreamType) {
        this.delegate = requireNonNull(delegate);
        this.autoCRLFInputStream = requireNonNull(EolStreamTypeUtil.wrapInputStream(delegate, eolStreamType));
    }

    @Override
    public int getType() {
        return delegate.getType();
    }

    @Override
    public long getSize() {
        return delegate.getSize();
    }

    @Override
    public int read() throws IOException {
        return autoCRLFInputStream.read();
    }
}
