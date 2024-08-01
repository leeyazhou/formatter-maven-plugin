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

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.AbbreviatedObjectId;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.CoreConfig.EolStreamType;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/** @author Réda Housni Alaoui */
public class AutoCRLFObjectReader extends ObjectReader {

  private final ObjectReader delegate;
  private final EolStreamType eolStreamType;

  public AutoCRLFObjectReader(ObjectReader delegate, EolStreamType eolStreamType) {
    this.delegate = requireNonNull(delegate);
    this.eolStreamType = requireNonNull(eolStreamType);
  }

  @Override
  public ObjectReader newReader() {
    return new AutoCRLFObjectReader(delegate.newReader(), eolStreamType);
  }

  @Override
  public Collection<ObjectId> resolve(AbbreviatedObjectId id) throws IOException {
    return delegate.resolve(id);
  }

  @Override
  public ObjectLoader open(AnyObjectId objectId, int typeHint)
      throws MissingObjectException, IncorrectObjectTypeException, IOException {
    return new AutoCRLFObjectLoader(delegate.open(objectId, typeHint), eolStreamType);
  }

  @Override
  public Set<ObjectId> getShallowCommits() throws IOException {
    return delegate.getShallowCommits();
  }

  @Override
  public void close() {
    delegate.close();
  }
}
