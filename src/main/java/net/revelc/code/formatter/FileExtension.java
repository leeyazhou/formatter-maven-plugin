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
package net.revelc.code.formatter;

import com.google.common.io.Files;
import java.nio.file.Path;
import java.util.Objects;

/** @author Réda Housni Alaoui */
public class FileExtension {

  private final String value;

  private FileExtension(String value) {
    this.value = value;
  }

  public static FileExtension parse(Path path) {
    return new FileExtension(Files.getFileExtension(path.getFileName().toString()));
  }

  public static FileExtension parse(String path) {
    return new FileExtension(Files.getFileExtension(path));
  }

  public static FileExtension of(String value) {
    return new FileExtension(value);
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FileExtension that = (FileExtension) o;

    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return value != null ? value.hashCode() : 0;
  }
}
